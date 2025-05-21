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

        Category category = categoryRepository.save(Category.builder()
                .categoryName("테스트 카테고리")
                .build());

        Member member = memberRepository.findAll().get(0);

        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .regDate(LocalDateTime.now())
                .category(category)
                .member(member)
                .build();

        postRepository.save(post);
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    void getAllPostsTest() throws Exception {
        // given


        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/posts/paged"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].postId").exists())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].memberName").exists())
                .andExpect(jsonPath("$.content[0].content").exists())
                .andExpect(jsonPath("$.content[0].regDate").exists())
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andDo(print());


//         docs
        testMock.andDo(document("get-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("content").description("게시 글 목록"),
                        fieldWithPath("content[].postId").description("포스트 아이디"),
                        fieldWithPath("content[].title").description("포스트 이름"),
                        fieldWithPath("content[].memberName").description("작성자"),
                        fieldWithPath("content[].content").description("포스트 내용"),
                        fieldWithPath("content[].regDate").description("작성 날짜"),
                        fieldWithPath("pageNumber").description("현재 페이지 번호"),
                        fieldWithPath("pageSize").description("페이지 크기"),
                        fieldWithPath("totalElements").description("전체 게시글 수"),
                        fieldWithPath("totalPages").description("전체 페이지 수"),
                        fieldWithPath("first").description("첫 페이지 여부"),
                        fieldWithPath("last").description("마지막 페이지 여부")

                )));
    }

    @Test
    @DisplayName("카테고리별 게시글 조회 테스트")
    void getCategoryPostsTest() throws Exception {
        // given

        Category category = categoryRepository.findAll().get(0);




        String categoryName = category.getCategoryName();

        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/posts/categoryPaged")
                        .param("categoryName", categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].postId").exists())
                .andExpect(jsonPath("$.content[0].title").value("테스트 제목"))
                .andExpect(jsonPath("$.content[0].memberName").value("허진"))
                .andExpect(jsonPath("$.content[0].content").value("테스트 내용"))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andDo(print());

        // docs
        testMock.andDo(document("get-categoryPost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("content").description("게시 글 목록"),
                        fieldWithPath("content[].postId").description("포스트 아이디"),
                        fieldWithPath("content[].title").description("포스트 이름"),
                        fieldWithPath("content[].memberName").description("작성자"),
                        fieldWithPath("content[].content").description("포스트 내용"),
                        fieldWithPath("content[].regDate").description("작성 날짜"),
                        fieldWithPath("pageNumber").description("현재 페이지 번호"),
                        fieldWithPath("pageSize").description("페이지 크기"),
                        fieldWithPath("totalElements").description("전체 게시글 수"),
                        fieldWithPath("totalPages").description("전체 페이지 수"),
                        fieldWithPath("first").description("첫 페이지 여부"),
                        fieldWithPath("last").description("마지막 페이지 여부")
                )));
    }
}