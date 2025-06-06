USE
socialnetwork;

CREATE TABLE roles
(
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255)
);

CREATE TABLE users
(
    user_id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    username          VARCHAR(255),
    email             VARCHAR(255),
    password          VARCHAR(255),
    first_name        VARCHAR(255),
    last_name         VARCHAR(255),
    gender            ENUM('MALE','FEMALE','OTHERS'),
    visibility        ENUM('PUBLIC', 'FRIEND', 'PRIVATE'),
    role_id           BIGINT,
    bio               VARCHAR(255),
    location          VARCHAR(255),
    work              VARCHAR(255),
    education         VARCHAR(255),
    created_at        DATETIME,
    updated_at        DATETIME,
    avatar            VARCHAR(255),
    background_image  VARCHAR(255),
    date_of_birth     DATE,
    is_email_verified BOOLEAN default false,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

CREATE TABLE relationships
(
    relationship_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id         BIGINT,
    friend_id       BIGINT,
    created_at      DATETIME,
    relation        ENUM('FRIEND', 'PENDING', 'BLOCK')
--                          CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id),
--                          CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE close_relationships
(
    close_relationship_id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id                 BIGINT,
    target_user_id          BIGINT,
    close_relationship_name ENUM('FATHER', 'MOTHER', 'BROTHER', 'SISTER', 'DATING'),
    created_at              DATETIME
);

CREATE TABLE posts
(
    post_id     BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id     BIGINT,
    content     TEXT,
    visibility  ENUM('PUBLIC', 'FRIEND', 'PRIVATE'),
    created_at  DATETIME,
    updated_at  DATETIME,
    photo_lists MEDIUMTEXT,
    CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE comments
(
    comment_id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id           BIGINT,
    post_id           BIGINT,
    parent_comment_id BIGINT,
    content           TEXT,
    created_at        DATETIME,
    updated_at        DATETIME,
--                           is_hidden BIT(1),
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

CREATE TABLE post_reactions
(
    post_reaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT,
    post_id          BIGINT,
--     reaction_type    ENUM('LIKE', 'WOW', 'LOVE', 'SAD','ANGRY'),
    created_at       DATETIME,
    CONSTRAINT fk_post_reaction_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_post_reaction_post FOREIGN KEY (post_id) REFERENCES posts (post_id)
);

CREATE TABLE comment_reactions
(
    comment_reaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id             BIGINT,
    comment_id          BIGINT,
--     reaction_type       ENUM('LIKE', 'WOW', 'LOVE', 'SAD','ANGRY'),
    created_at          DATETIME,
    CONSTRAINT fk_comment_reaction_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_reaction_comment FOREIGN KEY (comment_id) REFERENCES comments (comment_id) ON DELETE CASCADE
);

CREATE TABLE problematic_comments
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id    BIGINT,
    content    TEXT,
    spam_probability DOUBLE,
    created_at DATETIME,
    CONSTRAINT fk_problematic_comments_users FOREIGN KEY (user_id) REFERENCES users (user_id)
);

ALTER TABLE comments
    ADD COLUMN image VARCHAR(255);