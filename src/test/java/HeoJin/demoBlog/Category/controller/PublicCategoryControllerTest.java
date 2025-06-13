package HeoJin.demoBlog.Category.controller;

import HeoJin.demoBlog.configuration.base.SaveTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PublicCategoryControllerTest extends SaveTestData {


    @BeforeEach
    void Init() {

        createTestMember();
        saveAllCategories();

    }
    @Test
    @DisplayName("get /api/categories -> 전체 카테고리 반환")
    void test1() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get-/api/categories",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("categoryResponses").description("카테고리 목록"),
                        fieldWithPath("categoryResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("categoryResponses[].categoryName").description("저장된 카테고리 이름")
                )));
    }
    @Test
    @DisplayName("get /api/categories/stats -> 카테고리 + 카테고리 별 Post 수도 추가")
    void test2() throws Exception {
        // given

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/categories/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("get -/api/categories/stats 카테고리 + 카테고리 별 post 수",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("CategoryWithCountResponses").description("카테고리 목록"),
                        fieldWithPath("CategoryWithCountResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("CategoryWithCountResponses[].categoryName").description("저장된 카테고리 이름"),
                        fieldWithPath("CategoryWithCountResponses[].postCount").description("해당 카테고리 Post 수")
                        
                )));
    }
}
