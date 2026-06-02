package com.spring.ai.firstProject.first_project.services;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import com.spring.ai.firstProject.first_project.advisors.TokenPrintAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private Logger logger = LoggerFactory.getLogger(TokenPrintAdvisor.class);

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


    public  void saveData(List<String> list)
    {
        List<Document>documentList = list.stream().map(item -> new Document(item)).collect(Collectors.toList());
        this.vectorStore.add(documentList);
        System.out.println("data is saved successfully");
    }

    public String vectorDBSearch(String query, String conversationId ) {

       SearchRequest searchRequest= SearchRequest.
               builder().
               topK(3).
               similarityThreshold(0.6)
               .query(query)
               .build();
        List<Document> documentList = this.vectorStore.similaritySearch(searchRequest);

          List<String> StringList =documentList.stream().map(Document::getText).toList();
          String contextData = String.join(",",StringList);

        logger.info("Contex Data :{}",contextData);

        return this.chatClient.prompt()
                .system(system -> system.text(this.syetmMessage).param("documents",contextData))
                .user(user-> user.text(this.userMessage).param("query",query))
                .advisors(advisorSpec ->
                advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();


    }


    public String searchvectorDBAutomaticSearchQuestionAnswerAdvisor(String query, String conversationId) {


    //    QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore).build();
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor
                .builder(vectorStore)
                .searchRequest(
                        SearchRequest
                                .builder()
                                .topK(3)
                                .similarityThreshold(0.5)
                                .build()

                               )
                .build();

        return this.chatClient.prompt()

                .user(user ->
                        user.text(this.userMessage)
                                .param("query", query))

                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(advisor)

                .call()
                .content();

    }

    public String vectorDBAutomaticSearchRetrievalAugmentationAdvisor(String query, String conversationId) {

      var advisor =  RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever
                        .builder()
                        .vectorStore(this.vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build())
               .queryAugmenter(ContextualQueryAugmenter.builder().allowEmptyContext(true).build())
                .build();


        return this.chatClient.prompt()

                .user(user ->
                        user.text(this.userMessage)
                                .param("query", query))

                .advisors(advisorSpec ->
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .advisors(advisor)

                .call()
                .content();



    }
}