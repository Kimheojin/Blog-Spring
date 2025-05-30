package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.member.respository.MemberRepository;
import HeoJin.demoBlog.post.repository.PostRepository;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
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

    private Post savedPost;
    private Category savedCategory;

    @BeforeEach
    void init(WebApplicationContext webApplicationContext, RestDocumentationContextProvider RD) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(RD))
                .build();

        savedCategory = categoryRepository.save(Category.builder()
                .categoryName("테스트 카테고리")
                .build());

        Member member = memberRepository.findAll().get(0);

        savedPost = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .regDate(LocalDateTime.now())
                .category(savedCategory)
                .member(member)
                .build();

        savedPost = postRepository.save(savedPost);
    }

    @Test
    @DisplayName("전체 게시글 조회 테스트")
    void getAllPostsTest() throws Exception {
        // given

        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/posts/paged")
                        .param("page", "0")           // 명시적으로 전달
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].postId").exists())
                .andExpect(jsonPath("$.content[0].title").value("테스트 제목"))
                .andExpect(jsonPath("$.content[0].memberName").value("허진"))
                .andExpect(jsonPath("$.content[0].content").value("테스트 내용"))
                .andExpect(jsonPath("$.content[0].categoryName").value("테스트 카테고리"))
                .andExpect(jsonPath("$.content[0].regDate").exists())
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andDo(print());

//         docs
        testMock.andDo(document("get-posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("page").description("페이지 번호 (기본값: 0)"),
                        parameterWithName("size").description("페이지 크기 (기본값: 10)")
                ),
                responseFields(
                        fieldWithPath("content").description("게시 글 목록"),
                        fieldWithPath("content[].postId").description("포스트 아이디"),
                        fieldWithPath("content[].title").description("포스트 이름"),
                        fieldWithPath("content[].memberName").description("작성자"),
                        fieldWithPath("content[].content").description("포스트 내용"),
                        fieldWithPath("content[].categoryName").description("카테고리 이름"),
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
        String categoryName = savedCategory.getCategoryName();

        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/posts/categoryPaged")
                        .param("categoryName", categoryName)
                        .param("page", "0")           // 명시적으로 전달
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].postId").exists())
                .andExpect(jsonPath("$.content[0].title").value("테스트 제목"))
                .andExpect(jsonPath("$.content[0].memberName").value("허진"))
                .andExpect(jsonPath("$.content[0].content").value("테스트 내용"))
                .andExpect(jsonPath("$.content[0].categoryName").value("테스트 카테고리"))
                .andExpect(jsonPath("$.content[0].regDate").exists())
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andDo(print());

        // docs
        testMock.andDo(document("get-categoryPost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("categoryName").description("조회할 카테고리 이름"),
                        parameterWithName("page").description("페이지 번호 (기본값: 0)"),
                        parameterWithName("size").description("페이지 크기 (기본값: 10)")
                ),
                responseFields(
                        fieldWithPath("content").description("게시 글 목록"),
                        fieldWithPath("content[].postId").description("포스트 아이디"),
                        fieldWithPath("content[].title").description("포스트 이름"),
                        fieldWithPath("content[].memberName").description("작성자"),
                        fieldWithPath("content[].content").description("포스트 내용"),
                        fieldWithPath("content[].categoryName").description("카테고리 이름"),
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
    @DisplayName("단일 게시글 조회 테스트")
    void getSinglePostTest() throws Exception {
        // given
        String postId = savedPost.getId().toString();

        // when & then
        ResultActions testMock = mockMvc.perform(get("/api/posts")
                        .param("postId", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(savedPost.getId()))
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.memberName").value("허진"))
                .andExpect(jsonPath("$.content").value("테스트 내용"))
                .andExpect(jsonPath("$.categoryName").value("테스트 카테고리"))
                .andExpect(jsonPath("$.regDate").exists())
                .andDo(print());

        // docs
        testMock.andDo(document("get-single-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("postId").description("조회할 포스트 ID")
                ),
                responseFields(
                        fieldWithPath("postId").description("포스트 아이디"),
                        fieldWithPath("title").description("포스트 제목"),
                        fieldWithPath("memberName").description("작성자"),
                        fieldWithPath("content").description("포스트 내용"),
                        fieldWithPath("categoryName").description("카테고리 이름"),
                        fieldWithPath("regDate").description("작성 날짜")
                )));
    }

}