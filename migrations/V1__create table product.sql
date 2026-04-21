CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price INTEGER,
    description TEXT,
    image TEXT,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);