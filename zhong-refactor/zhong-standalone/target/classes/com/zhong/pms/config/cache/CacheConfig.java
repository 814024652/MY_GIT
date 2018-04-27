package com.zhong.pms.config.cache;

import com.cloudcache.client.cluster.base.CloudCacheClientBean;
import com.cloudcache.client.cluster.base.CloudCachePoolConfigure;
import com.cloudcache.client.cluster.common.PropertiesConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

/**
 * @author zhangliewei
 * @date 2018/3/8 15:42
 * @opyright(c) gome inc Gome Co.,LTD
 */
@Configuration
@PropertySource("classpath:cloud-cache.properties")
public class CacheConfig {

    @Value("${cache.namespace}")
    private  String namespace;

    @Value("${cache.authKey}")
    private  String authKey;

    @Bean
    @ConfigurationProperties(prefix = "cache.config")
    CloudCachePoolConfigure cloudCachePoolConfigure(){
        CloudCachePoolConfigure configure=new CloudCachePoolConfigure();
        return configure;
    }


    @Bean
    CloudCacheClientBean cloudCacheClientBean() {
        CloudCacheClientBean cloudCacheClientBean = new CloudCacheClientBean();
        Properties properties = new Properties();
        properties.put(PropertiesConst.Keys.NAMESPACE,namespace);
        properties.put(PropertiesConst.Keys.AUTH_KEY,authKey);
        cloudCacheClientBean.setProperties(properties);
        cloudCacheClientBean.setCloudCachePoolConfigure(cloudCachePoolConfigure());
        cloudCacheClientBean.createCloudCacheClient();
        return cloudCacheClientBean;
    }


}
