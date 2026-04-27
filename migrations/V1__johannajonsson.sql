CREATE TABLE products (
    product_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    external_id BIGINT,
    title VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    description TEXT,
    image TEXT,
    stock INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);