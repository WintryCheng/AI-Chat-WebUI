package llm.model.modelservice.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import llm.model.modelservice.service.QueryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Service
public class queryModelImpl implements QueryModel {

    @Value("${url.fastapi}")
    private String fastapiUrl;

    @Value("${spring.cloud.nacos.serverAddr}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.username}")
    private String nacosUsername;

    @Value("${spring.cloud.nacos.password}")
    private String nacosPassword;

    /**
     * 根据问题获取模型回答
     *
     * @param question 用户提问
     * @return
     */
    @Override
    public SseEmitter queryByQuestion(String question) {
        // 创建webclient连接获取
        WebClient webClient = WebClient.create(fastapiUrl);
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

    /**
     * 从nacos上获取指定服务的ip地址，并根据问题获取模型回答
     *
     * @param question 用户提问
     * @return
     */
    @Override
    public SseEmitter queryByMsQuestion(String question) throws NacosException {
        // 设置Nacos的：服务器地址、用户名、密码
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("username", nacosUsername);
        properties.put("password", nacosPassword);

        // 创建NamingService对象
        NamingService namingService = NacosFactory.createNamingService(properties);

        // 获取所有实例
        List<Instance> instances = namingService.getAllInstances("fastapi-service");

        if (null == instances || instances.isEmpty()) {
            return null;
        }

        // 随机获取一个实例的IP和端口
        Instance instance = instances.get(RandomUtil.randomInt(instances.size()));
        // 创建webclient连接获取
        WebClient webClient = WebClient.create("http://" + instance.getIp() + ":" + instance.getPort());
        // 建立 SSE 连接对象，0表示永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);

        Flux<String> stringFlux = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(instance.getIp())
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
