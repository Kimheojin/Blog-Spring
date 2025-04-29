package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.WithMockCustomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@Commit
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("get /api/categoryList : 정상 요청")
    void test1() throws Exception {
        mockMvc.perform(get("/api/categoryList"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test // 필터 단에서 걸려야 할듯
    @DisplayName("delete /api/category -> 권한 없는 상태 exception 확인")
    void test2() throws Exception {
        // given
        DeleteCategoryRequest DeleteRequest = DeleteCategoryRequest.builder()
                .categoryName("test 카테고리 입니다.")
                .build();

        // when + then
        mockMvc.perform(delete("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(DeleteRequest)))
                .andExpect(status().isUnauthorized()) // 401?
                .andExpect(jsonPath("$.message")
                        .value("인증이 필요합니다."))
                .andDo(print());
    }
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
    @WithMockCustomUser// 관리자 권한
    @Transactional
    @DisplayName("delete /api/category -> 정상 삭제")
    void test4() throws Exception {
        // given
        Category category = Category.builder()
                .categoryName("삭제될 카테고리").build();

        DeleteCategoryRequest deleteRequest = DeleteCategoryRequest.builder()
                .categoryName(category.getCategoryName())
                .build();

        categoryRepository.save(category);

        // when + then
        mockMvc.perform(delete("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertTrue(
                categoryRepository.findByCategoryName("삭제될 카테고리").isEmpty());
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
                .andExpect(content().json("[]")) // 이거 좋은 구조냐? 애매
                .andDo(print());
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

        mockMvc.perform(get("/api/categoryList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].categoryName").value("test1"))
                .andExpect(jsonPath("$[1].categoryName").value("test2"))
                .andExpect(jsonPath("$[2].categoryName").value("test3"))
                .andExpect(jsonPath("$[3].categoryName").value("test4"))
                .andExpect(jsonPath("$[4].categoryName").value("test5"))
                .andDo(print());

        categoryRepository.deleteAll();
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
        mockMvc.perform(post("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoryName").value("추카1"))
                .andDo(print());

        Optional<Category> savedCategory = categoryRepository.findByCategoryName("추카1");
        Assertions.assertNotNull(savedCategory);
        Assertions.assertEquals("추카1", savedCategory.get().getCategoryName());

        categoryRepository.deleteAll();
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
