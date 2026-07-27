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

    -- Primary Key
    id BIGINT NOT NULL AUTO_INCREMENT,

    -- Public Session Identifier (stored in JWT)
    session_id BINARY(16) NOT NULL,

    -- User
    user_id BIGINT NOT NULL,

    -- Session Lifecycle
    login_at DATETIME NOT NULL,
    last_active DATETIME,
    logout_at DATETIME,
    expires_at DATETIME NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Client Information
    os_type VARCHAR(50),
    browser VARCHAR(100),
    ip_address VARCHAR(45), -- Supports IPv4 & IPv6

    -- BaseEntity Audit Fields
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    creator_id BIGINT,
    modifier_id BIGINT,

    -- Constraints
    CONSTRAINT pk_user_session PRIMARY KEY (id),
    CONSTRAINT uk_user_session_session_id UNIQUE (session_id),
    CONSTRAINT fk_user_session_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_session_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT fk_user_session_modifier FOREIGN KEY (modifier_id) REFERENCES users(id)
);