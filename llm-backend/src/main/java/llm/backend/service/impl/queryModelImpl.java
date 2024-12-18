package llm.backend.service.impl;

import llm.backend.service.QueryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class queryModelImpl implements QueryModel {

    /**
     * 根据问题获取模型回答
     *
     * @param question 用户提问
     * @return
     */
    @Override
    public SseEmitter queryByQuestion(String question) {
        // 建立 SSE 连接对象，0表示永不超时
        WebClient webClient = WebClient.create("http://127.0.0.1:8000");
        // 建立 SSE 连接对象，0表示永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);

        Flux<String> stringFlux = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("127.0.0.1")
                        .path("/stream")
                        .queryParam("prompt", question)
                        .build()
                )
                .retrieve()
                .bodyToFlux(String.class);

        log.info("AI模型回答为: ");
        stringFlux
                .map(item -> Objects.equals(item, "None") ? "" : item)
                .doOnNext(item -> {
                    log.info(item);
                    try {
                        sseEmitter.send(item);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnError((e) -> log.error("sse error: ", e))
                .doOnComplete(sseEmitter::complete)
                .subscribe();

        return sseEmitter;
    }
}
