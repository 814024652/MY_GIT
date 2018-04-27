package com.zhong.pms.config.cache;

import com.cloudcache.client.cluster.base.CloudCacheClient;
import com.cloudcache.client.cluster.base.CloudCachePoolConfigure;
import com.cloudcache.client.cluster.common.PropertiesConst;
import com.cloudcache.client.cluster.factory.CloudCacheFactorySingle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by jiahua on 2017/6/7.
 */
@Configuration
@EnableCaching()
public class CloudCacheConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CloudCacheYml cloudCacheYml;

    @Bean
    public CloudCacheClient getRedis() {
        try {
            logger.info("reids缓存开始创建");
            Properties properties = new Properties();
            // namespace与authKey是接入分布式缓存系统的凭证，设置namespace，防止各组之间相同key的覆盖和删除。
            properties.put(PropertiesConst.Keys.NAMESPACE, cloudCacheYml.getNameSpace());
            // 设置authKey，它的第一个字母含义 0:只读，1:可读写，2:可持久化, 一个namespace可以对应多个authKey
            properties.put(PropertiesConst.Keys.AUTH_KEY, cloudCacheYml.getAuthKey());
            // 设置是否持久化，与持久化authKey配合使用，authKey与persistent==true同时设置才有效
            // 默认为不持久化。
            properties.put(PropertiesConst.Keys.PERSISTENT, true);
            // 创建连接池 CloudCache的链接池用的为Apache commons-pool2 具体参数配置请查阅官方文档
            CloudCachePoolConfigure cloudCachePoolConfigure = new CloudCachePoolConfigure();
            // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
            cloudCachePoolConfigure.setBlockWhenExhausted(true);
            // 是否启用后进先出, 默认true
            cloudCachePoolConfigure.setLifo(true);
            // 最大空闲连接数, 默认8个
            cloudCachePoolConfigure.setMaxIdle(32);
            // 最大连接数, 当前连接数的核数，连接数为当前核数的3-4倍性能最佳。

            //塞不确定的时间, // 默认-1
            cloudCachePoolConfigure.setMaxWaitMillis(1000*20);
            // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
            cloudCachePoolConfigure.setMinEvictableIdleTimeMillis(1800000);
            // 最小空闲连接数, 默认0
            cloudCachePoolConfigure.setMinIdle(8);
            // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
            cloudCachePoolConfigure.setTimeBetweenEvictionRunsMillis(-1);

            // 创建cloudCacheClient
            CloudCacheClient cloudCacheClient = CloudCacheFactorySingle.createCloudCacheClient(properties, cloudCachePoolConfigure);
            logger.info("reids创建成功");
            return cloudCacheClient;
        }catch (Exception e){
         logger.error("初始化REDIS失败",e);
        }
       return  null;
    }
}
