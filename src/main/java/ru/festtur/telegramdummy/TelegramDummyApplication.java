package ru.festtur.telegramdummy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class TelegramDummyApplication {
    void main(String[] args) {
        SpringApplication.run(TelegramDummyApplication.class, args);
    }
}
