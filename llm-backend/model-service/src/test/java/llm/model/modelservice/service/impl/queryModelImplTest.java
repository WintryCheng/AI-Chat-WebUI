package llm.model.modelservice.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Properties;

@SpringBootTest
class queryModelImplTest {

    @Value("${spring.cloud.nacos.serverAddr}")
    private String serverAddr;

    @Value("${spring.cloud.nacos.username}")
    private String nacosUsername;

    @Value("${spring.cloud.nacos.password}")
    private String nacosPassword;

    @Test
    void getIpAndPortByNacosServiceName() throws NacosException {
        // 设置Nacos的：服务器地址、用户名、密码
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("username", nacosUsername);
        properties.put("password", nacosPassword);

        // 创建NamingService对象
        NamingService namingService = NacosFactory.createNamingService(properties);

        // 获取所有实例
        List<Instance> instances = namingService.getAllInstances("fastapi-service");

        // 随机获取一个实例的IP和端口
        Instance instance = instances.get(RandomUtil.randomInt(instances.size()));

        // 打印实例的IP和端口
        System.out.println("IP: " + instance.getIp() + ", Port: " + instance.getPort());
    }
}

