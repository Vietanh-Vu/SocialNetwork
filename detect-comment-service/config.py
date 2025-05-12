import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()


class Config:
    # Flask settings
    SECRET_KEY = os.getenv('SECRET_KEY', 'your-secret-key-should-be-in-env')
    DEBUG = os.getenv('DEBUG', 'False').lower() == 'true'

    # API security - API Key only
    API_TOKEN = os.getenv('API_TOKEN', 'your-api-token-should-be-in-env')
    API_KEY_HEADER = 'X-API-KEY'

    # Model settings
    MODEL_PATH = os.getenv('MODEL_PATH', 'hate_speech_model')

    # Allowed origins for CORS
    ALLOWED_ORIGINS = os.getenv('ALLOWED_ORIGINS', 'http://api-service:8080,http://localhost:8080').split(',')

    # Allowed client IPs
    ALLOWED_IPS = os.getenv('ALLOWED_IPS', '127.0.0.1,::1,172.20.0.1,172.21.0.1').split(',')