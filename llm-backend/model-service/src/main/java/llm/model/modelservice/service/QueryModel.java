package llm.model.modelservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public interface QueryModel {


    /**
     * 根据问题获取模型回答
     *
     * @param question 用户提问
     * @return
     */
    SseEmitter queryByQuestion(String question);

}
