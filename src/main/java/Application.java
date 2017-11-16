import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import service.DatabaseService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import web.HeatmapController;

/**
 * Created by Sebastian Urbanek on 26.10.16.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {HeatmapController.class, DatabaseService.class})
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Docket heatmapApi() {
        String host;
        if (DatabaseService.LOCAL_MODE) {
            host = "localhost:8080";
        } else {
//            host = "141.64.5.234/excell-heatmap-api";
            host = "dlr-integration.minglabs.com/api/v1/service-request/heatmapservice";
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("excell-heatmap-api")
                .select()
                .apis(RequestHandlerSelectors.any()) 
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .paths(Predicates.not(PathSelectors.regex("/health")))
                .paths(Predicates.not(PathSelectors.regex("/health.json")))
                .build()
                .genericModelSubstitutes(ResponseEntity.class)
                .protocols(Sets.newHashSet("https"))
                .host(host)
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Lists.newArrayList(securityContext()))
                .apiInfo(apiInfo())
                ;
    }
    
	private ApiKey apiKey() {
		return new ApiKey("api_key", "Authorization", "header");
	}
	
    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/*.*"))
            .build();
    }

    private List<SecurityReference> defaultAuth() {
    	List<SecurityReference> ls = new ArrayList<>();
    	AuthorizationScope authorizationScope
    		= new AuthorizationScope("global", "accessEverything");
    	AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    	authorizationScopes[0] = authorizationScope;
    	SecurityReference s = new SecurityReference("api_key", authorizationScopes);
    	ls.add(s);
    	return ls;
    }

	@Bean
	public SecurityConfiguration security() {
		return new SecurityConfiguration(null, null, null, null, "Token", ApiKeyVehicle.HEADER, "Authorization", ",");
	}

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "ExCELL Heatmap API",
                "Diese API stellt eine Heatmap Visualisierung für das ExCELL Testgebiet Dresden zur Verfügung. "
                + "Demzufolge wird Dresden in separate Kacheln aufgeteilt, "
                + "die die durchschnittliche Geschwindigkeit der Fahrzeuge und die aggregierte Anzahl der Fahrzeuge pro Kachel anzeigen. "
                + "Es ist hierbei möglich das Datum und zusätzliche Optionen auszuwählen, "
                + "um die Ausgabe der Kacheln zu individualisieren.",
                "Version 1.0",
                "Use only for testing",
                new Contact(
              		  "Felix Kunde, Stephan Pieper, Sebastian Urbanek",
              		  "https://projekt.beuth-hochschule.de/magda/poeple",
              		  "s61811@beuth-hochschule.de"),
                "Apache 2",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>());
              return apiInfo;
    }
   
}
