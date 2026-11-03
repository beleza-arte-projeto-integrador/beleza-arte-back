Claude
















Readme · TXT
# Beleza & Arte — Back-end
 
API do sistema de agendamento para salões de beleza de pequeno e médio porte, desenvolvido no Projeto Integrador do curso de Análise e Desenvolvimento de Sistemas (SENAC).
 
**Tecnologias:** Java 21 · Spring Boot · Spring Data JPA · PostgreSQL (Supabase) · Flyway · Lombok · Maven
 
**Banco de dados:** veja o [dicionário de dados](docs/banco-de-dados.md), com a explicação de cada tabela e campo.
 
> **No dia a dia é só isso:** `git pull` no `main` e rodar a aplicação (seção 2). O banco se atualiza sozinho.
 
---
 
## Sumário
 
1. [O que você precisa ter instalado](#1-o-que-você-precisa-ter-instalado)
2. [Como rodar o projeto](#2-como-rodar-o-projeto)
3. [Banco de dados](#3-banco-de-dados)
4. [Banco local (opcional)](#4-banco-local-opcional)
5. [Como o código está organizado](#5-como-o-código-está-organizado)
6. [Endpoints disponíveis](#6-endpoints-disponíveis)
7. [Como contribuir (Git e Pull Requests)](#7-como-contribuir-git-e-pull-requests)
 
---
 
## 1. O que você precisa ter instalado
 
| Ferramenta | Obrigatório? | Para quê |
|---|---|---|
| **Java 21** (JDK) | Sim | Rodar o projeto. Recomendado: [Eclipse Temurin 21](https://adoptium.net/) |
| **Git** | Sim | Clonar o repositório e enviar suas alterações |
| Editor | Sim | VS Code com a extensão *Extension Pack for Java*, ou IntelliJ IDEA Community |
| Docker Desktop | **Não** | Só para quem quiser usar o [banco local](#4-banco-local-opcional) |
 
> **Não precisa instalar Maven, Spring Boot nem Flyway.** O projeto tem o *Maven Wrapper* (`mvnw` / `mvnw.cmd`), que baixa tudo sozinho na primeira vez que você roda.
 
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
 
> Nesse modo padrão a aplicação usa o **banco do Supabase**, que é compartilhado por todo o grupo. Ou seja: o que você cadastrar ou apagar aparece para todos. Para testar sem esse risco, use o [banco local](#4-banco-local-opcional).
 
---
 
## 3. Banco de dados
 
### Flyway: o banco se atualiza sozinho
 
A estrutura do banco (tabelas, campos, regras) fica em **scripts SQL numerados**, na pasta `src/main/resources/db/migration/`:
 
| Script | O que faz |
|---|---|
| `V1__cria_tabelas_iniciais.sql` | Cria as 9 tabelas do sistema |
| `V2__dados_de_teste.sql` | Coloca dados fictícios em todas as tabelas |
 
**Ninguém precisa rodar o Flyway à mão.** Toda vez que a aplicação sobe, o Flyway confere no banco quais scripts já rodaram (tabela `flyway_schema_history`) e executa só os novos. Se não houver nada novo, aparece no terminal `Schema "public" is up to date`.
 
Logo depois, o Hibernate **confere** se as classes Java (entidades) batem com as tabelas. Se alguma estiver diferente, a aplicação não sobe e mostra um erro de `Schema-validation` — o que evita estragar o banco por engano.
 
### Dados de teste
 
O `V2` já deixa o banco com dados fictícios em todas as tabelas — os mesmos profissionais, serviços e preços que aparecem no front-end:
 
- **10 serviços** (um inativo) e **9 profissionais** (Anna é a dona do salão, com papel `ADMIN`; Laura está inativa)
- **6 clientes**, **8 agendamentos** (um de cada status: agendado, confirmado, concluído, cancelado, não compareceu) e **6 pagamentos**
- Horários de trabalho, recursos do salão (cadeiras, cabine) e os serviços que cada profissional faz
 
**Login de teste:** usuário = coluna `username` (ex: `anna`), senha `Senha@123` para todos. *(O login ainda não está implementado na API — veja a seção 5.)*
 
As datas dos agendamentos são relativas ao dia em que o script rodou (alguns no passado, outros nos dias seguintes).
 
### Como mudar uma tabela
 
Só para quem precisar mudar a estrutura do banco (é raro):
 
1. **Nunca edite** um script que já está no `main` (V1, V2…). O Flyway percebe a alteração e a aplicação para de subir para todo mundo.
2. Crie o **próximo** script na mesma pasta: `V3__o_que_ele_faz.sql` (são **dois** sublinhados depois do número).
3. Ajuste a entidade Java correspondente.
4. **Teste no [banco local](#4-banco-local-opcional) antes de abrir o PR.** Depois do merge, o script é aplicado no Supabase na primeira vez que alguém rodar a aplicação — e aí não tem volta fácil.
5. Atualize o [dicionário de dados](docs/banco-de-dados.md).
 
---
 
## 4. Banco local (opcional)
 
Um PostgreSQL rodando **no seu próprio computador**, dentro do Docker. Serve para testar à vontade — inclusive mudanças na estrutura das tabelas — sem afetar o banco do grupo. O Flyway cria as tabelas e os dados de teste sozinho, igual ao Supabase.
 
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
 
Para confirmar que está usando o banco local, procure no terminal a linha `The following 1 profile is active: "dev"`. Na primeira vez também aparece `Successfully applied 2 migrations`.
 
**Comandos úteis do banco local:**
 
| Comando | O que faz |
|---|---|
| `docker compose up -d` | Liga o banco |
| `docker compose down` | Desliga o banco (os dados continuam salvos) |
| `docker compose down -v` | Desliga e **apaga todos os dados** (recomeçar do zero; na próxima vez o Flyway recria tudo) |
 
Dados de conexão, caso queira abrir o banco num cliente como DBeaver ou pgAdmin: host `localhost`, porta `5432`, banco `salaobeleza`, usuário `dev`, senha `dev`.
 
**Como funciona o perfil `dev`:** o Spring sempre lê o arquivo `src/main/resources/application.properties` (configuração do Supabase). Quando o perfil `dev` está ativo, ele lê **também** o `application-dev.properties`, e o que estiver nele substitui a configuração padrão — no caso, só o endereço, usuário e senha do banco.
 
---
 
## 5. Como o código está organizado
 
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
 
> **Dica para quem vai implementar uma parte nova:** use o módulo de **Professionals** como modelo — ele está completo, do controller ao repository. Os campos de cada tabela estão no [dicionário de dados](docs/banco-de-dados.md).
 
### Situação atual dos módulos
 
| Módulo | Situação |
|---|---|
| Professionals | ✅ Funciona, com teste unitário. A entidade ainda mapeia só 6 campos; faltam os novos (`cpf`, `username`, `role`, `active`…) |
| Services | 🔄 Em andamento no branch `feature/fernanda`. **Antes do merge:** na entidade `Services`, trocar `@Column(name = "created")` por `@Column(name = "created_at")` |
| Clients | ✅ Completo, com testes unitários. Mapeia todos os campos da tabela `clients` |
| Appointments | ⏳ Só a estrutura (interfaces), feita para o modelo antigo (um serviço por agendamento). Precisa seguir o modelo novo: `appointments` + `appointment_items` — **livre para quem quiser implementar** |
 
### O que ainda falta no back-end
 
- [x] Implementar Clients
- [ ] Implementar Appointments (com a verificação de **conflito de horário** — a regra está no [dicionário de dados](docs/banco-de-dados.md#4-regras-de-negócio-que-ficam-no-código))
- [ ] Concluir Services (PR da Fernanda)
- [ ] Mapear os novos campos de Professionals e o relacionamento com os serviços que cada um faz
- [ ] **CORS:** liberar o endereço do front-end para ele conseguir chamar a API pelo navegador
- [ ] **Login** dos profissionais (Spring Security + BCrypt; os hashes de teste já estão no banco)
- [ ] Antes da entrega: trocar a senha do Supabase e tirá-la do código (variável de ambiente)
 
---
 
## 6. Endpoints disponíveis
 
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
 
Os caminhos de Clients terminam em `Clients` porque `/searchName`, `/searchEmail` etc. já são usados por Professionals — dois endpoints com o mesmo caminho impedem a aplicação de subir.
 
| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| POST | `/createClients` | JSON no corpo | Cadastra um cliente (nome e telefone obrigatórios; o telefone não pode repetir) |
| PUT | `/updateClients` | JSON no corpo (com `id`) | Atualiza um cliente (a data de cadastro é mantida) |
| DELETE | `/deleteClients` | `?id=1` | Remove um cliente (não remove se ele tiver agendamentos) |
| GET | `/searchNameClients` | `?name=ana` | Busca por nome (parte do nome, sem diferenciar maiúsculas) |
| GET | `/searchPhoneClients` | `?phone=11999999999` | Busca por telefone |
| GET | `/searchEmailClients` | `?email=ana@email.com` | Busca por e-mail (sem diferenciar maiúsculas) |
| GET | `/searchAddressClients` | `?address=flores` | Busca por endereço (parte do texto) |
| GET | `/searchCreatedClients` | `?created=2026-11-03T10:00:00` | Busca por data de cadastro |
 
Exemplo de corpo para cadastrar:
 
```json
{
  "name": "Ana Souza",
  "phone": "11999999999",
  "email": "ana@email.com",
  "address": "Rua das Flores, 100",
  "birthDate": "1995-04-12",
  "preferences": "Prefere atendimento pela manhã"
}
```
 
---
 
## 7. Como contribuir (Git e Pull Requests)
 
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
- Mudou alguma tabela? Siga a receita da [seção 3](#como-mudar-uma-tabela).
 
Claude terminou a resposta
