package com.zhong.pms.config.spring.session;

import com.cloudcache.client.cluster.base.CloudCacheClient;
import com.cloudcache.client.cluster.base.CloudCacheClientBean;
import com.gome.commons.serialization.SerializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;

/**
 * @author zhangliewei
 * @date 2018/3/8 16:38
 * @opyright(c) gome inc Gome Co.,LTD
 */
public class CacheSessionRepository implements SessionRepository<ExpiringSession> {
    private Logger logger= LoggerFactory.getLogger(CacheSessionRepository.class);

    private CloudCacheClientBean cloudCacheClientBean;
    private SerializationService serializationService;

    private Integer defaultMaxInactiveInterval;
    private Integer expiringTime=1800;

    public CacheSessionRepository(CloudCacheClientBean cloudCacheClientBean, SerializationService serializationService,Integer expiringTime) {
        this.cloudCacheClientBean = cloudCacheClientBean;
        this.serializationService = serializationService;
        this.expiringTime=expiringTime;
    }


    @Override
    public ExpiringSession createSession() {
        ExpiringSession session = new MapSession();
        if (this.defaultMaxInactiveInterval != null) {
            session.setMaxInactiveIntervalInSeconds(this.defaultMaxInactiveInterval.intValue());
        }
        logger.debug("create session:{}",session.getId());
        return session;
    }

    @Override
    public void save(ExpiringSession expiringSession) {
        logger.debug("save session:{}",expiringSession.getId());
        CloudCacheClient cloudCacheClient = cloudCacheClientBean.cloudCacheClient;
        cloudCacheClient.setex(expiringSession.getId().getBytes(), serializationService.serialize(expiringSession),expiringTime);
    }

    @Override
    public ExpiringSession getSession(String id) {
        logger.debug("get session by id:{}",id);
        CloudCacheClient cloudCacheClient = cloudCacheClientBean.cloudCacheClient;
        byte[] bytes=cloudCacheClient.get(id.getBytes());
        if (bytes==null){
            return null;
        }
        ExpiringSession saved = serializationService.deserialize(bytes, ExpiringSession.class);
         if (saved.isExpired()) {
            this.delete(saved.getId());
            return null;
        } else {
            return new MapSession(saved);
        }

    }

    @Override
    public void delete(String id) {
        logger.debug("delete session by id:{}",id);
        CloudCacheClient cloudCacheClient = cloudCacheClientBean.cloudCacheClient;
        cloudCacheClient.del(id.getBytes());
    }

    public Integer getDefaultMaxInactiveInterval() {
        return defaultMaxInactiveInterval;
    }

    public void setDefaultMaxInactiveInterval(Integer defaultMaxInactiveInterval) {
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    public Integer getExpiringTime() {
        return expiringTime;
    }

    public void setExpiringTime(Integer expiringTime) {
        this.expiringTime = expiringTime;
    }
}
