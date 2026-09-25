package com.example.config;

import com.example.notify.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@ComponentScan(basePackages="com.example")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    @Order(1)
    Notifier smsNotifier(){
        return new SmsNotifier();
    }

    @Bean
    @Order(2)
    Notifier pushNotifier(){
        return new PushNotifier();
    }

    @Bean
    @Order(3)
    Notifier EmailNotifier(){
        return new EmailNotifier();
    }
    @Bean
    NotifyManager notifyManager(List<Notifier> notifiers){
        return new NotifyManager(notifiers);
    }


}
