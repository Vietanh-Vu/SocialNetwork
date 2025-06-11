import os
import pymysql.cursors
from dotenv import load_dotenv
import logging

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

    # Database configuration
    DB_CONFIG = {
        'host': os.getenv('DB_HOST', 'localhost'),
        'port': int(os.getenv('DB_PORT', 3306)),
        'user': os.getenv('DB_USER', 'root'),
        'password': os.getenv('DB_PASS', 'password'),
        'database': os.getenv('DB_NAME', 'socialnetwork'),
        'charset': 'utf8mb4',
        'cursorclass': pymysql.cursors.DictCursor
    }

    @staticmethod
    def get_ip_whitelist_from_db():
        """Lấy danh sách IP được phép từ cơ sở dữ liệu"""
        try:
            connection = pymysql.connect(**Config.DB_CONFIG)
            with connection.cursor() as cursor:
                # Truy vấn global_configs để lấy danh sách IP
                sql = "SELECT `desc` FROM `global_configs` WHERE `code` = 'detect_comment_ip_whitelist'"
                cursor.execute(sql)
                result = cursor.fetchone()

                if result and result['desc']:
                    ip_list = result['desc'].split(',')
                    return ip_list
                return []
        except Exception as e:
            import logging
            logging.getLogger(__name__).error(f"Error loading IP whitelist from database: {str(e)}")
            return []
        finally:
            if 'connection' in locals() and connection:
                connection.close()