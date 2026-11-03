-- =====================================================================
-- V2 - Dados de teste
--
-- Popula todas as tabelas com dados ficticios, para que quem trabalha
-- no front-end ou nos endpoints ja encontre o banco com conteudo.
-- Os profissionais, servicos e precos sao os mesmos que aparecem no
-- front-end (tela de agendamento e profissionais padrao).
--
-- Senha de teste de TODOS os profissionais: Senha@123
-- (guardada como hash BCrypt em password_hash; usuarios = coluna username)
--
-- CPFs, telefones, e-mails e enderecos sao ficticios.
--
-- As datas dos agendamentos sao relativas ao dia em que o script roda
-- (alguns no passado, outros no futuro), para sempre haver agenda
-- "proxima" e historico. Horarios no fuso de Brasilia.
--
-- Este script so roda uma vez em cada banco. Para recomecar no banco
-- local: docker compose down -v
-- =====================================================================


-- ---------------------------------------------------------------------
-- SERVICOS (precos do front-end; duracoes estimadas)
-- O id 10 esta inativo, para testar servicos que sairam do catalogo.
-- ---------------------------------------------------------------------
INSERT INTO services (id, name, description, category, duration_minutes, price, active, created_at) VALUES
(1,  'Corte',                  'Corte de cabelo feminino ou masculino.',               'Cabelo',     45, 50.00,  TRUE,  now() - interval '90 days'),
(2,  'Mechas / Coloração',     'Mechas, luzes ou coloração completa.',                 'Cabelo',    180, 250.00, TRUE,  now() - interval '90 days'),
(3,  'Escova / Tratamentos',   'Escova modeladora e tratamentos capilares.',           'Cabelo',     60, 120.00, TRUE,  now() - interval '90 days'),
(4,  'Penteados',              'Penteados para festas e eventos.',                     'Cabelo',     60, 150.00, TRUE,  now() - interval '90 days'),
(5,  'Manicure',               'Cutilagem e esmaltação das mãos.',                     'Unhas',      45, 60.00,  TRUE,  now() - interval '90 days'),
(6,  'Limpeza de pele',        'Limpeza de pele profunda com extração.',               'Estética',   90, 150.00, TRUE,  now() - interval '90 days'),
(7,  'Depilação',              'Depilação com cera.',                                  'Estética',   60, 120.00, TRUE,  now() - interval '90 days'),
(8,  'Design de sobrancelhas', 'Design de sobrancelhas com pinça ou linha.',           'Estética',   30, 45.00,  TRUE,  now() - interval '90 days'),
(9,  'Maquiagem',              'Maquiagem social para eventos.',                       'Maquiagem',  60, 200.00, TRUE,  now() - interval '90 days'),
(10, 'Hidratação capilar',     'Serviço antigo, fora do catálogo atual.',              'Cabelo',     60, 90.00,  FALSE, now() - interval '200 days');


-- ---------------------------------------------------------------------
-- PROFISSIONAIS (os 8 do front-end + Carol Dias, cadastrada em teste
-- no Supabase). Anna e a dona do salao (ADMIN).
-- Laura esta inativa, para testar quem saiu do salao.
-- ---------------------------------------------------------------------
INSERT INTO professionals (id, name, cpf, email, phone, specialty, experience_years, username, password_hash, role, active, created_at, updated_at) VALUES
(1, 'Anna Souza',      '10000000001', 'anna@belezaearte.test',     '61990000001',  'Cabeleireira',             12, 'anna',     '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'ADMIN',        TRUE,  now() - interval '120 days', now() - interval '5 days'),
(2, 'Juliana Lima',    '10000000002', 'juliana@belezaearte.test',  '61990000002',  'Esteticista',               6, 'juliana',  '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  now() - interval '110 days', NULL),
(3, 'Camila Rocha',    '10000000003', 'camila@belezaearte.test',   '61990000003',  'Manicure',                  4, 'camila',   '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  now() - interval '100 days', NULL),
(4, 'Marina Alves',    '10000000004', 'marina@belezaearte.test',   '61990000004',  'Maquiadora',                8, 'marina',   '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  now() - interval '95 days',  now() - interval '20 days'),
(5, 'Beatriz Costa',   '10000000005', 'beatriz@belezaearte.test',  '61990000005',  'Designer de Sobrancelhas',  5, 'beatriz',  '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  now() - interval '90 days',  NULL),
(6, 'Carolina Mendes', '10000000006', 'carolina@belezaearte.test', '61990000006',  'Depiladora',                3, 'carolina', '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  now() - interval '80 days',  NULL),
(7, 'Sofia Ribeiro',   '10000000007', 'sofia@belezaearte.test',    '61990000007',  'Cabeleireira',             10, 'sofia',    '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  now() - interval '115 days', NULL),
(8, 'Laura Martins',   '10000000008', 'laura@belezaearte.test',    '61990000008',  'Maquiadora',                2, 'laura',    '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', FALSE, now() - interval '150 days', now() - interval '30 days'),
(9, 'Carol Dias',      '10000000009', 'c@gmail.com',               '122222222222', 'Manicure',                  1, 'carol',    '$2a$10$JEga2XxWPHvHIJNCZ5n23e2fm6DH8vtG7hmEfXG7ZHyv7uA0g5iW2', 'PROFESSIONAL', TRUE,  '2026-11-03 02:48:32.198534+00', NULL);


-- ---------------------------------------------------------------------
-- SERVICOS DE CADA PROFISSIONAL (mesma relacao do front-end)
-- ---------------------------------------------------------------------
INSERT INTO professional_services (professional_id, service_id) VALUES
(1, 1), (1, 2), (1, 3),           -- Anna: corte, mechas, escova
(2, 6), (2, 7),                   -- Juliana: limpeza de pele, depilacao
(3, 5),                           -- Camila: manicure
(4, 9), (4, 4),                   -- Marina: maquiagem, penteados
(5, 8), (5, 7),                   -- Beatriz: sobrancelhas, depilacao
(6, 7),                           -- Carolina: depilacao
(7, 1), (7, 2), (7, 3), (7, 4),   -- Sofia: corte, mechas, escova, penteados
(8, 9), (8, 4),                   -- Laura: maquiagem, penteados
(9, 5);                           -- Carol: manicure


-- ---------------------------------------------------------------------
-- HORARIO DE TRABALHO
-- Profissionais 1 a 8: terca a sabado, 09:00-12:00 e 13:00-18:00
-- Camila tambem atende na segunda; Carol so aos sabados.
-- (weekday: 0 = domingo ... 6 = sabado)
-- ---------------------------------------------------------------------
INSERT INTO professional_availability (professional_id, weekday, start_time, end_time)
SELECT p.id, d.dia, f.inicio, f.fim
FROM generate_series(1, 8) AS p(id)
CROSS JOIN generate_series(2, 6) AS d(dia)
CROSS JOIN (VALUES (TIME '09:00', TIME '12:00'), (TIME '13:00', TIME '18:00')) AS f(inicio, fim);

INSERT INTO professional_availability (professional_id, weekday, start_time, end_time) VALUES
(3, 1, '09:00', '17:00'),
(9, 6, '08:00', '14:00');


-- ---------------------------------------------------------------------
-- RECURSOS DO SALAO (a Cadeira 4 esta em manutencao)
-- ---------------------------------------------------------------------
INSERT INTO resources (id, name, type, active) VALUES
(1, 'Cadeira 1',           'CADEIRA',     TRUE),
(2, 'Cadeira 2',           'CADEIRA',     TRUE),
(3, 'Cadeira 3',           'CADEIRA',     TRUE),
(4, 'Cabine de estética',  'CABINE',      TRUE),
(5, 'Bancada de manicure', 'EQUIPAMENTO', TRUE),
(6, 'Cadeira 4',           'CADEIRA',     FALSE);


-- ---------------------------------------------------------------------
-- CLIENTES (Camila Rodrigues e Maria das Gracas lembram as personas
-- do relatorio: uma com pressa e muito digital, outra com pouca
-- familiaridade com tecnologia)
-- ---------------------------------------------------------------------
INSERT INTO clients (id, name, phone, email, address, birth_date, preferences, created_at, updated_at) VALUES
(1, 'Camila Rodrigues',  '61981110001', 'camila.rodrigues@email.test', 'SQN 210 Bloco A, Asa Norte, Brasília - DF',    '1994-03-15', 'Prefere horários após as 18h ou aos sábados.',                                  now() - interval '60 days', now() - interval '10 days'),
(2, 'Maria das Graças',  '61981110002', 'maria.gracas@email.test',     'QNL 12 Casa 8, Taguatinga - DF',               '1956-08-02', 'Pouca familiaridade com celular: confirmar por ligação. Alergia a amônia.',     now() - interval '50 days', NULL),
(3, 'Fernanda Oliveira', '61981110003', 'fernanda.oliveira@email.test','Rua 7, Lote 15, Águas Claras - DF',            '1990-11-23', 'Cabelo cacheado; não fazer escova progressiva.',                                now() - interval '40 days', NULL),
(4, 'Patrícia Gomes',    '61981110004', 'patricia.gomes@email.test',   'SHCES 1203 Bloco C, Cruzeiro - DF',            '1987-06-30', 'Pele sensível; avisar antes de usar produtos novos.',                           now() - interval '30 days', NULL),
(5, 'Ricardo Pereira',   '61981110005', 'ricardo.pereira@email.test',  'Quadra 104 Conjunto 3, Recanto das Emas - DF', '1982-01-09', 'Só corte, máquina 2 nas laterais.',                                             now() - interval '20 days', NULL),
(6, 'Juliana Freitas',   '61981110006', 'juliana.freitas@email.test',  'SQS 308 Bloco F, Asa Sul, Brasília - DF',      '1996-09-12', 'Noiva: casamento em dezembro, fazer prova de maquiagem e penteado.',            now() - interval '15 days', now() - interval '3 days');


-- ---------------------------------------------------------------------
-- AGENDAMENTOS (um de cada status)
-- ---------------------------------------------------------------------
INSERT INTO appointments (id, client_id, status, notes, cancelled_by, cancel_reason, cancelled_at, created_at, updated_at) VALUES
(1, 1, 'CONCLUIDO',      'Retoque de mechas.',                 NULL,      NULL,                                NULL,                      now() - interval '10 days', now() - interval '7 days'),
(2, 2, 'CONCLUIDO',      'Cliente pediu confirmação por ligação.', NULL,  NULL,                                NULL,                      now() - interval '5 days',  now() - interval '3 days'),
(3, 3, 'CANCELADO',      NULL,                                 'CLIENTE', 'Imprevisto no trabalho.',           now() - interval '2 days', now() - interval '6 days',  now() - interval '2 days'),
(4, 4, 'NAO_COMPARECEU', NULL,                                 NULL,      NULL,                                NULL,                      now() - interval '4 days',  now() - interval '1 day'),
(5, 6, 'CONFIRMADO',     'Prova de maquiagem e penteado para o casamento.', NULL, NULL,                        NULL,                      now() - interval '3 days',  now() - interval '2 days'),
(6, 5, 'AGENDADO',       NULL,                                 NULL,      NULL,                                NULL,                      now() - interval '1 day',   NULL),
(7, 1, 'AGENDADO',       'Agendado pelo celular.',             NULL,      NULL,                                NULL,                      now(),                      NULL),
(8, 2, 'CANCELADO',      NULL,                                 'SALAO',   'Profissional com atestado médico.', now() - interval '1 day',  now() - interval '4 days',  now() - interval '1 day');


-- ---------------------------------------------------------------------
-- ITENS DOS AGENDAMENTOS
-- Horario = (hoje + N dias) as HH:MM, no fuso de Brasilia.
-- end_at = start_at + duracao do servico. price = preco do servico.
-- ---------------------------------------------------------------------
INSERT INTO appointment_items (appointment_id, service_id, professional_id, resource_id, start_at, end_at, price)
SELECT i.appointment_id, i.service_id, i.professional_id, i.resource_id,
       ((current_date + i.dias) + i.hora) AT TIME ZONE 'America/Sao_Paulo',
       ((current_date + i.dias) + i.hora) AT TIME ZONE 'America/Sao_Paulo' + make_interval(mins => s.duration_minutes),
       s.price
FROM (VALUES
    -- agendamento, servico, profissional, recurso, dias a partir de hoje, hora
    (1, 2, 1, 1,    -7, TIME '14:00'),   -- Mechas com Anna (concluido)
    (1, 5, 3, 5,    -7, TIME '17:00'),   -- Manicure com Camila, logo depois (concluido)
    (2, 3, 7, 2,    -3, TIME '10:00'),   -- Escova com Sofia (concluido)
    (3, 1, 7, 2,    -1, TIME '15:00'),   -- Corte com Sofia (cancelado pela cliente)
    (4, 6, 2, 4,    -1, TIME '10:00'),   -- Limpeza de pele com Juliana (cliente faltou)
    (5, 9, 4, 3,     1, TIME '09:00'),   -- Maquiagem com Marina (confirmado)
    (5, 4, 4, 3,     1, TIME '10:00'),   -- Penteado com Marina, logo depois (confirmado)
    (6, 1, 1, 1,     3, TIME '09:00'),   -- Corte com Anna (agendado)
    (6, 8, 5, NULL,  3, TIME '10:00'),   -- Sobrancelhas com Beatriz, sem recurso (agendado)
    (7, 7, 6, 4,     2, TIME '16:00'),   -- Depilacao com Carolina (agendado)
    (8, 3, 7, 2,     2, TIME '10:00')    -- Escova com Sofia (cancelado pelo salao)
) AS i(appointment_id, service_id, professional_id, resource_id, dias, hora)
JOIN services s ON s.id = i.service_id;


-- ---------------------------------------------------------------------
-- PAGAMENTOS (todas as formas e todos os status)
-- ---------------------------------------------------------------------
INSERT INTO payments (appointment_id, amount, method, status, paid_at, created_at) VALUES
(1, 310.00, 'PIX',            'PAGO',      now() - interval '7 days', now() - interval '7 days'),   -- mechas + manicure
(2, 120.00, 'DINHEIRO',       'PAGO',      now() - interval '3 days', now() - interval '3 days'),
(3,  20.00, 'PIX',            'ESTORNADO', now() - interval '6 days', now() - interval '6 days'),   -- sinal devolvido
(5, 100.00, 'PIX',            'PAGO',      now() - interval '3 days', now() - interval '3 days'),   -- sinal da noiva
(5, 250.00, 'CARTAO_CREDITO', 'PENDENTE',  NULL,                      now() - interval '3 days'),   -- restante, no dia
(6,  95.00, 'CARTAO_DEBITO',  'PENDENTE',  NULL,                      now() - interval '1 day');


-- ---------------------------------------------------------------------
-- Ajusta os contadores de id: como inserimos ids fixos acima, o proximo
-- cadastro feito pela API precisa comecar depois do maior id existente.
-- ---------------------------------------------------------------------
SELECT setval(pg_get_serial_sequence('services',      'id'), (SELECT max(id) FROM services));
SELECT setval(pg_get_serial_sequence('professionals', 'id'), (SELECT max(id) FROM professionals));
SELECT setval(pg_get_serial_sequence('resources',     'id'), (SELECT max(id) FROM resources));
SELECT setval(pg_get_serial_sequence('clients',       'id'), (SELECT max(id) FROM clients));
SELECT setval(pg_get_serial_sequence('appointments',  'id'), (SELECT max(id) FROM appointments));
