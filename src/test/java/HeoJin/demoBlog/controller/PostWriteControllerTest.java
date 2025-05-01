package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.PostRequest;
import HeoJin.demoBlog.dto.response.PostcontractionResponse;
import HeoJin.demoBlog.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostWriteControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private PostRequest postRequest;
    private PostcontractionResponse expectedResponse;

    @BeforeEach
    void init(){
        Category category = Category.builder()
                .categoryName("테스트 카테고리").build();
        categoryRepository.save(category);

        
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



    @Test
    @DisplayName("post /api/post -> 게시글 작성 정상 작동 테스트")
    @WithMockCustomUser
    void test1() throws Exception {

        mockMvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }


}