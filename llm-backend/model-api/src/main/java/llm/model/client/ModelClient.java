package llm.model.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * feign客户端，用于调用nacos上注册的服务
 */
@FeignClient("fastapi-service")
public interface ModelClient {
    @GetMapping("/stream")
    public String getStreamData();
}
