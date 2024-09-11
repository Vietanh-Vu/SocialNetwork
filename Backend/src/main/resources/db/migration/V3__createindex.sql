USE socialnetwork;
-- Tạo chỉ mục cho cột user_id trong bảng post_reactions
CREATE INDEX idx_post_user_id ON posts(user_id);

-- Tạo chỉ mục cho cột post_id trong bảng comments
CREATE INDEX idx_comments_post_id ON comments(post_id);

-- Tạo chỉ mục cho cột post_id trong bảng post_reactions
CREATE INDEX idx_post_reactions_post_id ON post_reactions(post_id);
-- Tạo chỉ mục cho cột parent_comment_id trong bảng comments
CREATE INDEX idx_parent_comment_id ON comments(parent_comment_id);


-- Tạo chỉ mục cho cột comment_id trong bảng comment_reactions
CREATE INDEX idx_comment_reactions_comment_id ON comment_reactions(comment_id);
