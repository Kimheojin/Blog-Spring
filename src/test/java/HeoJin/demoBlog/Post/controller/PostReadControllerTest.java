package HeoJin.demoBlog.Post.controller;

import HeoJin.demoBlog.configuration.base.SaveTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostReadControllerTest extends SaveTestData {

    @BeforeEach
    void init() {
        saveFullTestData();
    }


    @Test
    @DisplayName("get /api/posts -> 전체 글 + 조회된 post 수 반환")
    void test1() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-/api/posts",
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
    @Test
    @DisplayName("get /api/posts/category -> 카테고리 별 반환 + 조회 글 수 반환")
    void test2() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/posts/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("categoryName", "Java1"))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-api-posts-category",
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

    @Test
    @DisplayName("get /api/posts/single -> 단일 포스트 조회")
    void test3() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/posts/single")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("postId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-api-posts-single",
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


}