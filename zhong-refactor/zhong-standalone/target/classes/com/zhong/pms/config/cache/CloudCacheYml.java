package com.zhong.pms.config.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sso相关的配置信息
 * 
 * @author wangjie36@gome.com.cn
 * @date 2018/2/2
 * @copyright(c) gome inc Gome Co.,LTD
 */
@Component
@ConfigurationProperties(prefix = "cloudCache")
public class CloudCacheYml {
    /**
     * 授权码，Namespace申请成功后，会随当前申请成功Namespace与之对应的一个授权码。
     */
    private String authKey;

    /**
     * 隔离命名空间，每个业务通过设置Namespace来进行数据的隔离，防止各组之间的数据覆盖与删除。
     */
    private String nameSpace;

    /**
     * 登录接口相对地址
     */
    private String authLoginUri;

    /**
     * 登出接口相对地址
     */
    private String authLogoutUri;


    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
}
