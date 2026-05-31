package com.spring.ai.firstProject.first_project.services;

import org.springframework.ai.document.Document;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource syetmMessage;

    public ChatService(ChatClient chatClient , VectorStore vectorStore) {

        this.chatClient = chatClient;
        this.vectorStore=vectorStore;
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
        return this.chatClient.prompt()
                .system(system -> system.text(this.syetmMessage))
                .user(user -> user.text(this.userMessage).params(Map.of(
                        "techName", "Spring",
                        "exampleName", "SpringBoot")))
                .call()
                .content();


    }

    public Flux<String> streamChat(String query) {

        return this.chatClient.prompt()
                .system(system -> system.text(this.syetmMessage))
                .user(user -> user.text(this.userMessage).params(Map.of(
                        "techName", "Spring",
                        "exampleName", "SpringBoot")).param("concept", query))
                .stream()
                .content();


    }

    public  void saveData(List<String> list)
    {
        List<Document>documentList = list.stream().map(item -> new Document(item)).collect(Collectors.toList());
        this.vectorStore.add(documentList);
        System.out.println("data is saved successfully");
    }


}