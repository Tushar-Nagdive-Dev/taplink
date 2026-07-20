CREATE TABLE qr_barcode_configs (
    user_links_id BIGINT NOT NULL,
    code_type VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    foreground_color VARCHAR(7) NOT NULL DEFAULT '#000000',
    background_color VARCHAR(7) NOT NULL DEFAULT '#FFFFFF',
    size_pixels INT DEFAULT 300,
    margin_size INT DEFAULT 1,
    include_logo BOOLEAN NOT NULL DEFAULT FALSE,
    logo_url VARCHAR(512),
    logo_scale DOUBLE PRECISION,
    error_correction_level VARCHAR(10) DEFAULT 'MEDIUM',

    created_by VARCHAR(50),
    modified_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    creator_id BIGINT,
    modifier_id BIGINT,

    PRIMARY KEY (user_links_id),
    CONSTRAINT fk_qr_barcode_user_links FOREIGN KEY (user_links_id) REFERENCES user_links (id) ON DELETE CASCADE,
    CONSTRAINT fk_qr_barcode_creator FOREIGN KEY (creator_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_qr_barcode_modifier FOREIGN KEY (modifier_id) REFERENCES users (id) ON DELETE SET NULL
);