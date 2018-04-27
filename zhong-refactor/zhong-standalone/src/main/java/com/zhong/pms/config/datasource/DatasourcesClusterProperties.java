package com.zhong.pms.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
* @Description
* @Author zhangliewei
* @Date 2017/3/24
* @Copyright(c) gome inc Gome Co.,LTD
*/
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@PropertySource("classpath:/db.properties")
public class DatasourcesClusterProperties {

    List<GomeDruidDataSource> nodes;

    public List<GomeDruidDataSource> getNodes() {
        return nodes;
    }

    public void setNodes(List<GomeDruidDataSource> nodes) {
        this.nodes = nodes;
    }
}
