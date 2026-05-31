package com.spring.ai.firstProject.first_project;

import com.spring.ai.firstProject.first_project.helper.Helper;
import com.spring.ai.firstProject.first_project.services.ChatService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FirstProjectApplicationTests {

    @Autowired
    ChatService chatService;

    @Test
    void contextLoads() {
    }

    @Test
    void savetoVectorDB() {
        chatService.saveData(Helper.getData());

    }
}
