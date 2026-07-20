CREATE TABLE user_links (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    position INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields from BaseEntity
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    creator_id BIGINT,
    modifier_id BIGINT,

    -- Foreign key mapping back to the Users table
    CONSTRAINT fk_user_link_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Crucial for performance: Speeds up querying links for a specific profile
CREATE INDEX idx_user_links_user_id ON user_links(user_id);