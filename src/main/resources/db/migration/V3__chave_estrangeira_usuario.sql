-- 1. Adiciona a coluna
ALTER TABLE usuario
ADD COLUMN auth_user_fk UUID NOT NULL UNIQUE;

-- 2. Cria a constraint de chave estrangeira
ALTER TABLE usuario
ADD CONSTRAINT auth_user_fk
FOREIGN KEY (auth_user_fk)
REFERENCES auth_user (auth_user_id);