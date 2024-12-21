package llm.model.modelservice.service;

import com.alibaba.nacos.api.exception.NacosException;
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

    /**
     * 从nacos上获取指定服务的ip地址，并根据问题获取模型回答
     *
     * @param question 用户提问
     * @return
     */
    SseEmitter queryByMsQuestion(String question) throws NacosException;
}
