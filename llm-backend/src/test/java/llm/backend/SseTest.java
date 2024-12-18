package llm.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootTest
public class SseTest {

    @Test
    public void test1() throws InterruptedException {
        WebClient webClient = WebClient.create("http://127.0.0.1:8000");
        Flux<String> stringFlux = webClient
                .get()
                .uri("/stream")
                .retrieve()
                .bodyToFlux(String.class);
        stringFlux.subscribe(event -> {
            System.out.println(event);
        });
        Thread.sleep(5L * 1000);
    }

}
