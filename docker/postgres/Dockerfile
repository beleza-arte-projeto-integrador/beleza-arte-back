# Imagem do banco LOCAL de testes do Beleza & Arte.
# PostgreSQL 17 (a mesma versão principal do Supabase) já com as tabelas e os dados de teste.
#
# Os scripts em init/ rodam em ordem alfabética, só na PRIMEIRA vez que o
# container sobe com o volume vazio (regra da imagem oficial do Postgres).
FROM postgres:17-alpine

# Horário de Brasília nos logs e no now() da sessão (o salão fica no Brasil).
ENV TZ=America/Sao_Paulo \
    PGTZ=America/Sao_Paulo

COPY init/ /docker-entrypoint-initdb.d/
