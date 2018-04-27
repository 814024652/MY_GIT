package com.zhong.pms.config.spring.session;

import com.cloudcache.client.cluster.base.CloudCacheClientBean;
import com.gome.commons.serialization.SerializationService;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author zhangliewei
 * @date 2018/3/8 16:56
 * @opyright(c) gome inc Gome Co.,LTD
 */
public class CacheSessionRepositoryFactoryBean implements FactoryBean<CacheSessionRepository> {

    private CloudCacheClientBean cloudCacheClientBean;
    private SerializationService serializationService;
    private Integer expringTime;


    @Override
    public CacheSessionRepository getObject() throws Exception {
        CacheSessionRepository cacheSessionRepository = null;
        if (null != cloudCacheClientBean&&serializationService!=null) {
            cacheSessionRepository=new CacheSessionRepository(cloudCacheClientBean,serializationService,expringTime);
            return  cacheSessionRepository;
        }else{
            throw new RuntimeException("cache cannot be shared because cacheSessionRepository or serializationService is null");
        }
    }

    @Override
    public Class<?> getObjectType() {
        return CacheSessionRepository.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCloudCacheClientBean(CloudCacheClientBean cloudCacheClientBean) {
        this.cloudCacheClientBean = cloudCacheClientBean;
    }

    public void setSerializationService(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    public void setExpringTime(Integer expringTime) {
        this.expringTime = expringTime;
    }
}
