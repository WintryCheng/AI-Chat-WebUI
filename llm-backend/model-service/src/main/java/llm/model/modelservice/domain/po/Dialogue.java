package llm.model.modelservice.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// TODO 在插入本轮对话时，目前的表结构不方便查找上一次对话的主键id，后续优化
@TableName(value = "dialogue")
@Data
public class Dialogue {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户提问
     */
    private String question;
    /**
     * 模型回答
     */
    private String answer;
    /**
     * 上一句对话的主键id
     */
    private Long lastId;
    /**
     * 逻辑删除
     */
    private String deleted;
}
