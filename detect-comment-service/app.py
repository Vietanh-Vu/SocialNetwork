from flask import Flask, request, jsonify
from flask_cors import CORS
from functools import wraps
import logging
from datetime import datetime
import ipaddress

from config import Config
from utils.model_utils import HateSpeechModel
from utils.text_normalizer import TextNormalizer

# Initialize Flask app
app = Flask(__name__)

# Configure CORS
# CORS(app, origins="*")
CORS(app, resources={
    r"/api/*": {
        "origins": Config.ALLOWED_ORIGINS,
        "methods": ["POST", "OPTIONS"],
        "allow_headers": ["Content-Type", Config.API_KEY_HEADER]
    }
})

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize the model
model = None
normalizer = None

def initialize_model():
    global model, normalizer
    model = HateSpeechModel.get_instance(Config.MODEL_PATH)
    normalizer = TextNormalizer(Config.DB_CONFIG)


# Authentication decorator
def require_api_key(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        # Check for API key in header
        api_key = request.headers.get(Config.API_KEY_HEADER)
        if not api_key or api_key != Config.API_TOKEN:
            logger.warning(f"Invalid API key attempt from {request.remote_addr}")
            return jsonify({"error": "Unauthorized"}), 401
        return f(*args, **kwargs)

    return decorated_function


# IP whitelist decorator
def ip_whitelist(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        client_ip = request.remote_addr
        if client_ip not in Config.ALLOWED_IPS:
            try:
                # Check if the IP is in any allowed subnets
                client_ip_obj = ipaddress.ip_address(client_ip)
                allowed = any(
                    client_ip_obj in ipaddress.ip_network(allowed_ip, strict=False)
                    for allowed_ip in Config.ALLOWED_IPS if '/' in allowed_ip
                )
                if not allowed:
                    logger.warning(f"Access attempt from unauthorized IP: {client_ip}")
                    return jsonify({"error": "Forbidden"}), 403
            except ValueError:
                # If there's an error parsing IPs, deny access
                return jsonify({"error": "Forbidden"}), 403
        return f(*args, **kwargs)

    return decorated_function


# Main API endpoint for hate speech detection
@app.route('/api/detect', methods=['POST'])
@require_api_key
@ip_whitelist
def detect_hate_speech():
    if not request.is_json:
        return jsonify({"error": "Request must be JSON"}), 400

    data = request.get_json()
    if 'text' not in data or not data['text'].strip():
        return jsonify({"error": "Text is required"}), 400

    text = data['text'].strip()

    try:
        # Dự đoán trên text gốc
        original_result = model.predict(text)

        # Dự đoán trên text đã được normalize
        normalized_text = normalizer.normalize_text(text)
        normalized_result = model.predict(normalized_text)

        # Lấy xác suất hate của cả hai kết quả
        original_hate_prob = original_result["probabilities"]["HATE"]
        normalized_hate_prob = normalized_result["probabilities"]["HATE"]

        response = {
            "original_text": text,
            "normalized_text": normalized_text,
            "original_hate_probability": original_hate_prob,
            "normalized_hate_probability": normalized_hate_prob,
            "timestamp": datetime.now().isoformat()
        }

        return jsonify(response), 200

    except Exception as e:
        logger.error(f"Error processing request: {str(e)}")
        return jsonify({"error": "Error processing request"}), 500

# Health check endpoint - unprotected for monitoring
@app.route('/health', methods=['GET'])
def health_check():
    try:
        # Simple model test to ensure it's loaded properly
        _ = model.predict("Test message")
        return jsonify({"status": "healthy"}), 200
    except Exception as e:
        logger.error(f"Health check failed: {str(e)}")
        return jsonify({"status": "unhealthy", "error": str(e)}), 500


if __name__ == '__main__':
    initialize_model()
    app.run(host='0.0.0.0', port=5000, debug=Config.DEBUG)
else:
    # For WSGI servers
    initialize_model()