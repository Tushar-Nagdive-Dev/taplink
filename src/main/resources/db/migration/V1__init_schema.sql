-- 1. Create Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    creator_id BIGINT,
    modifier_id BIGINT
);

-- 2. Create Roles Table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),

    -- Audit fields
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    creator_id BIGINT,
    modifier_id BIGINT
);

-- 3. Create User_Roles Junction Table
CREATE TABLE user_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit fields (Replaces 'assigned_at')
    created_at DATETIME NOT NULL,
    modified_at DATETIME,
    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    creator_id BIGINT,
    modifier_id BIGINT,

    CONSTRAINT uk_user_role UNIQUE (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- ==========================================
-- DATA SEEDING (Must happen in strict order)
-- ==========================================

-- 4. Create the System Roles FIRST (This was missing!)
INSERT INTO roles (name, description, created_at, created_by)
VALUES
    ('ROLE_USER', 'Standard registered user', NOW(), 'SYSTEM'),
    ('ROLE_ADMIN', 'System administrator', NOW(), 'SYSTEM');

-- 5. Insert the default Admin User SECOND
-- The password hash below represents the plain-text password: password
INSERT INTO users (username, email, password_hash, enabled, created_at, created_by)
VALUES (
    'admin',
    'admin@taplink.co',
    '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6',
    true,
    NOW(),
    'SYSTEM'
);

-- 6. Map the Admin User to the Roles LAST
INSERT INTO user_roles (user_id, role_id, active, created_at, created_by)
VALUES
(
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
    true,
    NOW(),
    'SYSTEM'
),
(
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM roles WHERE name = 'ROLE_USER'),
    true,
    NOW(),
    'SYSTEM'
);