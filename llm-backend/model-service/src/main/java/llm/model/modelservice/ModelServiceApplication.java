package llm.model.modelservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "llm.model.client")
@SpringBootApplication
public class ModelServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModelServiceApplication.class, args);
    }

}
