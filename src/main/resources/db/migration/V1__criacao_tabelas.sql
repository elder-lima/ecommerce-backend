CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE roles (
    role_id UUID PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE auth_user (
    auth_user_id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role_fk UUID NOT NULL,
    FOREIGN KEY (role_fk) REFERENCES roles (role_id)
);

CREATE TABLE usuario (
    user_id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15),
    cpf CHAR(11) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES auth_user (auth_user_id)
);

CREATE TABLE produto (
    produto_id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    imagem VARCHAR(255)
);

CREATE TABLE categoria (
    categoria_id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE produto_categoria (
    produto_fk UUID NOT NULL,
    categoria_fk UUID NOT NULL,
    PRIMARY KEY (produto_fk, categoria_fk),
    FOREIGN KEY (produto_fk) REFERENCES produto (produto_id),
    FOREIGN KEY (categoria_fk) REFERENCES categoria (categoria_id)
);

CREATE TABLE pedido (
    pedido_id UUID PRIMARY KEY,
    criacao TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    usuario_fk UUID NOT NULL,
    FOREIGN KEY (usuario_fk) REFERENCES usuario (user_id)
);

CREATE TABLE pedido_item (
    pedido_fk UUID NOT NULL,
    produto_fk UUID NOT NULL,
    quantidade INTEGER NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (pedido_fk, produto_fk),
    FOREIGN KEY (pedido_fk) REFERENCES pedido (pedido_id),
    FOREIGN KEY (produto_fk) REFERENCES produto (produto_id)
);

CREATE TABLE pagamento (
    pagamento_id UUID PRIMARY KEY,
    momento TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (pagamento_id) REFERENCES pedido (pedido_id)
);
