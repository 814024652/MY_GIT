package com.zhong.pms.config.serialization;

import com.gome.commons.serialization.FSTSerializationService;
import com.gome.commons.serialization.SerializationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangliewei
 * @date 2018/3/20 11:09
 * @opyright(c) gome inc Gome Co.,LTD
 */
@Configuration
public class SerializationConfig {
    @Bean
    SerializationService  serializationService(){
        SerializationService serializationService=new FSTSerializationService();
        return serializationService;
    }

}
