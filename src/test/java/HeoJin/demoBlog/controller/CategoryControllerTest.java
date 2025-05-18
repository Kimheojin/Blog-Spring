package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.WithMockCustomUser;
import org.junit.jupiter.api.Assertions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
//@Commit
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void init(WebApplicationContext webApplicationContext, RestDocumentationContextProvider RD){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(RD))
                .build();
    }

    @Test
    @Transactional
    @DisplayName("get /api/categoryList -> 정상 요청")
    void test6() throws Exception {
        // given

        List<Category> categories = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Category category = Category.builder()
                    .categoryName("test" + i)
                    .build();
            categories.add(category);
        }
        categoryRepository.saveAll(categories);

        // when + then

        ResultActions testMock = mockMvc.perform(get("/api/categoryList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryResponses", hasSize(5)))
                .andExpect(jsonPath("$.categoryResponses[0].categoryName").value("test1"))
                .andExpect(jsonPath("$.categoryResponses[1].categoryName").value("test2"))
                .andExpect(jsonPath("$.categoryResponses[2].categoryName").value("test3"))
                .andExpect(jsonPath("$.categoryResponses[3].categoryName").value("test4"))
                .andExpect(jsonPath("$.categoryResponses[4].categoryName").value("test5"))
                .andDo(print());

        // docs
        testMock.andDo(document("get-categoryList",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("categoryResponses").description("카테고리 목록"),
                        fieldWithPath("categoryResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("categoryResponses[].categoryName").description("카테고리 이름")
                )));

        categoryRepository.deleteAll();
    }


    @Test // 전체 카테고리 반환
    @DisplayName("get /api/categoryList -> 정상 요청")
    void test1() throws Exception {
        // when + then
        mockMvc.perform(get("/api/categoryList"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser// 관리자 권한
    @Transactional // 카테고리 삭제
    @DisplayName("delete /api/category -> 정상 삭제")
    void test4() throws Exception {
        // given
        Category category1 = Category.builder()
                .categoryName("삭제될 카테고리").build();
        DeleteCategoryRequest deleteRequest = DeleteCategoryRequest.builder()
                .categoryName(category1.getCategoryName())
                .build();

        Category category2 = Category.builder()
                .categoryName("남겨질 카테고리").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // when + then
        ResultActions testMock = mockMvc.perform(delete("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertTrue(
                categoryRepository.findByCategoryName("삭제될 카테고리").isEmpty());

        // docs

        testMock.andDo(document("delete-category",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("categoryName").description("삭제 될 카테고리")
                ),
                responseFields(
                        fieldWithPath("categoryResponses").description("카테고리 목록"),
                        fieldWithPath("categoryResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("categoryResponses[].categoryName").description("카테고리 이름")
                )));
    }

    @Test
    @Transactional
    @WithMockCustomUser
    @DisplayName("post /api/category -> 정상 + 카테고리 추가")
    void test7() throws Exception {
        // given
        AddCategoryRequest addCategoryRequest = AddCategoryRequest.builder()
                .categoryName("추카1").build();

        // when + then
        ResultActions testMock = mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryResponses", hasSize(1)))
                .andExpect(jsonPath("$.categoryResponses[0].categoryName").value("추카1"))
                .andDo(print());

        Optional<Category> savedCategory = categoryRepository.findByCategoryName("추카1");
        Assertions.assertNotNull(savedCategory);
        Assertions.assertEquals("추카1", savedCategory.get().getCategoryName());


        // docs

        testMock.andDo(document("post-category",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("categoryName").description("추가 될 카테고리 이름")
                ),
                responseFields(
                        fieldWithPath("categoryResponses").description("카테고리 목록"),
                        fieldWithPath("categoryResponses[].categoryId").description("카테고리 아이디"),
                        fieldWithPath("categoryResponses[].categoryName").description("카테고리 이름")
                )));

        categoryRepository.deleteAll();
    }

    
    // 이 코드 @ExtendWith(RestDocumentationExtension.class) 추가 후 예외에서 에러로 바뀜
//    @Test // 필터 단에서 걸려야 할듯
//    @DisplayName("delete /api/category -> 권한 없는 상태 exception 확인")
//    void test2() throws Exception {
//        // given
//        DeleteCategoryRequest DeleteRequest = DeleteCategoryRequest.builder()
//                .categoryName("test 카테고리 입니다.")
//                .build();
//
//        // when + then
//        mockMvc.perform(delete("/api/category")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(DeleteRequest)))
//                .andExpect(status().isUnauthorized()) // 401?
//                .andExpect(jsonPath("$.message")
//                        .value("인증이 필요합니다."))
//                .andDo(print());
//    }
    @Test
    @WithMockCustomUser// 관리자 권한
    @DisplayName("delete /api/category -> 삭제할 카테고리 존재 X exception")
    void test3() throws Exception {
        // given
        DeleteCategoryRequest deleteRequest = DeleteCategoryRequest.builder()
                .categoryName("삭제할 카테고리")
                .build();

        // when + then
        mockMvc.perform(delete("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isNotFound()) // 404
                .andExpect(jsonPath("$.message")
                        .value("존재하지 않는 entity 입니다. : 카테고리"))
                .andDo(print());
    }



    @Test
    @Transactional
    @DisplayName("get /api/categoryList -> 정상 + 빈 json 반환")
    void test5() throws Exception {
        // given

        // when + then
        mockMvc.perform(get("/api/categoryList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryResponses").isArray())
                .andExpect(jsonPath("$.categoryResponses").isEmpty())
                .andDo(print());
    }




    @Test
    @Transactional
    @WithMockCustomUser
    @DisplayName("post /api/category -> 중복된 카테고리 추가")
    void test8() throws Exception {
        // given
        Category category = Category.builder()
                .categoryName("추카1").build();
        AddCategoryRequest addCategoryRequest = AddCategoryRequest.builder()
                .categoryName(category.getCategoryName()).build();

        categoryRepository.save(category);

        // when + then
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategoryRequest)))
                .andExpect(status().isBadRequest()) // 400
                .andDo(print());

        // 원래 존재하던 거 확인

        Optional<Category> savedCategory = categoryRepository.findByCategoryName("추카1");
        Assertions.assertNotNull(savedCategory);
        Assertions.assertEquals("추카1", savedCategory.get().getCategoryName());

        categoryRepository.deleteAll();
    }
}