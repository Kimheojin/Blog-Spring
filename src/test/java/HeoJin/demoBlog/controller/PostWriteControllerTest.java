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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class)
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
    void init(WebApplicationContext webApplicationContext,
              RestDocumentationContextProvider restDocumentation){
        // rest docs 관련
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();


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

        // when + then

        ResultActions testmock = mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        // restdocs

        testmock.andDo(document("post-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("title").description("게시글 제목").type(JsonFieldType.STRING),
                        fieldWithPath("content").description("게시글 내용").type(JsonFieldType.STRING),
                        fieldWithPath("categoryName").description("카테고리 이름").type(JsonFieldType.STRING)
                ),
                responseFields(
                        fieldWithPath("title").description("게시글 제목").type(JsonFieldType.STRING),
                        fieldWithPath("regDate").description("등록 일자").type(JsonFieldType.STRING)
                        // 실제 응답에 따라 필드 추가
                )
        ));
    }


}