import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Predicates;

import service.DatabaseService;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
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
            host = "141.64.5.234/excell-heatmap-api";
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
//          .protocols(Sets.newHashSet("https"))
                .host(host)
                .apiInfo(apiInfo())
                ;
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "ExCELL Heatmap API",
                "This API provides a heatmap visualization for the ExCELL test area (Dresden).",
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
