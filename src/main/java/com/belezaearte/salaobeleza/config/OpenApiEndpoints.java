package com.belezaearte.salaobeleza.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

final class OpenApiEndpoints {

    record Param(String description, String example) {
    }

    record Endpoint(String summary, String description, Map<String, Param> params, String body) {
    }

    static final Map<String, Endpoint> ENDPOINTS = new HashMap<>();
    static final Map<String, String> FIELDS = new HashMap<>();

    private OpenApiEndpoints() {
    }

    private static void endpoint(String key, String summary, String description, String body, Object... params) {
        Map<String, Param> map = new LinkedHashMap<>();
        for (int i = 0; i < params.length; i += 3) {
            map.put((String) params[i], new Param((String) params[i + 1], (String) params[i + 2]));
        }
        ENDPOINTS.put(key, new Endpoint(summary, description, map, body));
    }

    private static void field(String schema, String name, String description) {
        FIELDS.put(schema + "." + name, description);
    }

    static {
        professionals();
        clients();
        services();
        resources();
        appointments();
        appointmentsItems();
        payments();
    }

    private static void professionals() {
        endpoint("POST /createProfessionals", "Cadastrar profissional",
                "Cadastra quem trabalha no salão. O CPF pode vir com ou sem máscara (no banco ficam só os 11 números). "
                        + "CPF, e-mail e usuário não podem repetir. A senha é gravada criptografada (BCrypt) e nunca volta "
                        + "nas respostas. Todo profissional novo começa ativo.",
                """
                {
                  "name": "Anna Souza",
                  "cpf": "529.982.247-25",
                  "phone": "(61) 99999-9999",
                  "email": "anna@email.com",
                  "specialty": "Cabeleireira",
                  "experienceYears": 5,
                  "username": "anna",
                  "password": "Senha123"
                }""");
        endpoint("PUT /updateProfessionals", "Atualizar profissional",
                "Atualiza o profissional do `id` informado. Se o CPF vier mascarado (`***.982.247-**`), o CPF atual é "
                        + "mantido. Senha vazia mantém a senha atual. Para inativar, mande `\"active\": false`.",
                """
                {
                  "id": 1,
                  "name": "Anna Souza",
                  "phone": "(61) 99999-9999",
                  "email": "anna@email.com",
                  "specialty": "Cabeleireira",
                  "experienceYears": 6,
                  "username": "anna",
                  "active": true
                }""");
        endpoint("DELETE /deleteProfessionals", "Remover profissional",
                "Apaga o profissional. Para manter o histórico de atendimentos, prefira inativar (`active: false`).",
                null, "id", "Id do profissional", "1");
        endpoint("GET /searchName", "Buscar profissional por nome", "Busca pelo nome exato.",
                null, "name", "Nome do profissional", "Anna Souza");
        endpoint("GET /searchEmail", "Buscar profissional por e-mail", null,
                null, "email", "E-mail do profissional", "anna@belezaearte.test");
        endpoint("GET /searchPhone", "Buscar profissional por telefone", null,
                null, "phone", "Telefone do profissional", "61990000001");
        endpoint("GET /searchSpecialty", "Buscar profissionais por especialidade", null,
                null, "specialty", "Especialidade", "Cabeleireira");
        endpoint("GET /searchCreated", "Buscar profissionais por data de cadastro", null,
                null, "created", "Data e hora do cadastro", "2026-11-03T10:00:00-03:00");

        field("ProfessionalsDto", "id", "Id gerado pelo banco. Obrigatório só na atualização");
        field("ProfessionalsDto", "name", "Nome completo (obrigatório)");
        field("ProfessionalsDto", "specialty", "Especialidade principal, para exibição");
        field("ProfessionalsDto", "phone", "Telefone");
        field("ProfessionalsDto", "email", "E-mail (não pode repetir)");
        field("ProfessionalsDto", "cpf", "CPF com ou sem máscara (11 dígitos, não pode repetir). Nas respostas sai mascarado");
        field("ProfessionalsDto", "experienceYears", "Anos de experiência");
        field("ProfessionalsDto", "username", "Usuário de login (não pode repetir)");
        field("ProfessionalsDto", "password", "Senha. Só é enviada, nunca volta nas respostas");
        field("ProfessionalsDto", "active", "Se ainda trabalha no salão");
        field("ProfessionalsDto", "created", "Data do cadastro (automática)");
        field("ProfessionalsDto", "updated", "Data da última alteração (automática)");
    }

    private static void clients() {
        endpoint("POST /createClients", "Cadastrar cliente",
                "Nome e telefone são obrigatórios. O telefone identifica o cliente e não pode repetir.",
                """
                {
                  "name": "Ana Souza",
                  "phone": "11999999999",
                  "email": "ana@email.com"
                }""");
        endpoint("PUT /updateClients", "Atualizar cliente",
                "Atualiza o cliente do `id` informado. A data de cadastro é mantida.",
                """
                {
                  "id": 1,
                  "name": "Ana Souza",
                  "phone": "11999999999",
                  "email": "ana.souza@email.com"
                }""");
        endpoint("DELETE /deleteClients", "Remover cliente",
                "Não remove o cliente se ele tiver agendamentos. Para atender um pedido de exclusão da LGPD, use "
                        + "`/anonymizeClients`.",
                null, "id", "Id do cliente", "1");
        endpoint("GET /searchNameClients", "Buscar clientes por nome",
                "Busca por parte do nome, sem diferenciar maiúsculas de minúsculas.",
                null, "name", "Nome ou parte do nome", "ana");
        endpoint("GET /searchPhoneClients", "Buscar cliente por telefone", null,
                null, "phone", "Telefone do cliente", "11999999999");
        endpoint("GET /searchEmailClients", "Buscar cliente por e-mail",
                "Sem diferenciar maiúsculas de minúsculas.",
                null, "email", "E-mail do cliente", "ana@email.com");
        endpoint("GET /searchCreatedClients", "Buscar clientes por data de cadastro", null,
                null, "created", "Data e hora do cadastro", "2026-11-03T10:00:00-03:00");
        endpoint("GET /exportClientsData", "Exportar dados do cliente (LGPD)",
                "Devolve todos os dados guardados do cliente, para atender o direito de acesso da LGPD.",
                null, "id", "Id do cliente", "1");
        endpoint("PUT /anonymizeClients", "Anonimizar cliente (LGPD)",
                "Apaga os dados pessoais do cliente e mantém os agendamentos, para não quebrar o histórico do salão.",
                null, "id", "Id do cliente", "1");

        field("ClientsDto", "id", "Id gerado pelo banco. Obrigatório só na atualização");
        field("ClientsDto", "name", "Nome do cliente (obrigatório)");
        field("ClientsDto", "phone", "Telefone (obrigatório, não pode repetir)");
        field("ClientsDto", "email", "E-mail");
        field("ClientsDto", "created", "Data do cadastro (automática)");
        field("ClientsDataExportDto", "client", "Dados guardados do cliente");
        field("ClientsDataExportDto", "purpose", "Para que os dados são usados");
        field("ClientsDataExportDto", "generatedAt", "Quando a exportação foi gerada");
    }

    private static void services() {
        endpoint("POST /service/createServices", "Cadastrar serviço",
                "Cadastra um serviço no catálogo do salão. A duração é usada pela agenda para calcular o término.",
                """
                {
                  "name": "Corte",
                  "description": "Corte de cabelo feminino ou masculino.",
                  "category": "Cabelo",
                  "price": 50.00,
                  "duration_minutes": 45
                }""");
        endpoint("PUT /service/updateServices", "Atualizar serviço", "Atualiza o serviço do `id` informado.",
                """
                {
                  "id": 1,
                  "name": "Corte",
                  "description": "Corte de cabelo feminino ou masculino.",
                  "category": "Cabelo",
                  "price": 55.00,
                  "duration_minutes": 45
                }""");
        endpoint("DELETE /service/deleteServices", "Remover serviço", null,
                null, "id", "Id do serviço", "1");
        endpoint("GET /service/searchName", "Buscar serviço por nome", null,
                null, "name", "Nome do serviço", "Corte");
        endpoint("GET /service/searchDescription", "Buscar serviços por descrição", null,
                null, "description", "Descrição do serviço", "Corte de cabelo feminino ou masculino.");
        endpoint("GET /service/searchCategory", "Buscar serviços por categoria", null,
                null, "category", "Categoria", "Cabelo");
        endpoint("GET /service/searchPrice", "Buscar serviços por preço", null,
                null, "price", "Preço em reais", "50.00");
        endpoint("GET /service/searchDuration", "Buscar serviços por duração", null,
                null, "duration_minutes", "Duração em minutos", "45");
        endpoint("GET /service/searchCreated", "Buscar serviços por data de cadastro", null,
                null, "searchCreation", "Data e hora do cadastro", "2026-11-03T10:00:00-03:00");

        field("ServicesDto", "id", "Id gerado pelo banco. Obrigatório só na atualização");
        field("ServicesDto", "name", "Nome do serviço (não pode repetir)");
        field("ServicesDto", "description", "Descrição exibida ao cliente");
        field("ServicesDto", "category", "Agrupamento (ex: Cabelo, Unhas, Estética)");
        field("ServicesDto", "price", "Preço atual, em reais");
        field("ServicesDto", "duration_minutes", "Quanto tempo o serviço leva, em minutos");
        field("ServicesDto", "created", "Data do cadastro");
    }

    private static void resources() {
        endpoint("POST /createResources", "Cadastrar recurso",
                "Cadeiras, cabines e equipamentos do salão. Tipos aceitos: `CADEIRA`, `CABINE` e `EQUIPAMENTO`.",
                """
                {
                  "name": "Cadeira 1",
                  "type": "CADEIRA",
                  "active": true
                }""");
        endpoint("PUT /updateResources", "Atualizar recurso",
                "Atualiza o recurso do `id` informado. Um recurso quebrado ou em manutenção fica com `active: false`.",
                """
                {
                  "id": 1,
                  "name": "Cadeira 1",
                  "type": "CADEIRA",
                  "active": false
                }""");
        endpoint("DELETE /deleteResources", "Remover recurso", null,
                null, "id", "Id do recurso", "1");
        endpoint("GET /searchNameResources", "Buscar recursos por nome", null,
                null, "name", "Nome do recurso", "Cadeira 1");
        endpoint("GET /searchTypeResources", "Buscar recursos por tipo", null,
                null, "type", "CADEIRA, CABINE ou EQUIPAMENTO", "CADEIRA");
        endpoint("GET /searchActiveResources", "Buscar recursos ativos ou inativos", null,
                null, "active", "true para os ativos, false para os inativos", "true");

        field("ResourcesDto", "id", "Id gerado pelo banco. Obrigatório só na atualização");
        field("ResourcesDto", "name", "Nome de identificação (ex: Cadeira 1)");
        field("ResourcesDto", "type", "CADEIRA, CABINE ou EQUIPAMENTO");
        field("ResourcesDto", "active", "Se o recurso está em uso");
    }

    private static void appointments() {
        endpoint("POST /createAppointments", "Criar agendamento",
                "Cria a visita do cliente, que precisa existir. Todo agendamento novo começa como `AGENDADO`, mesmo que "
                        + "outro status seja enviado. Os serviços da visita são cadastrados depois, em "
                        + "`/appointmentsItems/createAppointmentsItems`.",
                """
                {
                  "idClient": 1,
                  "notes": "Cliente prefere atendimento à tarde"
                }""");
        endpoint("PUT /updateAppointments", "Atualizar agendamento (status e observações)",
                "O status segue o ciclo: `AGENDADO` → `CONFIRMADO` → `CONCLUIDO`; `AGENDADO` ou `CONFIRMADO` → "
                        + "`CANCELADO`; `CONFIRMADO` → `NAO_COMPARECEU`. Para cancelar, informe `cancelledBy` (`CLIENTE` "
                        + "ou `SALAO`) e, se quiser, `cancelReason`. `notes` não enviado mantém o atual; texto vazio apaga. "
                        + "O cliente não pode ser trocado.",
                """
                {
                  "id": 1,
                  "status": "CANCELADO",
                  "cancelledBy": "CLIENTE",
                  "cancelReason": "Imprevisto"
                }""");
        endpoint("DELETE /deleteAppointments", "Remover agendamento",
                "Remove o agendamento e os itens dele. Não remove se ele tiver pagamentos. Para guardar o histórico, "
                        + "prefira cancelar.",
                null, "id", "Id do agendamento", "1");
        endpoint("GET /searchClientAppointments", "Agendamentos do cliente", null,
                null, "idClient", "Id do cliente", "1");
        endpoint("GET /searchProfessionalAppointments", "Agendamentos do profissional",
                "Agendamentos com algum serviço desse profissional.",
                null, "idProfessional", "Id do profissional", "1");
        endpoint("GET /searchServiceAppointments", "Agendamentos com o serviço", null,
                null, "idService", "Id do serviço", "1");
        endpoint("GET /searchStatusAppointments", "Agendamentos por status",
                "Sem diferenciar maiúsculas de minúsculas.",
                null, "status", "AGENDADO, CONFIRMADO, CONCLUIDO, CANCELADO ou NAO_COMPARECEU", "AGENDADO");
        endpoint("GET /searchNotesAppointments", "Agendamentos por observação",
                "Busca por parte do texto, sem diferenciar maiúsculas de minúsculas.",
                null, "notes", "Texto ou parte das observações", "retoque");
        endpoint("GET /searchPriceAppointments", "Agendamentos por preço de serviço",
                "Agendamentos com algum serviço desse preço.",
                null, "price", "Preço em reais", "80.00");
        endpoint("GET /searchDateAppointments", "Agenda do dia",
                "Agendamentos com algum serviço nessa data, no horário de Brasília.",
                null, "date", "Data (AAAA-MM-DD)", "2026-11-20");
        endpoint("GET /searchTimeAppointments", "Agendamentos por dia e horário",
                "Agendamentos com algum serviço começando exatamente nesse dia e horário.",
                null, "startAt", "Data e hora de início", "2026-11-20T14:00:00-03:00");
        endpoint("GET /searchCreatedAppointments", "Agendamentos por data de criação",
                "Busca pela data em que o agendamento foi feito.",
                null, "created", "Data e hora da criação", "2026-11-03T10:00:00-03:00");

        field("AppointmentsDto", "id", "Id gerado pelo banco. Obrigatório só na atualização");
        field("AppointmentsDto", "idClient", "Id do cliente que agendou (obrigatório na criação, não pode mudar)");
        field("AppointmentsDto", "status", "AGENDADO, CONFIRMADO, CONCLUIDO, CANCELADO ou NAO_COMPARECEU");
        field("AppointmentsDto", "notes", "Observações do cliente ou do salão");
        field("AppointmentsDto", "cancelledBy", "Quem cancelou: CLIENTE ou SALAO (obrigatório ao cancelar)");
        field("AppointmentsDto", "cancelReason", "Motivo do cancelamento");
        field("AppointmentsDto", "cancelledAt", "Quando foi cancelado (automático)");
        field("AppointmentsDto", "created", "Quando o agendamento foi feito (automático)");
        field("AppointmentsDto", "updated", "Última alteração (automático)");
        field("AppointmentsDto", "items", "Serviços da visita. Só nas respostas: são cadastrados em /appointmentsItems");
    }

    private static void appointmentsItems() {
        endpoint("POST /appointmentsItems/createAppointmentsItems", "Adicionar serviço ao agendamento",
                "Cadastra um serviço da visita, com profissional, recurso, horário e preço. Todos os campos são "
                        + "obrigatórios.",
                """
                {
                  "appointment_id": 1,
                  "service_id": 1,
                  "professional_id": 1,
                  "resource_id": 1,
                  "start_at": "2026-11-25T09:00:00-03:00",
                  "end_at": "2026-11-25T09:45:00-03:00",
                  "price": 50.00
                }""");
        endpoint("PUT /appointmentsItems/updateAppointmentsItems", "Alterar serviço do agendamento",
                "Atualiza o item do `id` informado (ex: remarcar o horário ou trocar o profissional).",
                """
                {
                  "id": 1,
                  "appointment_id": 1,
                  "service_id": 1,
                  "professional_id": 1,
                  "resource_id": 1,
                  "start_at": "2026-11-25T10:00:00-03:00",
                  "end_at": "2026-11-25T10:45:00-03:00",
                  "price": 50.00
                }""");
        endpoint("DELETE /appointmentsItems/deleteAppointmentsItems", "Remover serviço do agendamento", null,
                null, "id", "Id do item", "1");
        endpoint("GET /appointmentsItems/searchStartDate", "Buscar itens por início", null,
                null, "start_at", "Data e hora de início", "2026-11-25T09:00:00-03:00");
        endpoint("GET /appointmentsItems/searchEndDate", "Buscar itens por término", null,
                null, "end_at", "Data e hora de término", "2026-11-25T09:45:00-03:00");
        endpoint("GET /appointmentsItems/searchPrice", "Buscar itens por preço", null,
                null, "price", "Preço em reais", "50.00");

        field("AppointmentsItemsDto", "id", "Id gerado pelo banco. Obrigatório só na atualização");
        field("AppointmentsItemsDto", "appointment_id", "Id do agendamento (a visita)");
        field("AppointmentsItemsDto", "service_id", "Id do serviço");
        field("AppointmentsItemsDto", "professional_id", "Id do profissional que vai atender");
        field("AppointmentsItemsDto", "resource_id", "Id da cadeira, cabine ou equipamento");
        field("AppointmentsItemsDto", "start_at", "Data e hora de início");
        field("AppointmentsItemsDto", "end_at", "Data e hora de término (depois do início)");
        field("AppointmentsItemsDto", "price", "Preço cobrado neste atendimento, em reais");
    }

    private static void payments() {
        endpoint("PUT /payPayments", "Registrar pagamento",
                "Marca um pagamento `PENDENTE` como `PAGO`, com a forma escolhida e a data e hora. Formas: `PIX`, "
                        + "`CARTAO_CREDITO`, `CARTAO_DEBITO` e `DINHEIRO`.",
                """
                {
                  "id": 7,
                  "method": "DINHEIRO"
                }""");
        endpoint("GET /listPayments", "Listar pagamentos (tela do admin)",
                "Do mais recente para o mais antigo, já com cliente, serviços e data do atendimento. Sem `status`, traz "
                        + "todos. Sem resultados, devolve `[]`.",
                null, "status", "PENDENTE, PAGO, CANCELADO ou ESTORNADO (opcional)", "PAGO");
        endpoint("GET /searchSummaryPayments", "Resumo do mês (cards da tela de pagamentos)",
                "Recebido no mês e variação em relação ao mês anterior, total pendente e taxa de recebimento. "
                        + "Percentuais vêm `null` quando não há base de cálculo.",
                null, "month", "Mês no formato AAAA-MM (opcional, padrão: mês atual)", "2026-11");
        endpoint("GET /searchRevenuePayments", "Faturamento por período",
                "Soma dos pagamentos `PAGO` no período e variação em relação ao período anterior. `DAY` alimenta a "
                        + "Receita do dia do painel; `LAST_7_DAYS`, `MONTH` e `YEAR`, o Faturamento dos relatórios.",
                null,
                "period", "DAY, LAST_7_DAYS, MONTH ou YEAR", "MONTH",
                "date", "Data de referência AAAA-MM-DD (opcional, padrão: hoje)", "2026-11-18");
        endpoint("GET /searchAppointmentPayments", "Pagamentos do agendamento", null,
                null, "idAppointment", "Id do agendamento", "5");
        endpoint("GET /searchStatusPayments", "Pagamentos por status", null,
                null, "status", "PENDENTE, PAGO, CANCELADO ou ESTORNADO", "PENDENTE");

        field("PaymentsDto", "id", "Id do pagamento");
        field("PaymentsDto", "idAppointment", "Id do agendamento pago");
        field("PaymentsDto", "amount", "Valor, em reais");
        field("PaymentsDto", "method", "PIX, CARTAO_CREDITO, CARTAO_DEBITO ou DINHEIRO");
        field("PaymentsDto", "status", "PENDENTE, PAGO, CANCELADO ou ESTORNADO");
        field("PaymentsDto", "paidAt", "Quando foi pago");
        field("PaymentsDto", "created", "Quando o pagamento foi gerado");
        field("PaymentsListDto", "clientName", "Nome do cliente");
        field("PaymentsListDto", "services", "Serviços da visita, ex: Maquiagem + Penteados");
        field("PaymentsListDto", "appointmentDate", "Início do primeiro serviço da visita");
        field("PaymentsSummaryDto", "receivedMonth", "Recebido no mês");
        field("PaymentsSummaryDto", "receivedPreviousMonth", "Recebido no mês anterior");
        field("PaymentsSummaryDto", "receivedChangePercent", "Variação em relação ao mês anterior (%)");
        field("PaymentsSummaryDto", "pendingAmount", "Total pendente");
        field("PaymentsSummaryDto", "pendingAppointments", "Quantos agendamentos têm pagamento pendente");
        field("PaymentsSummaryDto", "receiptRatePercent", "Pagos ÷ (pagos + pendentes) dos pagamentos do mês (%)");
        field("PaymentsRevenueDto", "received", "Recebido no período");
        field("PaymentsRevenueDto", "receivedPrevious", "Recebido no período anterior equivalente");
        field("PaymentsRevenueDto", "changePercent", "Variação em relação ao período anterior (%)");
    }
}
