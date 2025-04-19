package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.dto.response.CategoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("/api/categoryList 요청")
    void test1() throws Exception {
        mockMvc.perform(get("/api/categoryList"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("권한 필요한 요청 필터단 exception 테스트")
    void test2() throws Exception {
        // given
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .categoryName("test 카테고리 입니다.")
                .build();

        // when + then
        mockMvc.perform(delete("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryResponse)))
                .andExpect(status().isUnauthorized()) // 403
                .andExpect(jsonPath("$.message").value("인증이 필요합니다."))
                .andDo(print());
    }


}
