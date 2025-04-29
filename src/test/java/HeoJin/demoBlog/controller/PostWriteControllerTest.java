package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.dto.request.PostRequest;
import HeoJin.demoBlog.dto.response.PostcontractionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
class PostWriteControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private PostRequest postRequest;
    private PostcontractionResponse expectedResponse;

    @BeforeEach
    void init(){
        
        // 공통
        postRequest = PostRequest.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .categoryName("테스트 카테고리")
                .build();
        
        expectedResponse = PostcontractionResponse.builder()
                .title("테스트 제목")
                .regDate(LocalDateTime.now())
                .build();
    }



    
    
    




}