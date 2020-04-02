package org.v.th.collector.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

@Configuration
@ConditionalOnClass(RestTemplate.class)
public class RestTemplateConfig {

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory(CollectorProperties properties) {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(properties.getRest().getConnectionTimeout());
        httpRequestFactory.setReadTimeout(properties.getRest().getReadTimeout());
        if (properties.getRest().getProxy().isActive()) {
            SocketAddress address = new InetSocketAddress(
                    properties.getRest().getProxy().getHost(), properties.getRest().getProxy().getPort());
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            httpRequestFactory.setProxy(proxy);
        }
        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory simpleClientHttpRequestFactory) {
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

}
