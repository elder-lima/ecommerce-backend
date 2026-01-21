-- 1. Removendo a FK de auth_user
ALTER TABLE auth_user DROP CONSTRAINT IF EXISTS auth_user_role_fk_fkey;

-- 2. Remove a coluna errada
ALTER TABLE auth_user DROP COLUMN IF EXISTS role_fk;

CREATE TABLE user_roles (
    auth_user_fk UUID NOT NULL,
    role_fk UUID NOT NULL,
    PRIMARY KEY (auth_user_fk, role_fk),
    FOREIGN KEY(auth_user_fk) REFERENCES auth_user(auth_user_id),
    FOREIGN KEY(role_fk) REFERENCES roles(role_id)
)