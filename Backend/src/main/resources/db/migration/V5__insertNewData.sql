DELIMITER //

CREATE PROCEDURE populate_relationships_from_suggestions()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE sug_user_id BIGINT;
    DECLARE sug_friend_id BIGINT;
    DECLARE sug_status ENUM('BLOCK', 'FRIEND', 'NONE');

    -- Declare cursor for suggestions table
    DECLARE cur_suggestions CURSOR FOR
SELECT user_id, friend_id, status
FROM suggestions;

-- Declare continue handler for cursor
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN cur_suggestions;

-- Loop through each suggestion record
read_loop: LOOP
        FETCH cur_suggestions INTO sug_user_id, sug_friend_id, sug_status;
        IF done THEN
            LEAVE read_loop;
END IF;

        -- Insert into relationships table based on the status
CASE sug_status
            WHEN 'FRIEND' THEN
                INSERT INTO relationships (user_id, friend_id, created_at, relation)
                VALUES (sug_user_id, sug_friend_id, NOW(), 'FRIEND');

WHEN 'BLOCK' THEN
                INSERT INTO relationships (user_id, friend_id, created_at, relation)
                VALUES (sug_user_id, sug_friend_id, NOW(), 'BLOCK');

WHEN 'NONE' THEN
                -- 50% chance to insert a 'PENDING' relationship
                IF RAND() < 0.5 THEN
                    INSERT INTO relationships (user_id, friend_id, created_at, relation)
                    VALUES (sug_user_id, sug_friend_id, NOW(), 'PENDING');
END IF;
END CASE;

END LOOP;

CLOSE cur_suggestions;
END//

DELIMITER ;

-- Gọi thủ tục để tạo dữ liệu
CALL populate_relationships_from_suggestions();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE populate_relationships_from_suggestions;

DELIMITER //

CREATE PROCEDURE update_mutual_friends_and_suggest_points()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE sug_id BIGINT;
    DECLARE sug_user_id BIGINT;
    DECLARE sug_friend_id BIGINT;
    DECLARE mutual_count INT DEFAULT 0;
    DECLARE new_suggest_point INT;

    -- Cursor for looping through suggestions
    DECLARE cur_suggestions CURSOR FOR
SELECT suggestion_id, user_id, friend_id, suggest_point
FROM suggestions;

-- Continue handler for cursor
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    -- Open cursor
OPEN cur_suggestions;

read_loop: LOOP
        FETCH cur_suggestions INTO sug_id, sug_user_id, sug_friend_id, new_suggest_point;
        IF done THEN
            LEAVE read_loop;
END IF;

        -- Calculate mutual friends between sug_user_id and sug_friend_id
SELECT COUNT(*)
INTO mutual_count
FROM relationships r1
         JOIN relationships r2 ON r1.friend_id = r2.friend_id
WHERE r1.user_id = sug_user_id
  AND r2.user_id = sug_friend_id
  AND r1.relation = 'FRIEND'
  AND r2.relation = 'FRIEND';

-- Update suggest_point based on number of mutual friends
CASE
            WHEN mutual_count BETWEEN 1 AND 10 THEN
                SET new_suggest_point = new_suggest_point + 10;
WHEN mutual_count BETWEEN 11 AND 20 THEN
                SET new_suggest_point = new_suggest_point + 20;
WHEN mutual_count > 20 THEN
                SET new_suggest_point = new_suggest_point + 30;
ELSE
                -- Không thay đổi điểm nếu mutual_count = 0 hoặc không thuộc các trường hợp trên
                SET new_suggest_point = new_suggest_point;
END CASE;

        -- Update suggestions table with mutual friends count and new suggest_point
UPDATE suggestions
SET mutual_friends = mutual_count,
    suggest_point = new_suggest_point
WHERE suggestion_id = sug_id;

END LOOP;

    -- Close cursor
CLOSE cur_suggestions;

END//

DELIMITER ;

-- Gọi thủ tục để tạo dữ liệu
CALL update_mutual_friends_and_suggest_points();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE update_mutual_friends_and_suggest_points;

DELIMITER //
CREATE PROCEDURE generate_close_relationships()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE rel_user_id BIGINT;
    DECLARE rel_friend_id BIGINT;
    DECLARE rel_created_at DATETIME;
    DECLARE rel_cursor CURSOR FOR
SELECT user_id, friend_id, created_at FROM relationships WHERE relation = 'FRIEND';
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

OPEN rel_cursor;

rel_loop: LOOP
        FETCH rel_cursor INTO rel_user_id, rel_friend_id, rel_created_at;
        IF done THEN
            LEAVE rel_loop;
END IF;

        -- 50% chance to create a close relationship
        IF RAND() < 0.5 THEN
            INSERT INTO close_relationships (user_id, target_user_id, close_relationship_name, created_at)
            VALUES (
                rel_user_id,
                rel_friend_id,
                ELT(FLOOR(1 + RAND() * 5), 'FATHER', 'MOTHER', 'BROTHER', 'SISTER', 'DATING'),
                rel_created_at
            );
END IF;
END LOOP;

CLOSE rel_cursor;
END //
DELIMITER ;

-- Gọi thủ tục để tạo dữ liệu
CALL generate_close_relationships();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE generate_close_relationships;