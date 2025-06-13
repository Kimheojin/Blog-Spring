package HeoJin.demoBlog.Post.controller;

import HeoJin.demoBlog.configuration.base.SaveTestData;
import HeoJin.demoBlog.configuration.mockUser.WithMockCustomUser;
import HeoJin.demoBlog.post.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminReadControllerTest extends SaveTestData {

    @BeforeEach
    void init() {
        saveFullTestData();
    }

    // get + /api/admin/posts -> 상태 상관 없이 전체 조회
    @Test
    @WithMockCustomUser
    @DisplayName("get /api/admin/posts -> 전체 글 + 조회된 post 수 반환")
    void test1() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/admin/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-/api/admin/posts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("content").description("포스트"),
                        fieldWithPath("content[].postId").description("포스트 Id"),
                        fieldWithPath("content[].title").description("제목"),
                        fieldWithPath("content[].memberName").description("작성자 이름"),
                        fieldWithPath("content[].content").description("내용"),
                        fieldWithPath("content[].categoryName").description("카테고리 이름"),
                        fieldWithPath("content[].status").description("enum + 상태 "),
                        fieldWithPath("content[].regDate").description("저장 날짜"),
                        fieldWithPath("pageNumber").description("페이지 넘버"),
                        fieldWithPath("pageSize").description("페이지 사이즈"),
                        fieldWithPath("totalElements").description("총 elements 갯수"),
                        fieldWithPath("totalPages").description("총 페이지"),
                        fieldWithPath("first").description("처음인지 아닌지"),
                        fieldWithPath("last").description("마지막인지 아닌지")
                )));
    }


    // get + /api/admin/posts/category -> 카테고리 별 반환
    @Test
    @WithMockCustomUser
    @DisplayName("get /api/admin/posts -> 카테고리 별 포스트수 반환(상태 상관 X)")
    void test2() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/admin/posts/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("categoryName", "Java1"))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-/api/admin/posts/category",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("content").description("포스트"),
                        fieldWithPath("content[].postId").description("포스트 Id"),
                        fieldWithPath("content[].title").description("제목"),
                        fieldWithPath("content[].memberName").description("작성자 이름"),
                        fieldWithPath("content[].content").description("내용"),
                        fieldWithPath("content[].categoryName").description("카테고리 이름"),
                        fieldWithPath("content[].status").description("enum + 상태 "),
                        fieldWithPath("content[].regDate").description("저장 날짜"),
                        fieldWithPath("pageNumber").description("페이지 넘버"),
                        fieldWithPath("pageSize").description("페이지 사이즈"),
                        fieldWithPath("totalElements").description("총 elements 갯수"),
                        fieldWithPath("totalPages").description("총 페이지"),
                        fieldWithPath("first").description("처음인지 아닌지"),
                        fieldWithPath("last").description("마지막인지 아닌지")
                )));
    }

    // get + /api/admin/posts/single -> 단일 포스트 조회
    @Test
    @WithMockCustomUser
    @DisplayName("get /api/admin/posts/single -> 단일 포스트 조회(상태 상관 X)")
    void test3() throws Exception {
        // given

        Long testPostId = postRepository.findAll().get(0).getId();

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/admin/posts/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("postId", String.valueOf(testPostId)))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-/api/admin/posts/single",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("postId").description("포스트 Id"),
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("memberName").description("작성자 이름"),
                        fieldWithPath("content").description("내용"),
                        fieldWithPath("categoryName").description("카테고리 이름"),
                        fieldWithPath("status").description("enum + 상태 "),
                        fieldWithPath("regDate").description("저장 날짜")

                )));
    }



    // get + /api/admin/statusPosts -> 포스트 상태 별 조회
    @Test
    @WithMockCustomUser
    @DisplayName("get /api/admin/statusPosts -> 카테고리 별 포스트수 반환(상태 상관 X)")
    void test4() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/admin/statusPosts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("postStatus", "PUBLISHED"))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-/api/admin/statusPosts",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("content").description("포스트"),
                        fieldWithPath("content[].postId").description("포스트 Id"),
                        fieldWithPath("content[].title").description("제목"),
                        fieldWithPath("content[].memberName").description("작성자 이름"),
                        fieldWithPath("content[].content").description("내용"),
                        fieldWithPath("content[].categoryName").description("카테고리 이름"),
                        fieldWithPath("content[].status").description("enum + 상태 "),
                        fieldWithPath("content[].regDate").description("저장 날짜"),
                        fieldWithPath("pageNumber").description("페이지 넘버"),
                        fieldWithPath("pageSize").description("페이지 사이즈"),
                        fieldWithPath("totalElements").description("총 elements 갯수"),
                        fieldWithPath("totalPages").description("총 페이지"),
                        fieldWithPath("first").description("처음인지 아닌지"),
                        fieldWithPath("last").description("마지막인지 아닌지")
                )));
    }
}