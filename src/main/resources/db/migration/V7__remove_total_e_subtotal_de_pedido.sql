-- Remove o campo total da tabela pedido
ALTER TABLE pedido
DROP COLUMN IF EXISTS total;

-- Remove o campo subtotal da tabela pedido_item
ALTER TABLE pedido_item
DROP COLUMN IF EXISTS subtotal;