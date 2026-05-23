package com.spring.ai.firstProject.first_project.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChatController {


    private ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
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


}
