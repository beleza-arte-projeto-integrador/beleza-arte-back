package com.belezaearte.salaobeleza.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class OpenApiConfig {

    private static final String ERROR_SCHEMA = "Erro";
    private static final Map<String, Tag> TAGS = new LinkedHashMap<>();

    static {
        tag("professionals-controller", "Profissionais", "Cadastro de quem trabalha no salão");
        tag("clients-controller", "Clientes", "Cadastro de clientes e direitos da LGPD (exportar e anonimizar dados)");
        tag("services-controller", "Serviços", "Catálogo de serviços do salão");
        tag("resources-controller", "Recursos", "Cadeiras, cabines e equipamentos");
        tag("appointments-controller", "Agendamentos", "A visita do cliente ao salão e o status dela");
        tag("appointments-items-controller", "Itens de agendamento", "Cada serviço da visita, com profissional, horário e preço");
        tag("payments-controller", "Pagamentos", "Pagamentos dos agendamentos e valores para as telas do admin");
    }

    private static void tag(String controller, String name, String description) {
        TAGS.put(controller, new Tag().name(name).description(description));
    }

    @Bean
    public OpenAPI belezaArteOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Beleza & Arte — API")
                .version("v1")
                .description("""
                        Endpoints do back-end do salão Beleza & Arte.

                        - Os campos do JSON estão em inglês (`name`, `phone`, `email`…).
                        - Datas e horas seguem o padrão ISO 8601, com fuso: `2026-11-20T14:00:00-03:00`.
                        - Quando uma regra de negócio não é atendida (registro não encontrado, dado repetido, status \
                        inválido…), a resposta é **422** com `code`, `message` e `description`.

                        Regras completas no README do projeto; campos das tabelas em `docs/banco-de-dados.md`."""));
    }

    @Bean
    public GroupedOpenApi allEndpoints() {
        return group("1-todos", "1. Todos os endpoints", Set.of());
    }

    @Bean
    public GroupedOpenApi registrations() {
        return group("2-cadastros", "2. Cadastros", Set.of("ProfessionalsController", "ClientsController",
                "ServicesController", "ResourcesController"));
    }

    @Bean
    public GroupedOpenApi schedule() {
        return group("3-agenda", "3. Agenda", Set.of("AppointmentsController", "AppointmentsItemsController"));
    }

    @Bean
    @ConditionalOnClass(name = "com.belezaearte.salaobeleza.adapter.in.web.controller.PaymentsController")
    public GroupedOpenApi finance() {
        return group("4-financeiro", "4. Financeiro", Set.of("PaymentsController"));
    }

    private GroupedOpenApi group(String name, String displayName, Set<String> controllers) {
        GroupedOpenApi.Builder builder = GroupedOpenApi.builder().group(name).displayName(displayName);
        if (controllers.isEmpty()) {
            builder.pathsToMatch("/**");
        } else {
            builder.addOpenApiMethodFilter(method -> controllers.contains(method.getDeclaringClass().getSimpleName()));
        }
        return builder.build();
    }

    @Bean
    public GlobalOpenApiCustomizer belezaArteDocumentation() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }
            addErrorSchema(openApi);
            List<Tag> usedTags = new ArrayList<>();
            openApi.getPaths().forEach((path, item) -> item.readOperationsMap().forEach((method, operation) -> {
                renameTags(operation, usedTags);
                describe(method, path, operation);
                describeResponses(method, operation);
            }));
            openApi.setTags(TAGS.values().stream().filter(usedTags::contains).toList());
            sortOperations(openApi);
            describeFields(openApi);
        };
    }

    private void sortOperations(OpenAPI openApi) {
        Paths sorted = new Paths();
        openApi.getPaths().entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, PathItem> entry) -> rank(entry.getValue()))
                        .thenComparing(Map.Entry::getKey))
                .forEach(entry -> sorted.addPathItem(entry.getKey(), entry.getValue()));
        openApi.setPaths(sorted);
    }

    private int rank(PathItem item) {
        if (item.getPost() != null) {
            return 0;
        }
        if (item.getPut() != null) {
            return 1;
        }
        if (item.getDelete() != null) {
            return 2;
        }
        return 3;
    }

    private void renameTags(Operation operation, List<Tag> usedTags) {
        if (operation.getTags() == null) {
            return;
        }
        operation.setTags(operation.getTags().stream().map(name -> {
            Tag tag = TAGS.get(name);
            if (tag == null) {
                return name;
            }
            if (!usedTags.contains(tag)) {
                usedTags.add(tag);
            }
            return tag.getName();
        }).toList());
    }

    private void describe(PathItem.HttpMethod method, String path, Operation operation) {
        OpenApiEndpoints.Endpoint endpoint = OpenApiEndpoints.ENDPOINTS.get(method.name() + " " + path);
        if (endpoint == null) {
            return;
        }
        operation.setSummary(endpoint.summary());
        operation.setDescription(endpoint.description());
        if (operation.getParameters() != null) {
            operation.getParameters().forEach(parameter -> {
                OpenApiEndpoints.Param param = endpoint.params().get(parameter.getName());
                if (param != null) {
                    parameter.setDescription(param.description());
                    parameter.setExample(param.example());
                }
            });
        }
        if (endpoint.body() != null && operation.getRequestBody() != null && operation.getRequestBody().getContent() != null) {
            operation.getRequestBody().getContent().values().forEach(media -> media.setExample(endpoint.body()));
        }
    }

    private void describeResponses(PathItem.HttpMethod method, Operation operation) {
        ApiResponses responses = operation.getResponses() == null ? new ApiResponses() : operation.getResponses();
        if (method == PathItem.HttpMethod.DELETE) {
            responses.remove("200");
            responses.addApiResponse("204", new ApiResponse().description("Removido. A resposta não tem corpo"));
        }
        responses.forEach((code, response) -> {
            if (code.equals("200")) {
                response.setDescription("Sucesso");
            } else if (code.equals("201")) {
                response.setDescription("Criado com sucesso");
            }
        });
        responses.addApiResponse("400", new ApiResponse()
                .description("Parâmetro obrigatório ausente ou em formato inválido (ex: data ou número errado)"));
        responses.addApiResponse("422", new ApiResponse()
                .description("Regra de negócio não atendida (ex: registro não encontrado, dado repetido)")
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + ERROR_SCHEMA)))));
        operation.setResponses(responses);
    }

    private void addErrorSchema(OpenAPI openApi) {
        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }
        openApi.getComponents().addSchemas(ERROR_SCHEMA, new ObjectSchema()
                .description("Corpo das respostas de erro de regra de negócio")
                .addProperty("code", new StringSchema().description("Código HTTP").example("422"))
                .addProperty("message", new StringSchema().description("O que aconteceu")
                        .example("Could not find appointment!, The requested appointment was not found in the system!"))
                .addProperty("description", new StringSchema().description("Detalhe adicional, quando houver")));
    }

    @SuppressWarnings("rawtypes")
    private void describeFields(OpenAPI openApi) {
        if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
            return;
        }
        openApi.getComponents().getSchemas().forEach((schemaName, schema) -> {
            Map<String, Schema> properties = schema.getProperties();
            if (properties == null) {
                return;
            }
            properties.forEach((field, property) -> {
                String description = OpenApiEndpoints.FIELDS.get(schemaName + "." + field);
                if (description != null) {
                    property.setDescription(description);
                }
            });
        });
    }
}
