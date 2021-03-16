package net.tislib.restaurantapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant Review API",
                version = "1.0.0",
                description = "Api documentation for Restaurant Review APP",
                contact = @Contact(
                        name = "Taleh Ibrahimli",
                        email = "talehsmail@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        security = {
                @SecurityRequirement(
                        name = "apiKeyBearerAuth",
                        scopes = {
                                "REGULAR", "OWNER", "ADMIN"
                        }
                )
        }
)
@SecurityScheme(name = "apiKeyBearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "Api Key",
        description = "Get access token: <a target=\"_blank\" href=\"#/authentication/token\">link</a>"
)
public class OpenApiConfig {

}
