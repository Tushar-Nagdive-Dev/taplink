-- ==========================================
-- 1. Link Routing (1:1 with user_links)
-- ==========================================
CREATE TABLE link_routing (
    user_links_id BIGINT NOT NULL,
    short_code VARCHAR(50),
    custom_slug VARCHAR(100),
    expires_at TIMESTAMP,

    -- BaseEntity Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    creator_id BIGINT,
    modifier_id BIGINT,

    PRIMARY KEY (user_links_id),
    CONSTRAINT fk_lr_user_links FOREIGN KEY (user_links_id) REFERENCES user_links(id) ON DELETE CASCADE,
    CONSTRAINT uk_lr_short_code UNIQUE (short_code),
    CONSTRAINT uk_lr_custom_slug UNIQUE (custom_slug),

    -- Audit Foreign Keys
    CONSTRAINT fk_lr_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_lr_modifier FOREIGN KEY (modifier_id) REFERENCES users(id) ON DELETE SET NULL
);
CREATE INDEX idx_lr_short_code ON link_routing(short_code);

-- ==========================================
-- 2. Link Presentation (1:1 with user_links)
-- ==========================================
CREATE TABLE link_presentation (
    user_links_id BIGINT NOT NULL,
    label VARCHAR(150),
    color_code VARCHAR(7) NOT NULL DEFAULT '#FFFFFF',
    is_favorite BOOLEAN NOT NULL DEFAULT FALSE,

    -- BaseEntity Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    creator_id BIGINT,
    modifier_id BIGINT,

    PRIMARY KEY (user_links_id),
    CONSTRAINT fk_lp_user_links FOREIGN KEY (user_links_id) REFERENCES user_links(id) ON DELETE CASCADE,

    -- Audit Foreign Keys
    CONSTRAINT fk_lp_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_lp_modifier FOREIGN KEY (modifier_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ==========================================
-- 3. Tags (Dictionary per User)
-- ==========================================
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    badge_color VARCHAR(7),
    user_id BIGINT NOT NULL,

    -- BaseEntity Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    creator_id BIGINT,
    modifier_id BIGINT,

    CONSTRAINT fk_tags_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_tags_name_per_user UNIQUE (user_id, name), -- Prevents a user from creating duplicate tag names

    -- Audit Foreign Keys
    CONSTRAINT fk_tags_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_tags_modifier FOREIGN KEY (modifier_id) REFERENCES users(id) ON DELETE SET NULL
);

-- ==========================================
-- 4. Link Tags (Many-to-Many Bridge)
-- ==========================================
CREATE TABLE link_tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_links_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,

    -- BaseEntity Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    creator_id BIGINT,
    modifier_id BIGINT,

    CONSTRAINT fk_lt_user_links FOREIGN KEY (user_links_id) REFERENCES user_links(id) ON DELETE CASCADE,
    CONSTRAINT fk_lt_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
    CONSTRAINT uk_link_tags UNIQUE (user_links_id, tag_id), -- Prevents applying the exact same tag to the same link twice

    -- Audit Foreign Keys
    CONSTRAINT fk_lt_creator FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_lt_modifier FOREIGN KEY (modifier_id) REFERENCES users(id) ON DELETE SET NULL
);