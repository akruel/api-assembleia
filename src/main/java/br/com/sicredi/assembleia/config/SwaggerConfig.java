package br.com.sicredi.assembleia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.basePackage("br.com.sicredi.assembleia.v1"))
          .build()
          .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
              .title("API para votação de pautas cooperativas")
              .description("Trello da API: https://trello.com/b/LLuq5hxp/api-assembleia")
              .version("1.0.0")
              .contact(new Contact("Anderson Kruel", "https://www.linkedin.com/public-profile/settings?trk=d_flagship3_profile_self_view_public_profile&lipi=urn%3Ali%3Apage%3Ad_flagship3_profile_self_edit_top_card%3B98%2BVDj3DRJqRvyEBJ%2FdjpA%3D%3D", "anderson.kruel@compasso.com.br"))
              .build();
  }

}