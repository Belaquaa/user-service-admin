package belaquaa.userserviceadmin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI adminServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("User Service Admin API")
                        .description("API для управления пользователями в User Service Admin")
                        .version("v1.0"));
    }

    @Bean
    public GroupedOpenApi adminUsersGroupApi() {
        return GroupedOpenApi.builder()
                .group("admin-users")
                .pathsToMatch("/admin/users/**")
                .build();
    }
}