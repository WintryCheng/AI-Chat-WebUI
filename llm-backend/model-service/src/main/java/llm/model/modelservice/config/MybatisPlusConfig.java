package llm.model.modelservice.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO 使用MybatisPlus，添加分页查询功能

/**
 * MybatisPlus 分页查询配置类
 */
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 1. 初始化核心插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 2. 创建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 3. 设置分页参数
        paginationInnerInterceptor.setMaxLimit(1000L);
        // 4. 添加分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 5. 返回核心插件
        return interceptor;
    }
}
