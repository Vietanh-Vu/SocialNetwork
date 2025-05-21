DELIMITER //

CREATE PROCEDURE populate_relationships_from_suggestions()
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE sug_user_id BIGINT;
    DECLARE sug_friend_id BIGINT;
    DECLARE sug_status ENUM('BLOCK', 'FRIEND', 'NONE');

    DECLARE cur_suggestions CURSOR FOR
SELECT user_id, friend_id, status FROM suggestions;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN cur_suggestions;

read_loop: LOOP
        FETCH cur_suggestions INTO sug_user_id, sug_friend_id, sug_status;
        IF done THEN
            LEAVE read_loop;
END IF;

CASE sug_status
            WHEN 'FRIEND' THEN
                INSERT INTO relationships (user_id, friend_id, created_at, relation)
                VALUES (sug_user_id, sug_friend_id, NOW(), 'FRIEND');
WHEN 'BLOCK' THEN
                INSERT INTO relationships (user_id, friend_id, created_at, relation)
                VALUES (sug_user_id, sug_friend_id, NOW(), 'BLOCK');
WHEN 'NONE' THEN
                IF RAND() < 0.5 THEN
                    INSERT INTO relationships (user_id, friend_id, created_at, relation)
                    VALUES (sug_user_id, sug_friend_id, NOW(), 'PENDING');
END IF;
END CASE;
END LOOP;

CLOSE cur_suggestions;
END//

DELIMITER ;

CALL populate_relationships_from_suggestions();
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

    DECLARE cur_suggestions CURSOR FOR
SELECT suggestion_id, user_id, friend_id, suggest_point FROM suggestions;

DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

OPEN cur_suggestions;

read_loop: LOOP
        FETCH cur_suggestions INTO sug_id, sug_user_id, sug_friend_id, new_suggest_point;
        IF done THEN
            LEAVE read_loop;
END IF;

SELECT COUNT(*) INTO mutual_count
FROM relationships r1
         JOIN relationships r2 ON r1.friend_id = r2.friend_id
WHERE r1.user_id = sug_user_id
  AND r2.user_id = sug_friend_id
  AND r1.relation = 'FRIEND'
  AND r2.relation = 'FRIEND';

CASE
    WHEN mutual_count BETWEEN 1 AND 10 THEN
        SET new_suggest_point = new_suggest_point + 10;
WHEN mutual_count BETWEEN 11 AND 20 THEN
        SET new_suggest_point = new_suggest_point + 20;
WHEN mutual_count > 20 THEN
        SET new_suggest_point = new_suggest_point + 30;
ELSE
        -- mutual_count = 0, không thay đổi suggest_point
        SET new_suggest_point = new_suggest_point;
END CASE;


UPDATE suggestions
SET suggest_point = new_suggest_point
WHERE suggestion_id = sug_id;
END LOOP;

CLOSE cur_suggestions;
END//

DELIMITER ;

CALL update_mutual_friends_and_suggest_points();
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
END//

DELIMITER ;

CALL generate_close_relationships();
DROP PROCEDURE generate_close_relationships;