package llm.backend.controller;

import cn.hutool.core.util.StrUtil;
import llm.backend.client.ModelClient;
import llm.backend.service.QueryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;

@RestController
@RequestMapping("/question")
@Slf4j
public class MyController {

    @Resource
    private QueryModel queryModel;

    @Resource
    private ModelClient modelClient;

    /**
     * 根据用户提问获取流式获取模型回复，直接使用webclient进行sse调用
     *
     * @param userMessage 用户提问
     * @return
     */
    @GetMapping("/webflux")
    public SseEmitter webflux(@RequestParam(value = "userMessage", required = false) String userMessage) {
        log.info("用户请求数据为: {}, 调用本地大模型进行问答", userMessage);
        // 提问内容为空，不请求模型
        if (StrUtil.isBlank(userMessage)) {
            return null;
        }
        return queryModel.queryByQuestion(userMessage);
    }


    /**
     * 调用nacos上注册的服务，根据用户提问获取模型回复（此时不是流式返回结果）
     *
     * @return
     */
    @GetMapping("/microservice")
    public String microservice() {
        return modelClient.getStreamData();
    }

    /**
     * TODO 调用nacos上注册的服务，根据用户提问流式获取模型回复（先查找ip地址再进行调用，此时是流式返回结果）
     *
     * @return
     */
    @GetMapping("/microserviceWebflux")
    public String microserviceWebflux() {
        return modelClient.getStreamData();
    }

}

