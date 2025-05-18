-- Thay thế tất cả các ký tự tiếng Việt có dấu trong trường email bằng ký tự không dấu
UPDATE users SET first_name = 'User', last_name = '1' where username = 'user1';
UPDATE users SET first_name = 'User', last_name = '2' where username = 'user2';
UPDATE users SET first_name = 'User', last_name = '3' where username = 'user3';

UPDATE users
SET email = LOWER(email);

UPDATE users
SET email = REPLACE(email, 'á', 'a');
UPDATE users
SET email = REPLACE(email, 'à', 'a');
UPDATE users
SET email = REPLACE(email, 'ả', 'a');
UPDATE users
SET email = REPLACE(email, 'ã', 'a');
UPDATE users
SET email = REPLACE(email, 'ạ', 'a');
UPDATE users
SET email = REPLACE(email, 'ă', 'a');
UPDATE users
SET email = REPLACE(email, 'ắ', 'a');
UPDATE users
SET email = REPLACE(email, 'ằ', 'a');
UPDATE users
SET email = REPLACE(email, 'ẳ', 'a');
UPDATE users
SET email = REPLACE(email, 'ẵ', 'a');
UPDATE users
SET email = REPLACE(email, 'ặ', 'a');
UPDATE users
SET email = REPLACE(email, 'â', 'a');
UPDATE users
SET email = REPLACE(email, 'ấ', 'a');
UPDATE users
SET email = REPLACE(email, 'ầ', 'a');
UPDATE users
SET email = REPLACE(email, 'ẩ', 'a');
UPDATE users
SET email = REPLACE(email, 'ẫ', 'a');
UPDATE users
SET email = REPLACE(email, 'ậ', 'a');

UPDATE users
SET email = REPLACE(email, 'é', 'e');
UPDATE users
SET email = REPLACE(email, 'è', 'e');
UPDATE users
SET email = REPLACE(email, 'ẻ', 'e');
UPDATE users
SET email = REPLACE(email, 'ẽ', 'e');
UPDATE users
SET email = REPLACE(email, 'ẹ', 'e');
UPDATE users
SET email = REPLACE(email, 'ê', 'e');
UPDATE users
SET email = REPLACE(email, 'ế', 'e');
UPDATE users
SET email = REPLACE(email, 'ề', 'e');
UPDATE users
SET email = REPLACE(email, 'ể', 'e');
UPDATE users
SET email = REPLACE(email, 'ễ', 'e');
UPDATE users
SET email = REPLACE(email, 'ệ', 'e');

UPDATE users
SET email = REPLACE(email, 'í', 'i');
UPDATE users
SET email = REPLACE(email, 'ì', 'i');
UPDATE users
SET email = REPLACE(email, 'ỉ', 'i');
UPDATE users
SET email = REPLACE(email, 'ĩ', 'i');
UPDATE users
SET email = REPLACE(email, 'ị', 'i');

UPDATE users
SET email = REPLACE(email, 'ó', 'o');
UPDATE users
SET email = REPLACE(email, 'ò', 'o');
UPDATE users
SET email = REPLACE(email, 'ỏ', 'o');
UPDATE users
SET email = REPLACE(email, 'õ', 'o');
UPDATE users
SET email = REPLACE(email, 'ọ', 'o');
UPDATE users
SET email = REPLACE(email, 'ô', 'o');
UPDATE users
SET email = REPLACE(email, 'ố', 'o');
UPDATE users
SET email = REPLACE(email, 'ồ', 'o');
UPDATE users
SET email = REPLACE(email, 'ổ', 'o');
UPDATE users
SET email = REPLACE(email, 'ỗ', 'o');
UPDATE users
SET email = REPLACE(email, 'ộ', 'o');
UPDATE users
SET email = REPLACE(email, 'ơ', 'o');
UPDATE users
SET email = REPLACE(email, 'ớ', 'o');
UPDATE users
SET email = REPLACE(email, 'ờ', 'o');
UPDATE users
SET email = REPLACE(email, 'ở', 'o');
UPDATE users
SET email = REPLACE(email, 'ỡ', 'o');
UPDATE users
SET email = REPLACE(email, 'ợ', 'o');

UPDATE users
SET email = REPLACE(email, 'ú', 'u');
UPDATE users
SET email = REPLACE(email, 'ù', 'u');
UPDATE users
SET email = REPLACE(email, 'ủ', 'u');
UPDATE users
SET email = REPLACE(email, 'ũ', 'u');
UPDATE users
SET email = REPLACE(email, 'ụ', 'u');
UPDATE users
SET email = REPLACE(email, 'ư', 'u');
UPDATE users
SET email = REPLACE(email, 'ứ', 'u');
UPDATE users
SET email = REPLACE(email, 'ừ', 'u');
UPDATE users
SET email = REPLACE(email, 'ử', 'u');
UPDATE users
SET email = REPLACE(email, 'ữ', 'u');
UPDATE users
SET email = REPLACE(email, 'ự', 'u');

UPDATE users
SET email = REPLACE(email, 'ý', 'y');
UPDATE users
SET email = REPLACE(email, 'ỳ', 'y');
UPDATE users
SET email = REPLACE(email, 'ỷ', 'y');
UPDATE users
SET email = REPLACE(email, 'ỹ', 'y');
UPDATE users
SET email = REPLACE(email, 'ỵ', 'y');

UPDATE users
SET email = REPLACE(email, 'đ', 'd');
-- Done
