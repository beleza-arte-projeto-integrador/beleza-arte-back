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
6. [Como contribuir (Git e Pull Requests)](#6-como-contribuir-git-e-pull-requests)

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

**2.** Rode a aplicação:

| Terminal | Comando |
|---|---|
| Git Bash, Linux ou Mac | `./mvnw spring-boot:run` |
| PowerShell ou CMD (Windows) | `.\mvnw.cmd spring-boot:run` |

A primeira execução demora alguns minutos (ela baixa as dependências). Nas próximas é rápida.

**3.** Quando aparecer no terminal a linha `Started SalaobelezaApplication`, a API está no ar em:

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
| Appointments | Só a estrutura (interfaces) |

---

## 5. Endpoints disponíveis

Todos começam com `http://localhost:8081/v1/api/salao`. Para testar, use o [Postman](https://www.postman.com/), o [Insomnia](https://insomnia.rest/) ou a extensão *REST Client* do VS Code. Os nomes dos campos no JSON estão em **inglês** (`name`, `phone`, `email`…).

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

Exemplo de corpo para cadastrar:

```json
{
  "name": "Anna",
  "specialty": "Cabeleireira",
  "phone": "61999999999",
  "email": "anna@email.com"
}
```

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

Exemplo de corpo para cadastrar (os mesmos campos da tela de cadastro do front-end):

```json
{
  "name": "Ana Souza",
  "phone": "11999999999",
  "email": "ana@email.com"
}
```

---

## 6. Como contribuir (Git e Pull Requests)

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
