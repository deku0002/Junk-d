package com.example.junk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class JunkApplication {

    public static void main(String[] args) {
        SpringApplication.run(JunkApplication.class, args);
        System.out.println("🚀 Junk'd Backend API is running!");
        System.out.println("📱 Ready to analyze waste images and reward users!");
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();
    }
}