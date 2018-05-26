package cn.com.fhz.websocket.config;

import org.springframework.context.annotation.Configuration;

/**
 * Created by woni on 18/5/25.
 */
@Configuration
public class MyWebSocketConfig {
    //因为是war包放入tomcat，所以注释掉
//    @Bean
//    public ServerEndpointExporter serverEndpointExporter(){
//        return new ServerEndpointExporter();
//    }
}
