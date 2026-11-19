# Beleza & Arte — Back-end

API do sistema de agendamento para salões de beleza de pequeno e médio porte, desenvolvido no Projeto Integrador do curso de Análise e Desenvolvimento de Sistemas (SENAC).

**Tecnologias:** Java 21 · Spring Boot · Spring Data JPA · PostgreSQL (Supabase) · Lombok · Maven

**Banco de dados:** veja o [dicionário de dados](docs/banco-de-dados.md), com a explicação de cada tabela e campo.

---

## Sumário

1. [O que você precisa ter instalado](#1-o-que-você-precisa-ter-instalado)
2. [Como rodar o projeto](#2-como-rodar-o-projeto)
3. [Banco de dados](#3-banco-de-dados)
4. [Como o código está organizado](#4-como-o-código-está-organizado)
5. [Endpoints disponíveis](#5-endpoints-disponíveis)
6. [Proteção de dados (LGPD)](#6-proteção-de-dados-lgpd)
7. [Como contribuir (Git e Pull Requests)](#7-como-contribuir-git-e-pull-requests)

---

## 1. O que você precisa ter instalado

| Ferramenta | Para quê |
|---|---|
| **Java 21** (JDK) | Rodar o projeto. Recomendado: [Eclipse Temurin 21](https://adoptium.net/) |
| **Git** | Clonar o repositório e enviar suas alterações |
| Editor | VS Code com a extensão *Extension Pack for Java*, ou IntelliJ IDEA Community |

> **Não precisa instalar Maven nem Spring Boot.** O projeto tem o *Maven Wrapper* (`mvnw` / `mvnw.cmd`), que baixa tudo sozinho na primeira vez que você roda.

Para conferir o Java, abra o terminal e rode `java -version`. Deve aparecer `21`.

---

## 2. Como rodar o projeto

**1.** Clone o repositório e entre na pasta (só na primeira vez):

```bash
git clone https://github.com/beleza-arte-projeto-integrador/beleza-arte-back.git
cd beleza-arte-back
```

Se você já tem o projeto, só atualize: `git checkout main` e depois `git pull`.

**2.** Configure a senha do banco (só na primeira vez). Copie o arquivo `.env.example` para `.env`:

| Terminal | Comando |
|---|---|
| Git Bash, Linux ou Mac | `cp .env.example .env` |
| PowerShell ou CMD (Windows) | `copy .env.example .env` |

Abra o `.env` e troque os valores de exemplo (`DB_URL`, `DB_USERNAME` e `DB_PASSWORD`) pelos dados do Supabase (peça para alguém do grupo). O `.env` **não vai para o git**, então a senha não fica no repositório.

> Se preferir, você pode definir essas três como variáveis de ambiente em vez de usar o `.env`. Sem elas, a aplicação não consegue conectar no banco.

**3.** Rode a aplicação:

| Terminal | Comando |
|---|---|
| Git Bash, Linux ou Mac | `./mvnw spring-boot:run` |
| PowerShell ou CMD (Windows) | `.\mvnw.cmd spring-boot:run` |

A primeira execução demora alguns minutos (ela baixa as dependências). Nas próximas é rápida.

**4.** Quando aparecer no terminal a linha `Started SalaobelezaApplication`, a API está no ar em:

```
http://localhost:8081/v1/api/salao
```

Para testar, abra no navegador: http://localhost:8081/v1/api/salao/searchSpecialty?specialty=Cabeleireira

Para parar, aperte `Ctrl + C` no terminal.

---

## 3. Banco de dados

O banco fica no **Supabase** e é **compartilhado por todo o grupo**: o que você cadastrar ou apagar aparece para todos.

As tabelas são criadas e alteradas **direto no Supabase** (pelo site ou por um cliente como o DBeaver). A aplicação não cria nem altera tabelas, só usa as que existem.

O banco já tem **dados de teste** (profissionais, serviços, clientes e agendamentos fictícios).

### Como mudar uma tabela

1. Combine no grupo antes, porque o banco é de todos.
2. Faça a mudança no Supabase.
3. Ajuste a entidade Java correspondente (pasta `adapter/out/persistence`). Os nomes nas anotações `@Column` precisam ser **iguais** aos das colunas no banco.
4. Atualize o [dicionário de dados](docs/banco-de-dados.md).

> Se aparecer o erro `column ... does not exist`, alguma entidade está com uma coluna que não existe na tabela do Supabase.

### Banco local para testes (Docker)

Para testar sem mexer no Supabase do grupo, dá para subir um PostgreSQL no seu computador, já com as tabelas e os dados de teste. Precisa ter o **Docker** instalado.

```bash
docker compose up -d --build   # sobe o banco (porta 5433)
docker compose ps              # espere aparecer "healthy"
```

Para a aplicação usar esse banco, troque no `.env` as três linhas do banco por:

```
DB_URL=jdbc:postgresql://localhost:5433/salaobeleza
DB_USERNAME=dev
DB_PASSWORD=dev
```

Para voltar ao Supabase, restaure os valores originais. Para conectar no Beekeeper ou no DBeaver, use: host `localhost`, porta `5433`, banco `salaobeleza`, usuário `dev` e senha `dev` (sem SSL).

| Comando | O que faz |
|---|---|
| `docker compose down` | Desliga o banco e mantém os dados |
| `docker compose down -v` | Desliga e **apaga os dados**. Na próxima subida, tudo é recriado do zero |

As tabelas e os dados ficam em [docker/postgres/init/](docker/postgres/init/). Eles só rodam quando o banco é criado. Se mudar algum script, recrie com `docker compose down -v && docker compose up -d --build`. Quando uma tabela mudar no Supabase, atualize também o `01-estrutura.sql`, para os dois bancos continuarem iguais.

---

## 4. Como o código está organizado

O projeto segue a **arquitetura hexagonal** (também chamada de *ports and adapters*). A ideia é separar as regras do negócio dos detalhes técnicos (web, banco de dados), para que um possa mudar sem quebrar o outro.

```
src/main/java/com/belezaearte/salaobeleza/
├── adapter/
│   ├── in/web/controller/      → Recebem as requisições HTTP (a "porta de entrada" da API)
│   └── out/persistence/        → Entidades JPA e adapters que acessam o banco
│       └── repository/         → Interfaces do Spring Data JPA
├── application/
│   ├── domain/
│   │   ├── dto/                → Objetos que entram e saem da API (JSON)
│   │   └── service/            → Regras de negócio
│   └── port/
│       ├── in/                 → Use cases: o que a aplicação sabe fazer
│       └── out/                → O que a aplicação precisa do banco
└── exception/                  → Tratamento padronizado de erros
```

**Caminho de uma requisição:**

```
Controller  →  UseCase (port/in)  →  Service  →  Port (port/out)  →  Adapter  →  Repository  →  Banco
```

Exemplo: `ClientsController` chama `CreateClientsUseCase`, que é implementado por `ClientsService`, que usa `CreateClientsPort`, implementado por `ClientsAdapter`, que salva com `ClientsRepository`.

> **Dica:** para implementar uma parte nova, use os módulos de **Professionals** ou **Clients** como modelo.

### Situação atual dos módulos

| Módulo | Situação |
|---|---|
| Professionals | Funciona, com teste unitário |
| Clients | Funciona, com testes unitários |
| Services | Em andamento no branch `feature/fernanda` |
| Appointments | Funciona, com testes unitários |
| Payments | Funciona, com testes unitários (geração automática, listagem e resumo) |

---

## 5. Endpoints disponíveis

Todos começam com `http://localhost:8081/v1/api/salao`. Os nomes dos campos no JSON estão em **inglês** (`name`, `phone`, `email`…).

### Documentação interativa (Swagger)

Com a aplicação rodando, abra no navegador:

**http://localhost:8081/v1/api/salao/swagger-ui.html**

Como está organizado:

- **Seletor de área** (no canto superior direito, em *Select a definition*): **1. Todos os endpoints**, **2. Cadastros** (Profissionais, Clientes, Serviços e Recursos), **3. Agenda** (Agendamentos e Itens de agendamento) e **4. Financeiro** (Pagamentos).
- Dentro da área, cada **módulo é um grupo**, que começa fechado. Clique no nome para abrir.
- Em cada grupo, os endpoints aparecem na ordem **criar → alterar → remover → buscas**.
- O campo **Filter by tag** filtra os grupos pelo nome.

Cada endpoint mostra o que faz, as regras, os parâmetros e um exemplo de JSON. Para testar sem outro programa, abra um endpoint, clique em **Try it out** (os campos já vêm preenchidos com exemplos), ajuste o que quiser e clique em **Execute**.

- A documentação é **gerada automaticamente a partir dos controllers**: um endpoint novo aparece sozinho.
- Os textos (resumo, descrição, exemplos de parâmetros e de JSON, e a descrição dos campos) ficam em [`config/OpenApiEndpoints.java`](src/main/java/com/belezaearte/salaobeleza/config/OpenApiEndpoints.java), e não nos controllers. **Criou um endpoint?** Acrescente uma linha `endpoint("MÉTODO /caminho", ...)` no módulo dele. Sem isso, ele aparece no Swagger, mas sem descrição.
- Os nomes dos grupos e as respostas comuns (204 nos `DELETE`, erros 400 e 422) são configurados em [`config/OpenApiConfig.java`](src/main/java/com/belezaearte/salaobeleza/config/OpenApiConfig.java).
- O mesmo conteúdo, em formato OpenAPI (JSON), fica em `http://localhost:8081/v1/api/salao/v3/api-docs`. Dá para importar esse endereço no [Postman](https://www.postman.com/) ou no [Insomnia](https://insomnia.rest/) e ter todas as requisições prontas.
- O Swagger usa o banco configurado no `.env`. Para testar à vontade, prefira o [banco local](#3-banco-de-dados) em vez do Supabase do grupo.

As tabelas abaixo são um resumo rápido, com as regras de cada módulo.

### Professionals

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| POST | `/createProfessionals` | JSON no corpo | Cadastra um profissional |
| PUT | `/updateProfessionals` | JSON no corpo (com `id`) | Atualiza um profissional |
| DELETE | `/deleteProfessionals` | `?id=1` | Remove um profissional |
| GET | `/searchName` | `?name=Anna Souza` | Busca por nome (exato) |
| GET | `/searchEmail` | `?email=anna@belezaearte.test` | Busca por e-mail |
| GET | `/searchPhone` | `?phone=61990000001` | Busca por telefone |
| GET | `/searchSpecialty` | `?specialty=Cabeleireira` | Busca por especialidade |
| GET | `/searchCreated` | `?created=2026-11-03T10:00:00` | Busca por data de cadastro |

Exemplo de corpo para cadastrar (os campos da tela de cadastro de profissional do front-end):

```json
{
  "name": "Anna Souza",
  "cpf": "529.982.247-25",
  "phone": "(61) 99999-9999",
  "email": "anna@email.com",
  "specialty": "Cabeleireira",
  "experienceYears": 5,
  "username": "anna",
  "password": "Senha123"
}
```

- **CPF:** pode vir com ou sem máscara; no banco ficam só os 11 números. Nas respostas ele sai **mascarado** (`***.982.247-**`). Na atualização, se o CPF vier mascarado assim, o CPF atual é mantido.
- **CPF, e-mail e usuário** não podem repetir.
- **Status:** `active` começa como `true` (ativo). Para inativar, mande `"active": false` na atualização.
- **Senha:** é gravada **criptografada** (hash BCrypt) e não volta nas respostas. Na atualização, se a senha vier vazia, a senha atual é mantida.
- **Experiência:** número de anos (`experienceYears`).

### Clients

Os caminhos de Clients terminam em `Clients` porque `/searchName`, `/searchEmail` etc. já são usados por Professionals. Dois endpoints com o mesmo caminho impedem a aplicação de subir.

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| POST | `/createClients` | JSON no corpo | Cadastra um cliente (nome e telefone obrigatórios; o telefone não pode repetir) |
| PUT | `/updateClients` | JSON no corpo (com `id`) | Atualiza um cliente (a data de cadastro é mantida) |
| DELETE | `/deleteClients` | `?id=1` | Remove um cliente (não remove se ele tiver agendamentos) |
| GET | `/searchNameClients` | `?name=ana` | Busca por nome (parte do nome, sem diferenciar maiúsculas) |
| GET | `/searchPhoneClients` | `?phone=11999999999` | Busca por telefone |
| GET | `/searchEmailClients` | `?email=ana@email.com` | Busca por e-mail (sem diferenciar maiúsculas) |
| GET | `/searchCreatedClients` | `?created=2026-11-03T10:00:00` | Busca por data de cadastro |
| GET | `/exportClientsData` | `?id=1` | Devolve todos os dados guardados do cliente (LGPD) |
| PUT | `/anonymizeClients` | `?id=1` | Apaga os dados pessoais do cliente e mantém os agendamentos (LGPD) |

Exemplo de corpo para cadastrar (os mesmos campos da tela de cadastro do front-end):

```json
{
  "name": "Ana Souza",
  "phone": "11999999999",
  "email": "ana@email.com"
}
```

### Appointments

Um agendamento é a **visita** do cliente ao salão. Os serviços da visita (profissional, horário e preço) são os **itens**, cadastrados nos endpoints de `/appointmentsItems` com o `appointment_id` do agendamento. Nas respostas, cada agendamento já vem com a lista `items`.

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| POST | `/createAppointments` | JSON no corpo | Cria o agendamento (o cliente é obrigatório e precisa existir) |
| PUT | `/updateAppointments` | JSON no corpo (com `id`) | Muda o status e as observações |
| DELETE | `/deleteAppointments` | `?id=1` | Remove o agendamento e os itens dele (não remove se ele tiver pagamentos) |
| GET | `/searchClientAppointments` | `?idClient=1` | Agendamentos do cliente |
| GET | `/searchProfessionalAppointments` | `?idProfessional=1` | Agendamentos com algum serviço desse profissional |
| GET | `/searchServiceAppointments` | `?idService=1` | Agendamentos com esse serviço |
| GET | `/searchStatusAppointments` | `?status=AGENDADO` | Busca por status (sem diferenciar maiúsculas) |
| GET | `/searchNotesAppointments` | `?notes=retoque` | Busca por parte das observações (sem diferenciar maiúsculas) |
| GET | `/searchPriceAppointments` | `?price=80.00` | Agendamentos com algum serviço desse preço |
| GET | `/searchDateAppointments` | `?date=2026-11-20` | Agenda do dia: agendamentos com algum serviço nessa data (horário de Brasília) |
| GET | `/searchTimeAppointments` | `?startAt=2026-11-20T14:00:00-03:00` | Agendamentos com algum serviço começando nesse dia e horário |
| GET | `/searchCreatedAppointments` | `?created=2026-11-03T10:00:00-03:00` | Busca pela data em que o agendamento foi feito |

Exemplo de corpo para criar:

```json
{
  "idClient": 1,
  "notes": "Cliente prefere atendimento à tarde"
}
```

- **Status:** todo agendamento novo começa como `AGENDADO`, mesmo que outro status seja enviado. Depois ele só pode mudar seguindo o ciclo do [dicionário de dados](docs/banco-de-dados.md#appointments--agendamentos): `AGENDADO` → `CONFIRMADO` → `CONCLUIDO`; `AGENDADO` ou `CONFIRMADO` → `CANCELADO`; `CONFIRMADO` → `NAO_COMPARECEU`. `CONCLUIDO`, `CANCELADO` e `NAO_COMPARECEU` não mudam mais.
- **Cancelamento:** mande `"status": "CANCELADO"` com `cancelledBy` (`CLIENTE` ou `SALAO`) e, se quiser, `cancelReason`. A data do cancelamento é preenchida automaticamente. Para guardar o histórico, prefira cancelar em vez de apagar.

```json
{
  "id": 1,
  "status": "CANCELADO",
  "cancelledBy": "CLIENTE",
  "cancelReason": "Imprevisto"
}
```

- **Observações:** se `notes` não vier na atualização, as observações atuais são mantidas. Para apagar, mande `"notes": ""`.
- **Cliente:** o cliente de um agendamento não pode ser trocado.
- **Pagamento:** ao cancelar, os pagamentos `PENDENTE` do agendamento viram `CANCELADO`.

### Payments

Não há endpoint para criar pagamento: o pagamento `PENDENTE` (forma `PIX`) é **gerado automaticamente** quando serviços são adicionados a um agendamento em `/appointmentsItems`, e recalculado quando um serviço é alterado ou removido. Detalhes no [dicionário de dados](docs/banco-de-dados.md#payments--pagamentos).

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| PUT | `/payPayments` | JSON no corpo (`id` e `method`) | Registra o pagamento: `PENDENTE` → `PAGO`, com a forma de pagamento e a data/hora |
| GET | `/listPayments` | `?status=PAGO` (opcional) | Lista para a tela de pagamentos do admin, do mais recente para o mais antigo, com cliente, serviços e data do atendimento. Sem `status`, traz todos. Sem resultados, devolve `[]` |
| GET | `/searchSummaryPayments` | `?month=2026-11` (opcional, padrão: mês atual) | Valores dos cards da tela de pagamentos |
| GET | `/searchRevenuePayments` | `?period=DAY` e `&date=2026-11-18` (opcional, padrão: hoje) | Quanto foi recebido no período e a variação em relação ao período anterior |
| GET | `/searchAppointmentPayments` | `?idAppointment=1` | Pagamentos de um agendamento |
| GET | `/searchStatusPayments` | `?status=PENDENTE` | Pagamentos com esse status |

Status aceitos (sem diferenciar maiúsculas): `PENDENTE`, `PAGO`, `CANCELADO` e `ESTORNADO`.

Exemplo de item de `/listPayments`:

```json
{
  "id": 8,
  "idAppointment": 8,
  "clientName": "Maria das Graças",
  "services": "Escova / Tratamentos",
  "appointmentDate": "2026-11-20T10:00:00-03:00",
  "amount": 120.00,
  "method": "PIX",
  "status": "CANCELADO",
  "paidAt": null,
  "created": "2026-11-14T15:04:38-03:00"
}
```

Exemplo de `/searchSummaryPayments`:

```json
{
  "month": "2026-11",
  "receivedMonth": 530.00,
  "receivedPreviousMonth": 0,
  "receivedChangePercent": null,
  "pendingAmount": 370.00,
  "pendingAppointments": 2,
  "receiptRatePercent": 59
}
```

| Campo | Card da tela | Como é calculado |
|---|---|---|
| `receivedMonth` | Recebido este mês | Soma dos pagamentos `PAGO` com `paid_at` no mês |
| `receivedChangePercent` | "↑ 16% vs. mês anterior" | Variação em relação a `receivedPreviousMonth`. `null` se o mês anterior não teve recebimentos |
| `pendingAmount` / `pendingAppointments` | Pagamentos pendentes | Soma dos `PENDENTE` e quantos agendamentos têm pendência |
| `receiptRatePercent` | Taxa de recebimento | Dos pagamentos criados no mês: `PAGO` ÷ (`PAGO` + `PENDENTE`). `null` se não houver nenhum |

**`/searchRevenuePayments`** soma os pagamentos `PAGO` pela data do pagamento (`paid_at`, horário de Brasília):

| `period` | Usado em | Período | Comparado com |
|---|---|---|---|
| `DAY` | Painel: card "Receita do dia" | O dia de `date` | O dia anterior |
| `LAST_7_DAYS` | Relatórios: "Últimos 7 dias" | `date` e os 6 dias antes | Os 7 dias anteriores |
| `MONTH` | Relatórios: "Este mês" | O mês de `date` | O mês anterior |
| `YEAR` | Relatórios: "Este ano" | O ano de `date` | O ano anterior |

```json
{
  "period": "LAST_7_DAYS",
  "start": "2026-11-12",
  "end": "2026-11-18",
  "received": 220.00,
  "receivedPrevious": 310.00,
  "changePercent": -29
}
```

`changePercent` é `null` quando o período anterior não teve recebimentos.

**`/payPayments`** registra que o cliente pagou. A forma escolhida na hora substitui a do pendente (que começa como `PIX`). Só pagamentos `PENDENTE` podem ser pagos.

```json
{
  "id": 7,
  "method": "DINHEIRO"
}
```

Formas aceitas (sem diferenciar maiúsculas): `PIX`, `CARTAO_CREDITO`, `CARTAO_DEBITO` e `DINHEIRO`. Se depois um serviço for adicionado ao agendamento, um novo `PENDENTE` é gerado só com a diferença.

---

## 6. Proteção de dados (LGPD)

O sistema guarda dados pessoais de clientes (nome, telefone, e-mail) e de profissionais (nome, CPF, telefone, e-mail, usuário e senha). A Lei Geral de Proteção de Dados (Lei 13.709/2018) exige que esses dados sejam protegidos e que o dono dos dados (o *titular*) possa exercer os direitos dele. O que o back-end faz:

| O que | Como | Artigo da LGPD |
|---|---|---|
| **Senha criptografada** | A senha do profissional é gravada só como hash BCrypt (classe `PasswordConfig`). Nem quem tem acesso ao banco consegue ver a senha original | Art. 46 (segurança) |
| **CPF mascarado** | A API nunca devolve o CPF inteiro, só `***.982.247-**` | Art. 6º, III (necessidade) |
| **Senha fora das respostas** | A senha nunca volta no JSON | Art. 46 (segurança) |
| **Cópia dos dados** | `GET /exportClientsData?id=1` devolve tudo o que o salão guarda sobre o cliente e para que os dados são usados | Art. 9º e art. 18, II (acesso) |
| **Eliminação dos dados** | `DELETE /deleteClients?id=1` apaga o cliente que não tem agendamentos. Se ele tiver, `PUT /anonymizeClients?id=1` troca nome, telefone e e-mail por valores que não identificam ninguém (`Cliente anonimizado`, `ANON-1`, sem e-mail). Os agendamentos continuam para os relatórios do salão | Art. 18, IV e VI (anonimização e eliminação) |

> **Atenção:** senhas cadastradas **antes** dessa mudança continuam no banco como texto. Elas só passam a ser criptografadas quando forem trocadas (pela atualização do profissional com uma senha nova).

**Ao mexer no código:** não coloque dados pessoais (nome, CPF, telefone, e-mail, senha) em mensagens de log nem em mensagens de erro.

---

## 7. Como contribuir (Git e Pull Requests)

O branch principal é o **`main`**. Ninguém trabalha direto nele: cada tarefa é feita num branch próprio e entra no `main` por um **Pull Request (PR)**.

**1. Antes de começar**, atualize o `main`:

```bash
git checkout main
git pull
```

**2. Crie um branch para a sua tarefa**, a partir do `main` atualizado (use um nome que diga o que você vai fazer):

```bash
git checkout -b feature/nome-da-tarefa
```

**3. Trabalhe e salve em commits**, com mensagens que expliquem o que mudou:

```bash
git add .
git commit -m "Implementa busca de clientes por telefone"
```

**4. Envie o branch para o GitHub:**

```bash
git push -u origin feature/nome-da-tarefa
```

**5. Abra o Pull Request:** no GitHub, aba **Pull requests** → **New pull request** → em *base* deixe `main` e em *compare* escolha o seu branch → **Create pull request**. Descreva o que foi feito e peça para alguém do grupo revisar.

**Boas práticas:**

- Uma funcionalidade por branch.
- Sempre dê `git pull` no `main` antes de criar um branch novo.
- Se o seu branch ficar desatualizado em relação ao `main`, rode `git pull origin main` dentro dele para trazer as novidades.
- **Nunca coloque senhas novas no código.**
