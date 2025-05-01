package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.domain.Post;
import HeoJin.demoBlog.dto.request.CategoryRequest;
import HeoJin.demoBlog.repository.CategoryRepository;
import HeoJin.demoBlog.repository.MemberRepository;
import HeoJin.demoBlog.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].title").exists())
                .andExpect(jsonPath("$.posts[0].content").exists())
                .andExpect(jsonPath("$.posts[0].regDate").exists())
                .andDo(print());
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
        mockMvc.perform(get("/api/categoryPosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].title").exists())
                .andExpect(jsonPath("$.posts[0].content").exists())
                .andExpect(jsonPath("$.posts[0].regDate").exists())
                .andDo(print());
    }
}