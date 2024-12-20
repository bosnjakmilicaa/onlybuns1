package com.project.onlybuns.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiplePortsConfig {

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addAdditionalTomcatConnectors(createConnector(8082));  // Port 8082
        factory.addAdditionalTomcatConnectors(createConnector(8083));  // Port 8083
        return factory;
    }

    private org.apache.catalina.connector.Connector createConnector(int port) {
        org.apache.catalina.connector.Connector connector = new org.apache.catalina.connector.Connector("HTTP/1.1");
        connector.setPort(port);
        return connector;
    }
}
