INSERT INTO auth_user (auth_user_id, email, senha)
VALUES (
  gen_random_uuid(),
  'admin@admin.com',
  '$2a$10$zKX4jqC6e.cLG/TC.n9tXuRhaZQWzsZtF7fMpVokZ/8GMjXySSTmi'
);

INSERT INTO user_roles (auth_user_fk, role_fk)
SELECT
  u.auth_user_id,
  r.role_id
FROM auth_user u
JOIN roles r ON r.nome = 'ROLE_ADMIN'
WHERE u.email = 'admin@admin.com'
ON CONFLICT DO NOTHING;