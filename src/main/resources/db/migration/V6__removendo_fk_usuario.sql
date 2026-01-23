-- Remove a constraint antiga
ALTER TABLE usuario
DROP CONSTRAINT IF EXISTS auth_user_fk;

-- Remove a coluna antiga
ALTER TABLE usuario
DROP COLUMN IF EXISTS auth_user_fk;

-- Garante que user_id é NOT NULL (caso antigo)
ALTER TABLE usuario
ALTER COLUMN user_id SET NOT NULL;

-- Cria a FK correta (FK compartilhada)
ALTER TABLE usuario
ADD CONSTRAINT fk_usuario_auth_user
FOREIGN KEY (user_id)
REFERENCES auth_user (auth_user_id)
ON DELETE CASCADE;