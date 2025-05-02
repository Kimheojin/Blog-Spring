package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.domain.Post;
import HeoJin.demoBlog.dto.request.CategoryRequest;
import HeoJin.demoBlog.repository.CategoryRepository;
import HeoJin.demoBlog.repository.MemberRepository;
import HeoJin.demoBlog.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(RestDocumentationExtension.class)
public class PostReadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init(WebApplicationContext webApplicationContext, RestDocumentationContextProvider RD){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(RD))
                .build();
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    void getAllPostsTest() throws Exception {
        // given
        Category category = categoryRepository.save(Category.builder()
                .categoryName("테스트 카테고리")
                .build());

        Member member = memberRepository.findAll().get(0); // 초기 init 데이터 사용

        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .regDate(LocalDateTime.now())
                .category(category)
                .member(member)
                .build();
        postRepository.save(post);

        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].title").exists())
                .andExpect(jsonPath("$.posts[0].content").exists())
                .andExpect(jsonPath("$.posts[0].regDate").exists())
                .andDo(print());


        // docs


        testMock.andDo(document("get-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("posts").description("게시 글 목록"),
                        fieldWithPath("posts[].title").description("포스트 이름"),
                        fieldWithPath("posts[].memberName").description("작성자"),
                        fieldWithPath("posts[].content").description("포스트 내용"),
                        fieldWithPath("posts[].regDate").description("작성 날짜")
                )));
    }

    @Test
    @DisplayName("카테고리별 게시글 조회 테스트")
    void getCategoryPostsTest() throws Exception {
        // given
        String categoryName = "테스트 카테고리";

        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .categoryName(categoryName)
                        .build()));

        Member member = memberRepository.findAll().get(0);

        Post post = Post.builder()
                .title("카테고리별 테스트 제목")
                .content("카테고리별 테스트 내용")
                .regDate(LocalDateTime.now())
                .category(category)
                .member(member)
                .build();
        postRepository.save(post);

        CategoryRequest categoryRequest = CategoryRequest.builder()
                .categoryName(categoryName)
                .build();

        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/categoryPosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].title").exists())
                .andExpect(jsonPath("$.posts[0].content").exists())
                .andExpect(jsonPath("$.posts[0].regDate").exists())
                .andDo(print());

        // docs
        testMock.andDo(document("get-categoryPost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("categoryName").description("원하는 카테고리")
                ),
                responseFields(
                        fieldWithPath("posts").description("게시 글 목록"),
                        fieldWithPath("posts[].title").description("포스트 이름"),
                        fieldWithPath("posts[].memberName").description("작성자"),
                        fieldWithPath("posts[].content").description("포스트 내용"),
                        fieldWithPath("posts[].regDate").description("작성 날짜")
                )));
    }
}