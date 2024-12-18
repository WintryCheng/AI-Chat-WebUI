package llm.backend.controller;

import cn.hutool.core.util.StrUtil;
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

    /**
     * 根据用户提问获取流式获取模型回复
     *
     * @param userMessage 用户提问
     * @return
     */
    @GetMapping("/local/sse")
    public SseEmitter localGenerateQuestionSSE(@RequestParam(value = "userMessage", required = false) String userMessage) {
        log.info("用户请求数据为: {}, 调用本地大模型进行问答", userMessage);
        // 提问内容为空，不请求模型
        if (StrUtil.isBlank(userMessage)) {
            return null;
        }
        return queryModel.queryByQuestion(userMessage);
    }

}

