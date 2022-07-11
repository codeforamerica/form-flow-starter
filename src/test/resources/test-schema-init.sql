CREATE DOMAIN IF NOT EXISTS "JSONB" AS text;

-- CREATE SEQUENCE IF NOT EXISTS submissions_id START WITH 1 MAXVALUE 9999999;

CREATE TABLE IF NOT EXISTS submissions
(
    id SERIAL PRIMARY KEY,
    flow VARCHAR NOT NULL,
    input_data JSONB NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    submitted_at TIMESTAMP WITHOUT TIME ZONE
);