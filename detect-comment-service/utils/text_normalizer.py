import re
import pymysql
import json
import logging
import unicodedata

# Set up logging
logger = logging.getLogger(__name__)


class TextNormalizer:
    def __init__(self, db_config: dict):
        self.db_config = db_config
        self.abbreviations = {}
        self.leet_mapping = {}
        self.homoglyphs_mapping = {}
        self.setup_patterns()
        self.load_config_from_db()
        # Danh sách các ký tự đặc biệt cần loại bỏ giữa các chữ cái
        self.special_chars = "!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/~`"

    def setup_patterns(self):
        self.extra_spaces_pattern = re.compile(r'\s+')
        self.repeated_chars_pattern = re.compile(r'(.)\1{2,}')
        self.emoji_pattern = re.compile(r'[\U0001F600-\U0001F64F]|[\U0001F300-\U0001F5FF]')

    def load_config_from_db(self):
        try:
            conn = pymysql.connect(**self.db_config)
            with conn.cursor() as cursor:
                cursor.execute(
                    "SELECT code, `desc` FROM global_configs WHERE code IN ('ABBREVIATIONS', 'LEET_MAPPING', 'HOMOGLYPHS_MAPPING')"
                )
                results = cursor.fetchall()

                for row in results:
                    code = row.get('code')
                    desc = row.get('desc')

                    try:
                        if not desc or not desc.strip():
                            continue

                        data = json.loads(desc)
                        if code == 'ABBREVIATIONS':
                            self.abbreviations = data
                            logger.info(f"Loaded abbreviations: {len(self.abbreviations)}")
                        elif code == 'LEET_MAPPING':
                            self.leet_mapping = data
                            logger.info(f"Loaded leet mapping: {len(self.leet_mapping)}")
                        elif code == 'HOMOGLYPHS_MAPPING':
                            self.homoglyphs_mapping = data
                            logger.info(f"Loaded homoglyphs mapping: {len(self.homoglyphs_mapping)}")
                    except Exception:
                        logger.warning(f"Failed parsing config for {code}")
        except Exception as e:
            logger.error(f"Error loading config from DB: {e}")

    def normalize_text(self, text: str) -> str:
        # Giữ nguyên case ban đầu để xử lý, chỉ lowercase ở cuối
        text = text.lower()
        text = self.remove_emojis(text)
        text = self.remove_special_chars_between_letters(text)
        text = self.normalize_homoglyphs(text)
        text = self.normalize_leet_speak(text)
        text = self.remove_repeated_chars(text)
        text = self.expand_abbreviations(text)
        text = self.normalize_spacing(text)
        return text

    def remove_emojis(self, text: str) -> str:
        return self.emoji_pattern.sub(' ', text)

    def remove_special_chars_between_letters(self, text: str) -> str:
        """Lọc các ký tự đặc biệt giữa các chữ cái bằng thao tác trực tiếp"""
        # Tách văn bản thành các từ để xử lý riêng
        words = text.split()
        cleaned_words = []

        for word in words:
            # Chuyển từ thành danh sách ký tự để dễ thao tác
            chars = list(word)
            result = []

            i = 0
            while i < len(chars):
                # Thêm ký tự hiện tại vào kết quả
                result.append(chars[i])

                # Kiểm tra nếu đây là chữ cái và còn ký tự phía sau
                if i < len(chars) - 2 and self.is_letter(chars[i]):
                    # Kiểm tra xem ký tự tiếp theo có phải đặc biệt không và ký tự sau nó có phải chữ không
                    if chars[i + 1] in self.special_chars and self.is_letter(chars[i + 2]):
                        # Bỏ qua ký tự đặc biệt (không thêm vào kết quả)
                        i += 1

                i += 1

            cleaned_words.append(''.join(result))

        return ' '.join(cleaned_words)

    def is_letter(self, char: str) -> bool:
        """Kiểm tra xem một ký tự có phải là chữ cái (bao gồm cả tiếng Việt) không"""
        # Sử dụng unicodedata để kiểm tra chính xác hơn
        try:
            # Kiểm tra xem ký tự có phải là chữ cái không
            category = unicodedata.category(char)
            return category.startswith('L')  # 'L' là category cho các chữ cái
        except:
            # Fallback cho trường hợp lỗi
            return char.isalpha()

    def normalize_leet_speak(self, text: str) -> str:
        # Cần xử lý cả case-sensitive và case-insensitive
        for k, v in self.leet_mapping.items():
            text = text.replace(k, v)
        return text

    def normalize_homoglyphs(self, text: str) -> str:
        for k, v in self.homoglyphs_mapping.items():
            text = text.replace(k, v)
        return text

    def remove_repeated_chars(self, text: str) -> str:
        return self.repeated_chars_pattern.sub(r'\1\1', text)

    def expand_abbreviations(self, text: str) -> str:
        words = text.split()
        return ' '.join(self.abbreviations.get(w, w) for w in words)

    def normalize_spacing(self, text: str) -> str:
        return self.extra_spaces_pattern.sub(' ', text).strip()

