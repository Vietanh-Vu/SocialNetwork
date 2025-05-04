import torch
from transformers import AutoTokenizer, AutoModelForSequenceClassification
from torch.nn.functional import softmax
import logging

# Set up logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


class HateSpeechModel:
    _instance = None

    @classmethod
    def get_instance(cls, model_path):
        """Singleton pattern để đảm bảo chỉ load model 1 lần"""
        if cls._instance is None:
            cls._instance = cls(model_path)
        return cls._instance

    def __init__(self, model_path):
        """
        Khởi tạo model Transformer cho việc phát hiện hate speech

        Args:
            model_path (str): Đường dẫn đến thư mục chứa model
        """
        try:
            logger.info(f"Loading model from {model_path}")
            self.tokenizer = AutoTokenizer.from_pretrained(model_path)
            self.model = AutoModelForSequenceClassification.from_pretrained(model_path)
            self.model.eval()
            self.label_map = {0: "CLEAN", 1: "HATE"}

            # Kiểm tra CUDA
            self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
            self.model.to(self.device)
            logger.info(f"Model loaded successfully on {self.device}")
        except Exception as e:
            logger.error(f"Error loading model: {str(e)}")
            raise

    def predict(self, text):
        """
        Dự đoán mức độ hate speech của một đoạn văn bản

        Args:
            text (str): Văn bản cần phân tích

        Returns:
            dict: Kết quả dự đoán bao gồm nhãn và xác suất
        """
        try:
            # Tokenize
            inputs = self.tokenizer(text, return_tensors="pt", truncation=True, padding=True)
            inputs = {k: v.to(self.device) for k, v in inputs.items()}

            # Inference
            with torch.no_grad():
                outputs = self.model(**inputs)

            # Softmax để lấy xác suất
            probs = softmax(outputs.logits, dim=1)
            label_id = torch.argmax(probs, dim=1).item()

            # Kết quả
            results = {
                "label": self.label_map[label_id],
                "confidence": float(probs[0][label_id].item()),
                "probabilities": {
                    self.label_map[i]: float(probs[0][i].item()) for i in self.label_map
                }
            }

            return results
        except Exception as e:
            logger.error(f"Error during prediction: {str(e)}")
            raise