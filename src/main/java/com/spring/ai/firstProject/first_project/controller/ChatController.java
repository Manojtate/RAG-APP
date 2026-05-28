package com.spring.ai.firstProject.first_project.controller;

import com.spring.ai.firstProject.first_project.services.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class ChatController {

    @Autowired
    private ChatService chatService;

    private ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q") String query) {
        var ResponseContent = this.chatClient.prompt(query).call().content();
        return ResponseEntity.ok(ResponseContent);
    }

    @GetMapping("/user/chat")
    public ResponseEntity<String> useUserPrompt(
            @RequestParam(value = "q") String query) {
        String strQuery = "As an expert in coding always write code in java .Now replay for this question :{query}";
        var ResponseContent = this.chatClient.
                prompt().user(u -> u.text(strQuery).param("query", query))
                .call().content();

        return ResponseEntity.ok(ResponseContent);
    }


    @GetMapping("/user/stream-chat")
    public ResponseEntity<Flux<String>> streamChat(
            @RequestParam(value = "q") String query) {

        return ResponseEntity.ok(this.chatService.streamChat(query));
    }

    @GetMapping("/chatMemory")
    public ResponseEntity<String> ChatMemory(
            @RequestParam("q") String query,
            @RequestParam("conversationId") String conversationId) {

        var responseContent = this.chatClient
                .prompt(query)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID,
                        conversationId))
                .call()
                .content();

        return ResponseEntity.ok(responseContent);
    }
}
