package com.spring.ai.firstProject.first_project.config;

import com.spring.ai.firstProject.first_project.advisors.TokenPrintAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {

        return builder
                .defaultAdvisors(new TokenPrintAdvisor(),new SimpleLoggerAdvisor() ,new SafeGuardAdvisor(List.of("Sensitive")))
                .defaultOptions(
                        ChatOptions.builder()
                                .temperature(0.3)
                                .maxTokens(2000)
                )
                .build();
    }
}