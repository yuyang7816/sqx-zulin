package com.sqx.system.chats;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket的配置
 */
@Configuration
public class WebSocketStompConfig{
    //这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }
}
