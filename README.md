# Beleza & Arte — Back-end

API do sistema de agendamento para salões de beleza de pequeno e médio porte, desenvolvido no Projeto Integrador do curso de Análise e Desenvolvimento de Sistemas (SENAC).

**Tecnologias:** Java 21 · Spring Boot · Spring Data JPA · PostgreSQL (Supabase) · Lombok · Maven

---

## Sumário

1. [O que você precisa ter instalado](#1-o-que-você-precisa-ter-instalado)
2. [Como rodar o projeto](#2-como-rodar-o-projeto)
3. [Banco local (opcional)](#3-banco-local-opcional)
4. [Como o código está organizado](#4-como-o-código-está-organizado)
5. [Endpoints disponíveis](#5-endpoints-disponíveis)
6. [Como contribuir (Git e Pull Requests)](#6-como-contribuir-git-e-pull-requests)

---

## 1. O que você precisa ter instalado

| Ferramenta | Obrigatório? | Para quê |
|---|---|---|
| **Java 21** (JDK) | Sim | Rodar o projeto. Recomendado: [Eclipse Temurin 21](https://adoptium.net/) |
| **Git** | Sim | Clonar o repositório e enviar suas alterações |
| Editor | Sim | VS Code com a extensão *Extension Pack for Java*, ou IntelliJ IDEA Community |
| Docker Desktop | **Não** | Só para quem quiser usar o [banco local](#3-banco-local-opcional) |

> **Não precisa instalar Maven nem Spring Boot.** O projeto tem o *Maven Wrapper* (`mvnw` / `mvnw.cmd`), que baixa tudo sozinho na primeira vez que você roda.

Para conferir o Java, abra o terminal e rode `java -version`. Deve aparecer `21`.

---

## 2. Como rodar o projeto

**1.** Clone o repositório e entre na pasta:

```bash
git clone https://github.com/beleza-arte-projeto-integrador/beleza-arte-back.git
cd beleza-arte-back
```

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

Para parar, aperte `Ctrl + C` no terminal.

> Nesse modo padrão a aplicação usa o **banco do Supabase**, que é compartilhado por todo o grupo. Ou seja: o que você cadastrar ou apagar aparece para todos. Para testar sem esse risco, use o banco local abaixo.

---

## 3. Banco local (opcional)

Um PostgreSQL rodando **no seu próprio computador**, dentro do Docker. Serve para testar à vontade — inclusive mudanças na estrutura das tabelas — sem afetar o banco do grupo.

**1.** Abra o **Docker Desktop** e espere ele iniciar.

**2.** Na pasta do projeto, ligue o banco:

```bash
docker compose up -d
```

**3.** Rode a aplicação com o perfil `dev` (é só o comando de antes com um parâmetro a mais):

| Terminal | Comando |
|---|---|
| Git Bash, Linux ou Mac | `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` |
| PowerShell ou CMD (Windows) | `.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"` |

Para confirmar que está usando o banco local, procure no terminal a linha `The following 1 profile is active: "dev"`.

**Comandos úteis do banco local:**

| Comando | O que faz |
|---|---|
| `docker compose up -d` | Liga o banco |
| `docker compose down` | Desliga o banco (os dados continuam salvos) |
| `docker compose down -v` | Desliga e **apaga todos os dados** (recomeçar do zero) |

Dados de conexão, caso queira abrir o banco num cliente como DBeaver ou pgAdmin: host `localhost`, porta `5432`, banco `salaobeleza`, usuário `dev`, senha `dev`.

### Como funciona o perfil `dev`

O Spring sempre lê o arquivo `src/main/resources/application.properties` (configuração do Supabase). Quando o perfil `dev` está ativo, ele lê **também** o `application-dev.properties`, e o que estiver nele substitui a configuração padrão — no caso, o endereço, usuário e senha do banco.

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

Exemplo: `ProfessionalsController` chama `CreateProfessionalsUseCase`, que é implementado por `ProfessionalsService`, que usa `CreateProfessionalsPort`, implementado por `ProfessionalsAdapter`, que salva com `ProfessionalsRepository`.

> **Dica para quem vai implementar uma parte nova:** use o módulo de **Professionals** como modelo — ele está completo, do controller ao repository.

**Situação atual dos módulos:**

| Módulo | Situação |
|---|---|
| Professionals | ✅ Completo, com teste unitário |
| Services | 🔄 Em andamento no branch `feature/fernanda` |
| Clients | ⏳ Só a estrutura (interfaces) — **livre para quem quiser implementar** |
| Appointments | ⏳ Só a estrutura (interfaces) — **livre para quem quiser implementar** |

---

## 5. Endpoints disponíveis

Todos começam com `http://localhost:8081/v1/api/salao`. Para testar, use o [Postman](https://www.postman.com/), o [Insomnia](https://insomnia.rest/) ou a extensão *REST Client* do VS Code.

### Professionals

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| POST | `/createProfessionals` | JSON no corpo | Cadastra um profissional |
| PUT | `/updateProfessionals` | JSON no corpo (com `id`) | Atualiza um profissional |
| DELETE | `/deleteProfessionals` | `?id=1` | Remove um profissional |
| GET | `/searchName` | `?name=Anna` | Busca por nome |
| GET | `/searchEmail` | `?email=anna@email.com` | Busca por e-mail |
| GET | `/searchPhone` | `?phone=61999999999` | Busca por telefone |
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

---

## 6. Como contribuir (Git e Pull Requests)

O branch principal é o **`main`**. Ninguém trabalha direto nele: cada tarefa é feita num branch próprio e entra no `main` por um **Pull Request (PR)**.

**1. Antes de começar**, atualize o `main`:

```bash
git checkout main
git pull
```

**2. Crie um branch para a sua tarefa** (use um nome que diga o que você vai fazer):

```bash
git checkout -b feature/clients-service
```

**3. Trabalhe e salve em commits pequenos**, com mensagens que expliquem o que mudou:

```bash
git add .
git commit -m "Implementa busca de clientes por telefone"
```

**4. Envie o branch para o GitHub:**

```bash
git push -u origin feature/clients-service
```

**5. Abra o Pull Request:** no GitHub, aba **Pull requests** → **New pull request** → em *base* deixe `main` e em *compare* escolha o seu branch → **Create pull request**. Descreva o que foi feito e peça para alguém do grupo revisar.

**Boas práticas:**

- Avise no grupo antes de começar uma tarefa, para ninguém fazer a mesma coisa em dobro.
- Prefira PRs pequenos, com um assunto só — são mais fáceis de revisar.
- **Nunca coloque senhas novas no código.**
- Se o seu branch ficar desatualizado em relação ao `main`, rode `git pull origin main` dentro dele para trazer as novidades.
