package org.nmdp.tarr2fhir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan({"io.swagger.api", "org.nmdp"})
public class Tarr2fhirApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tarr2fhirApplication.class, args);
    }

}
