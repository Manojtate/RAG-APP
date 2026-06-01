package com.spring.ai.firstProject.first_project.config;

import com.spring.ai.firstProject.first_project.advisors.TokenPrintAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    private Logger logger = LoggerFactory.getLogger(AiConfig.class);

    @Bean
    public ChatMemory chatMemory() {

        InMemoryChatMemoryRepository inMemoryChatMemoryRepository = new InMemoryChatMemoryRepository();
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(inMemoryChatMemoryRepository)
                .maxMessages(10)
                .build();
    }


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {

        this.logger.info("ChatMemoryImplementation class : " + chatMemory.getClass().getName());

        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return builder
                .defaultAdvisors(messageChatMemoryAdvisor, new TokenPrintAdvisor(), new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("Sensitive")))
                .defaultOptions(
                        ChatOptions.builder()
                                .temperature(0.3)
                                .maxTokens(2000)
                )
                .build();
    }
}