-- Kiểm tra và thêm role ADMIN nếu chưa tồn tại
INSERT INTO roles (role_id, name)
SELECT 2,
       'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

-- Tạo người dùng admin mới
INSERT INTO users (username,
                   email,
                   password,
                   first_name,
                   last_name,
                   gender,
                   visibility,
                   role_id,
                   bio,
                   location,
                   work,
                   education,
                   created_at,
                   updated_at,
                   avatar,
                   background_image,
                   date_of_birth,
                   is_email_verified)
VALUES ('admin', -- username
        'admin@gmail.com', -- email
        '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', -- password: 123456 (đã hash)
        'System', -- first_name
        'Admin', -- last_name
        'MALE', -- gender
        'PRIVATE', -- visibility
        2, -- role_id (2 là ADMIN)
        'System Administrator', -- bio
        'System', -- location
        'System', -- work
        'System', -- education
        NOW(), -- created_at
        NOW(), -- updated_at
        NULL, -- avatar
        NULL, -- background_image
        '2000-01-01', -- date_of_birth
        TRUE -- is_email_verified
       );