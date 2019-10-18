package jp.mb.ride.allocation.service.documentation

import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors.any
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun docket(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(metadata())
                .useDefaultResponseMessages(false)
                .securitySchemes(listOf(apiKey()))
                .securityContexts(listOf(securityContext()))
    }

    @Bean
    fun security(): SecurityConfiguration {
        return SecurityConfigurationBuilder.builder().scopeSeparator(",")
                .additionalQueryStringParams(null)
                .useBasicAuthenticationWithAccessCodeGrant(false).build()
    }


    private fun apiKey(): ApiKey {
        return ApiKey("apiKey", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build()
    }

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope(
                "global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("apiKey",
                authorizationScopes))
    }

    private fun metadata(): ApiInfo {
        return ApiInfoBuilder()//
                .title("Ride allocation service API")
                .description("Ride allocation service")
                .version("1.0.0")
                .contact(Contact(null, null, "myoungsokang@gmail.com"))
                .build()
    }
}
