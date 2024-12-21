package llm.model.modelservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import llm.model.modelservice.domain.po.Dialogue;


public interface DialogueMapper extends BaseMapper<Dialogue> {
    void saveDialogue(Dialogue dialogue);
}
