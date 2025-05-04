USE socialnetwork;
-- Add indexes to relationships table
CREATE INDEX idx_relationships_user_id ON relationships(user_id);
CREATE INDEX idx_relationships_friend_id ON relationships(friend_id);
CREATE INDEX idx_relationships_user_friend ON relationships(user_id, friend_id);

-- Add indexes to close_relationships table
CREATE INDEX idx_close_relationships_user_id ON close_relationships(user_id);
CREATE INDEX idx_close_relationships_target_user_id ON close_relationships(target_user_id);

-- Add index to posts table
CREATE INDEX idx_posts_user_id ON posts(user_id);

-- Add indexes to comments table
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_comments_parent_comment_id ON comments(parent_comment_id);

-- Add index to post_reactions table
CREATE INDEX idx_post_reactions_post_id ON post_reactions(post_id);

-- Add indexes to comment_reactions table
CREATE INDEX idx_comment_reactions_comment_id ON comment_reactions(comment_id);
CREATE INDEX idx_comment_reactions_user_id ON comment_reactions(user_id);

-- Add index to problematic_comments table
CREATE INDEX idx_problematic_comments_user_id ON problematic_comments(user_id);

