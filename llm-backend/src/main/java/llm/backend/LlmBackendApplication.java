package llm.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LlmBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LlmBackendApplication.class, args);
    }

}
