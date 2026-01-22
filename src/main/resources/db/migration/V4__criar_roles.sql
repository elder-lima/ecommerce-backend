-- Ativa a extensão pgcrypto no PostgreSQL
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO roles (role_id, nome)
VALUES
  (gen_random_uuid(), 'ROLE_ADMIN'),
  (gen_random_uuid(), 'ROLE_BASIC');