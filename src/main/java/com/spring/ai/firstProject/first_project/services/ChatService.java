package com.spring.ai.firstProject.first_project.services;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage ;

    @Value("classpath:/prompts/system-message.st")
    private Resource syetmMessage ;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chatTemplate() {

//        var systemPromptTemplate = SystemPromptTemplate.builder().template(this.syetmMessage).build();
//        var systemMessage = systemPromptTemplate.createMessage();
//
//        PromptTemplate userPromptTemplate = PromptTemplate.builder().template(this.userMessage).build();
//        var userMessage = userPromptTemplate.createMessage(Map.of(
//                "techName", "Spring",
//                "exampleName", "SpringBoot"));
//
//        Prompt prompt = new Prompt(systemMessage, userMessage);
//        return this.chatClient.prompt(prompt).call().content();



        // using Fluent api
        return  this. chatClient.prompt()
                  .system(system-> system.text(this.syetmMessage))
                  .user(user->user.text(this.userMessage).params(Map.of(
                          "techName", "Spring",
                          "exampleName", "SpringBoot")))
                  .call()
                  .content();



    }

}