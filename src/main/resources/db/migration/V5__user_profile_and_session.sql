-- ==============================================================================
-- 1. Create User Profiles Table (One-to-One with Users)
-- ==============================================================================
CREATE TABLE user_profile (
    -- Primary Key acts as both the ID and the Foreign Key to the users table
    user_id BIGINT NOT NULL,

    -- Profile Specific Fields
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    profile_picture_url VARCHAR(255),
    bio VARCHAR(250),
    location VARCHAR(100),
    timezone VARCHAR(50),
    auth_provider VARCHAR(20),

    -- BaseEntity Audit Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    creator_id BIGINT,
    modifier_id BIGINT,

    -- Constraints
    CONSTRAINT pk_user_profiles PRIMARY KEY (user_id),
    CONSTRAINT fk_profiles_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_profiles_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT fk_profiles_modifier FOREIGN KEY (modifier_id) REFERENCES users(id)
);

-- ==============================================================================
-- 2. Create User Sessions Table (One-to-Many with Users)
-- ==============================================================================
CREATE TABLE user_session (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,

    -- Session Specific Fields
    os_type VARCHAR(50),
    browser VARCHAR(50),
    ip_address VARCHAR(45), -- 45 characters supports full IPv6 formatting
    last_active DATETIME,
    is_active BOOLEAN DEFAULT TRUE,

    -- BaseEntity Audit Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    creator_id BIGINT,
    modifier_id BIGINT,

    -- Constraints
    CONSTRAINT pk_user_sessions PRIMARY KEY (id),
    CONSTRAINT fk_sessions_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_sessions_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT fk_sessions_modifier FOREIGN KEY (modifier_id) REFERENCES users(id)
);