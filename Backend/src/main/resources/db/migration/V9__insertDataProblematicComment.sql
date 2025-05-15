DELIMITER $$

CREATE PROCEDURE insert_problematic_comments()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE rand_days_ago INT;
    DECLARE created_date DATE;

    WHILE i < 1000000 DO
        -- Chọn ngẫu nhiên số ngày lùi từ hiện tại (0 đến 364)
        SET rand_days_ago = FLOOR(RAND() * 365);

        -- Tạo ngày bằng cách trừ đi số ngày ngẫu nhiên từ CURRENT_DATE
        SET created_date = DATE_SUB(CURRENT_DATE(), INTERVAL rand_days_ago DAY);

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
