USE socialnetwork;
CREATE TABLE suggestions
(
    suggestion_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id        BIGINT,
    friend_id      BIGINT,
    suggest_point  INT,
    mutual_friends INT,
    status         ENUM('BLOCK','FRIEND','NONE') default 'NONE'
);

INSERT INTO suggestions (user_id, friend_id, suggest_point, mutual_friends, status)
VALUES (1, 2, 20, 0, 'FRIEND'),
       (1, 3, 10, 0, 'BLOCK'),
       (2, 3, 10, 0, 'NONE');

ALTER TABLE posts
    ADD last_comment DATETIME DEFAULT '2000-01-01 00:00:00';

-- Tạo 300 bản ghi ngẫu nhiên cho bảng relationships
DELIMITER //

CREATE PROCEDURE populate_suggestions()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE j INT DEFAULT 1;
    DECLARE user_id_1 BIGINT;
    DECLARE user_id_2 BIGINT;
    DECLARE location_1 VARCHAR(255);
    DECLARE location_2 VARCHAR(255);
    DECLARE work_1 VARCHAR(255);
    DECLARE work_2 VARCHAR(255);
    DECLARE education_1 VARCHAR(255);
    DECLARE education_2 VARCHAR(255);
    DECLARE suggest_point INT;

    -- Loop through each user to create suggestions
    WHILE i <= 53 DO
            -- Get user details for user_id i
            SELECT user_id, location, work, education INTO user_id_1, location_1, work_1, education_1
            FROM users
            WHERE user_id = i;

            SET j = i + 1;

            -- Loop through each other user to create pairs
            WHILE j <= 53 DO
                    IF NOT ((i = 1 AND j = 2) OR (i = 2 AND j = 3) OR (i = 1 AND j = 3)) THEN
                        -- Get user details for user_id j
                        SELECT user_id, location, work, education INTO user_id_2, location_2, work_2, education_2
                        FROM users
                        WHERE user_id = j;

                        -- Calculate suggest_point
                        SET suggest_point = 0;
                        IF location_1 = location_2 THEN
                            SET suggest_point = suggest_point + 10;
                        END IF;
                        IF work_1 = work_2 THEN
                            SET suggest_point = suggest_point + 10;
                        END IF;
                        IF education_1 = education_2 THEN
                            SET suggest_point = suggest_point + 10;
                        END IF;

                        -- Insert into suggestions table
                        INSERT INTO suggestions (user_id, friend_id, suggest_point, mutual_friends, status)
                        VALUES (user_id_1,
                                user_id_2,
                                suggest_point,
                                0,
                                CASE
                                    WHEN RAND() < 0.33 THEN 'FRIEND'
                                    WHEN RAND() < 0.66 THEN 'BLOCK'
                                    ELSE 'NONE'
                                END);
                    END IF;
                    SET j = j + 1;
                END WHILE;

            SET i = i + 1;
        END WHILE;
END//

DELIMITER ;

-- Gọi thủ tục để tạo dữ liệu
CALL populate_suggestions();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE populate_suggestions;