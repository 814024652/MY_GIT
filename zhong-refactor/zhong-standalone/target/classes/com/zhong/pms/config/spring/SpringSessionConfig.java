package com.zhong.pms.config.spring;

import com.cloudcache.client.cluster.base.CloudCacheClientBean;
import com.gome.commons.serialization.SerializationService;
import com.zhong.pms.config.spring.session.CacheSessionRepositoryFactoryBean;
import com.zhong.pms.config.spring.session.SessionRepositoryFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.ExpiringSession;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.servlet.DispatcherType;

/**
 * @author zhangliewei
 * @date 2018/2/23 16:45
 * @opyright(c) gome inc Gome Co.,LTD
 */
@Configuration
@EnableSpringHttpSession
@PropertySource("classpath:cloud-cache.properties")
public class SpringSessionConfig {

    @Autowired
    SerializationService serializationService;
    @Autowired
    CloudCacheClientBean cloudCacheClientBean;
    @Value("${cache.config.expiring}")
    private Integer expiringTime;

    @Bean
    CacheSessionRepositoryFactoryBean cacheSessionRepositoryFactoryBean() {
        CacheSessionRepositoryFactoryBean cacheSessionRepositoryFactoryBean = new CacheSessionRepositoryFactoryBean();
        cacheSessionRepositoryFactoryBean.setCloudCacheClientBean(cloudCacheClientBean);
        cacheSessionRepositoryFactoryBean.setSerializationService(serializationService);
        cacheSessionRepositoryFactoryBean.setExpringTime(expiringTime);
        return cacheSessionRepositoryFactoryBean;
    }

    @Bean
    SessionRepositoryFilterFactoryBean sessionRepositoryFilterFactoryBean() {
        SessionRepositoryFilterFactoryBean sessionRepositoryFilterFactoryBean = new SessionRepositoryFilterFactoryBean();
        try {
            sessionRepositoryFilterFactoryBean.setSessionRepository(cacheSessionRepositoryFactoryBean().getObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionRepositoryFilterFactoryBean;
    }

    //配置session拦截
    @Bean
    public FilterRegistrationBean sessionRepositoryFilterDelegate() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        try {
            SessionRepositoryFilter<ExpiringSession> sessionSessionRepositoryFilter = sessionRepositoryFilterFactoryBean().getObject();
            registrationBean.setFilter(sessionSessionRepositoryFilter);
            registrationBean.setName("sessionSessionRepositoryFilter");
            registrationBean.addUrlPatterns("/*");
            registrationBean.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.FORWARD);
            registrationBean.setOrder(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registrationBean;
    }


}
