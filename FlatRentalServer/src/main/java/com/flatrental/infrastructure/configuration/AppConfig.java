package com.flatrental.infrastructure.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tempuri.ITerytWs1;
import org.tempuri.TerytWs1;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.AddressingFeature;
import java.util.Map;

@Lazy
@Configuration
@EnableScheduling
@EnableCaching
public class AppConfig {

    @Bean
    public ITerytWs1 getTerytClient() {
        ITerytWs1 terytClient = new TerytWs1().getCustom(new AddressingFeature(true));
        Map<String, Object> requestContext = ((BindingProvider) terytClient).getRequestContext();
        requestContext.put("ws-security.username", "TestPubliczny");
        requestContext.put("ws-security.password", "1234abcd");
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://uslugaterytws1test.stat.gov.pl/TerytWs1.svc");
        return terytClient;
    }

}
