package com.zhong.pms.config.spring.session;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * @author zhangliewei
 * @date 2018/3/8 16:53
 * @opyright(c) gome inc Gome Co.,LTD
 */
public class SessionRepositoryFilterFactoryBean implements FactoryBean<SessionRepositoryFilter<ExpiringSession>> {

    private SessionRepository<ExpiringSession> sessionRepository = new MapSessionRepository();

    public void setSessionRepository(SessionRepository<ExpiringSession> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionRepositoryFilter<ExpiringSession> getObject() throws Exception {
        return new SessionRepositoryFilter<ExpiringSession>(sessionRepository);
    }

    @Override
    public Class<?> getObjectType() {
        return SessionRepositoryFilter.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
