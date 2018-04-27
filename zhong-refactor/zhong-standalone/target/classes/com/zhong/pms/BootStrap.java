package com.zhong.pms;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableSwagger2Doc
@SpringCloudApplication
@MapperScan("com.zhong.pms.dao")
@EnableFeignClients
@EnableEurekaClient
public class BootStrap implements EmbeddedServletContainerCustomizer {
    private static int port = 8080;

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        // 为了解决同一台机器启动多个进程注册同一个eureka端口冲突问题, Modify: wangjie36@gome.com.cn, Since: 2018/4/2
        String[] args0 = new String[args.length + 1];
        System.arraycopy(args, 0, args0, 0, args.length);
        args0[args.length] = "--server.port=" + port;

        SpringApplication.run(BootStrap.class, args0);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(port);
    }
}
