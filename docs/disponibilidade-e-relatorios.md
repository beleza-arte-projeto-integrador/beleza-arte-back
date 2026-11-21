# Disponibilidade e Relatórios gerenciais

Responsável: **Matheus**

Este documento explica as duas partes que eu implementei no back-end: a **disponibilidade** dos profissionais (tabela `professional_availability`) e os **relatórios gerenciais**. As duas seguem a mesma arquitetura hexagonal do resto do projeto (Controller → UseCase → Service → Port → Adapter → Repository).

Nenhuma tabela foi criada ou alterada: a `professional_availability` já existe no Supabase, conforme o [dicionário de dados](banco-de-dados.md#professional_availability--horário-de-trabalho). Nenhum arquivo de outro integrante foi modificado.

---

## 1. Disponibilidade (`professional_availability`)

Cada registro é uma **faixa de horário** em que o profissional trabalha em um dia da semana. Para ter intervalo de almoço, cadastre duas faixas no mesmo dia (ex: 09:00–12:00 e 13:00–18:00).

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| POST | `/createProfessionalAvailability` | JSON no corpo | Cadastra uma faixa de horário |
| PUT | `/updateProfessionalAvailability` | JSON no corpo (com `id`) | Altera uma faixa |
| DELETE | `/deleteProfessionalAvailability` | `?id=1` | Remove uma faixa |
| GET | `/searchProfessionalAvailability` | `?idProfessional=1` | Lista as faixas do profissional, em ordem de dia e horário (lista vazia = ainda não cadastrou) |
| GET | `/searchAvailableTimes` | `?idProfessional=1&date=2026-11-24&durationMinutes=60&intervalMinutes=60` | **Horários livres** do profissional no dia |

Exemplo de corpo para cadastrar (segunda-feira, das 9h às 12h):

```json
{
  "idProfessional": 1,
  "weekday": 1,
  "startTime": "09:00",
  "endTime": "12:00"
}
```

**Regras:**

- `weekday`: `0` = domingo, `1` = segunda … `6` = sábado (igual ao banco).
- `startTime` tem que ser antes de `endTime`.
- O profissional precisa existir.
- Duas faixas do mesmo profissional no mesmo dia **não podem se sobrepor** (ex: 09:00–12:00 e 11:00–14:00).

**Horários livres (`/searchAvailableTimes`):**

1. Pega as faixas de trabalho do dia da semana da data.
2. Divide em horários a cada `intervalMinutes` (padrão 60), com a duração do serviço `durationMinutes` (padrão 60). O serviço precisa terminar dentro da faixa.
3. Tira os horários que batem com outro serviço já agendado desse profissional (mesma regra de sobreposição do dicionário de dados). Agendamentos **cancelados** liberam o horário.

Resposta (cada item já traz `startAt` no horário de Brasília, pronto para criar o item do agendamento):

```json
[
  { "startTime": "09:00:00", "endTime": "10:00:00", "startAt": "2026-11-24T09:00:00-03:00", "endAt": "2026-11-24T10:00:00-03:00" }
]
```

Se o profissional não trabalha nesse dia da semana, a lista vem vazia.

---

## 2. Relatórios gerenciais

| Método | Caminho | Parâmetros | O que faz |
|---|---|---|---|
| GET | `/searchManagementReport` | `?startDate=2026-11-01&endDate=2026-11-30` e, opcional, `&idProfessional=1` | Indicadores do salão no período |

O período pode ter no máximo 366 dias. As datas consideram o horário de Brasília.

**O que vem na resposta:**

| Campo | Significado |
|---|---|
| `totalAppointments` | Agendamentos (visitas) com algum serviço no período |
| `completedAppointments` / `cancelledAppointments` / `noShowAppointments` | Quantos estão `CONCLUIDO`, `CANCELADO` e `NAO_COMPARECEU` |
| `cancellationRate` / `noShowRate` | Percentual de cancelamentos e de faltas sobre o total (ex: `12.5` = 12,5%) |
| `totalServices` | Serviços válidos (de agendamentos que não foram cancelados nem tiveram falta) |
| `expectedRevenue` | Faturamento previsto: soma do preço dos serviços válidos |
| `completedRevenue` | Faturamento dos agendamentos concluídos |
| `averageTicket` | Ticket médio: faturamento previsto ÷ agendamentos válidos |
| `uniqueClients` | Clientes diferentes atendidos (sem contar cancelamentos e faltas) |
| `availableMinutes` / `bookedMinutes` / `occupancyRate` | Taxa de ocupação: minutos agendados ÷ minutos de expediente cadastrados em `professional_availability`. Fica `null` se ninguém do filtro tiver horário cadastrado |
| `services` | Ranking de serviços: quantidade e faturamento |
| `professionals` | Desempenho por profissional: serviços, faturamento, cancelamentos e faltas |
| `servicesByWeekday` | Serviços por dia da semana (posição 0 = domingo … 6 = sábado) |
| `servicesByHour` | Serviços por horário de início (ex: `"09:00": 4`) |

Os preços usados são os de `appointment_items.price` (o preço cobrado no dia), então um aumento futuro de preço não muda o histórico. O relatório não mostra dados pessoais dos clientes (LGPD): só contagens.

---

## 3. Arquivos

```
adapter/in/web/controller/ProfessionalAvailabilityController.java
adapter/in/web/controller/ManagementReportsController.java
adapter/out/persistence/ProfessionalAvailability.java            (entidade da tabela)
adapter/out/persistence/ProfessionalAvailabilityAdapter.java
adapter/out/persistence/ManagementReportsAdapter.java
adapter/out/persistence/repository/ProfessionalAvailabilityRepository.java
application/domain/dto/ProfessionalAvailabilityDto.java
application/domain/dto/AvailableTimeDto.java
application/domain/dto/ManagementReportDto.java
application/domain/dto/ReportServiceDto.java
application/domain/dto/ReportProfessionalDto.java
application/domain/service/ProfessionalAvailabilityService.java
application/domain/service/ManagementReportsService.java
application/port/in/ProfessionalAvailability/   (5 use cases)
application/port/in/ManagementReports/          (1 use case)
application/port/out/professionalAvailabilityPort/  (6 ports)
application/port/out/managementReportsPort/         (2 ports)
```

Os agendamentos do período são buscados pelo `SearchDateAppointmentsPort`, que já existia no módulo Appointments (só é usado, não foi alterado).

## 4. Testes unitários

34 testes (JUnit 5 + Mockito, no mesmo padrão dos outros módulos):

- `ProfessionalAvailabilityServiceTest`: cadastro, validações, sobreposição, intervalo de almoço, edição, exclusão e horários livres.
- `ProfessionalAvailabilityAdapterTest`: exclusão, edição e busca.
- `ManagementReportsAdapterTest`: busca de serviços e profissionais para os nomes do relatório.
- `ManagementReportsServiceTest`: contagens, taxas, faturamento, ticket médio, rankings, dia da semana e horário, filtro por profissional, ocupação e período inválido.

Para rodar só esses testes:

| Terminal | Comando |
|---|---|
| Git Bash, Linux ou Mac | `./mvnw test -Dtest="ProfessionalAvailability*Test,ManagementReports*Test"` |
| PowerShell ou CMD (Windows) | `.\mvnw.cmd test "-Dtest=ProfessionalAvailability*Test,ManagementReports*Test"` |
