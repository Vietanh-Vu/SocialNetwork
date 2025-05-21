USE socialnetwork;

DROP TABLE IF EXISTS suggestions;

CREATE TABLE suggestions
(
    suggestion_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id        BIGINT,
    friend_id      BIGINT,
    suggest_point  INT,
    status         ENUM('BLOCK','FRIEND','NONE') default 'NONE'
);

-- Add indexes to suggestions table
CREATE INDEX idx_suggestions_user_id ON suggestions(user_id);
CREATE INDEX idx_suggestions_friend_id ON suggestions(friend_id);
CREATE INDEX idx_suggestions_user_friend ON suggestions(user_id, friend_id);

INSERT INTO suggestions (user_id, friend_id, suggest_point, status)
VALUES (1, 2, 20, 'FRIEND'),
       (1, 3, 10, 'BLOCK'),
       (2, 3, 10, 'NONE');

ALTER TABLE posts
    ADD last_comment DATETIME DEFAULT '2000-01-01 00:00:00';

-- Tạo dữ liệu gợi ý
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

    WHILE i <= 53 DO
SELECT user_id, location, work, education INTO user_id_1, location_1, work_1, education_1
FROM users WHERE user_id = i;

SET j = i + 1;

        WHILE j <= 53 DO
            IF NOT ((i = 1 AND j = 2) OR (i = 2 AND j = 3) OR (i = 1 AND j = 3)) THEN
SELECT user_id, location, work, education INTO user_id_2, location_2, work_2, education_2
FROM users WHERE user_id = j;

SET suggest_point = 0;
                IF location_1 = location_2 THEN SET suggest_point = suggest_point + 10; END IF;
                IF work_1 = work_2 THEN SET suggest_point = suggest_point + 10; END IF;
                IF education_1 = education_2 THEN SET suggest_point = suggest_point + 10; END IF;

INSERT INTO suggestions (user_id, friend_id, suggest_point, status)
VALUES (user_id_1, user_id_2, suggest_point,
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

CALL populate_suggestions();
DROP PROCEDURE populate_suggestions;
