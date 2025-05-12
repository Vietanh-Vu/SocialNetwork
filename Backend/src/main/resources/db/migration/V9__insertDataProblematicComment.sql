-- V1__insert_problematic_comments.sql

DELIMITER $$

CREATE PROCEDURE insert_problematic_comments()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE rand_month INT;
    DECLARE rand_day INT;
    DECLARE max_day INT;
    DECLARE created_date DATE;

    WHILE i < 10000 DO
        -- Chọn tháng ngẫu nhiên từ 1 đến 12
        SET rand_month = FLOOR(RAND() * 12) + 1;

        -- Lấy số ngày tối đa trong tháng đó (năm 2024 là năm nhuận)
        SET max_day = CASE
            WHEN rand_month IN (1,3,5,7,8,10,12) THEN 31
            WHEN rand_month = 2 THEN 29
            ELSE 30
END;

        -- Chọn ngày ngẫu nhiên trong tháng
        SET rand_day = FLOOR(RAND() * max_day) + 1;

        -- Tạo ngày hợp lệ trong năm 2024
        SET created_date = STR_TO_DATE(CONCAT('2024-', LPAD(rand_month, 2, '0'), '-', LPAD(rand_day, 2, '0')), '%Y-%m-%d');

INSERT INTO problematic_comments (user_id, content, spam_probability, created_at)
VALUES (
           -- user_id: phân phối gần chuẩn
           LEAST(GREATEST(FLOOR(((RAND() + RAND() + RAND()) / 3) * 53) + 1, 1), 53),

           -- content
           CONCAT('This is a sample comment #', i + 1),

           -- spam_probability: 0.5–1.0
           ROUND(RAND() * 0.5 + 0.5, 4),

           -- created_at
           created_date
       );
SET i = i + 1;
END WHILE;
END$$

DELIMITER ;

-- Gọi procedure
CALL insert_problematic_comments();

-- Dọn thủ tục sau khi gọi xong
DROP PROCEDURE insert_problematic_comments;
