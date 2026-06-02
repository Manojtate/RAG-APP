package com.spring.ai.firstProject.first_project.controller;

import com.spring.ai.firstProject.first_project.services.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ChatController {

    @Autowired
    private ChatService chatService;

    private ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping("/user/vectorDB/search")
    public ResponseEntity<String> vectorDBSearch(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam("conversationId") String conversationId) {

        return ResponseEntity.ok(this.chatService.vectorDBSearch(query, conversationId));
    }

    @GetMapping("/user/vectorDBAutomaticSearch/search")
    public ResponseEntity<String>searchvectorDBAutomaticSearchQuestionAnswerAdvisor(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam("conversationId") String conversationId) {

        return ResponseEntity.ok(this.chatService.searchvectorDBAutomaticSearchQuestionAnswerAdvisor(query, conversationId));
    }

    @GetMapping("/user/vectorDBAutomaticSearch/Retrieval/search")
    public ResponseEntity<String> vectorDBAutomaticSearchRetrievalAugmentationAdvisor(
            @RequestParam(value = "q", required = true) String query,
            @RequestParam("conversationId") String conversationId) {

        return ResponseEntity.ok(this.chatService.vectorDBAutomaticSearchRetrievalAugmentationAdvisor(query, conversationId));
    }

}
