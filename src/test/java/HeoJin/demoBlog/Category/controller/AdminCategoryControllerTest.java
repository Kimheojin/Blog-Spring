package HeoJin.demoBlog.Category.controller;


import HeoJin.demoBlog.category.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.category.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.configuration.base.SaveTestData;
import HeoJin.demoBlog.configuration.mockUser.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AdminCategoryControllerTest extends SaveTestData {

    // Post + /api/admin/categories -> 카테고리 추가
    @Test
    @WithMockCustomUser
    @DisplayName("post /api/admin/categories -> 카테고리 추가 정상 요청")
    void test1() throws Exception {
        // given
        final String categoryName = "테스트1";
        AddCategoryRequest request = AddCategoryRequest.builder()
                .categoryName(categoryName)
                .build();

        // when + then
        ResultActions testMock = mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("post-/api/admin/categories",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("categoryName").description("추가 카테고리 이름")
                ),
                responseFields(
                        fieldWithPath("categoryResponses").description("카테고리 목록"),
                        fieldWithPath("categoryResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("categoryResponses[].categoryName").description("저장된 카테고리 이름")
                )));
    }

    // Delete + api/admin/categories -> 카테고리 삭제

    @Test
    @WithMockCustomUser
    @DisplayName("delete /api/admin/categories -> 카테고리 삭제 정상 요청")
    void test2() throws Exception {
        // given
        final String categoryName = "테스트1";
        Category testCategory = Category.builder()
                .categoryName(categoryName)
                .build();

        categoryRepository.save(testCategory);

        DeleteCategoryRequest request = DeleteCategoryRequest.builder()
                .categoryId(categoryRepository.findByCategoryName(categoryName).get().getId())
                .categoryName(categoryName)
                .build();
        // when + then
        ResultActions testMock = mockMvc.perform(delete("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

        // docs
        testMock.andDo(document("delete-/api/admin/categories",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("categoryName").description("추가 카테고리 이름")
                ),
                responseFields(
                        fieldWithPath("categoryResponses").description("카테고리 목록"),
                        fieldWithPath("categoryResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("categoryResponses[].categoryName").description("저장된 카테고리 이름")
                )));

        categoryRepository.deleteAll();
    }



}