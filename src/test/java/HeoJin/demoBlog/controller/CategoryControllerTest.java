package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.dto.request.DeleteCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Commit
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("/api/categoryList -> 요청")
    @WithMockUser
    void test1() throws Exception {
        mockMvc.perform(get("/api/categoryList"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("/api/category -> 권한 필요한 요청 필터단 exception 테스트")
    void test2() throws Exception {
        // given
        DeleteCategoryRequest categoryResponse = DeleteCategoryRequest.builder()
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
    @Test
    @WithMockCustomUser// 관리자 권한으로 테스트
    @DisplayName("카테고리 삭제 성공 테스트")
    void testDeleteCategorySuccess() throws Exception {
        // given
        DeleteCategoryRequest deleteRequest = DeleteCategoryRequest.builder()
                .categoryName("삭제할 카테고리")
                .build();

        // when + then
        mockMvc.perform(delete("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().isOk())  // 200 OK
                .andDo(print());
    }



/*
    @Test
    @DisplayName("/api/category -> 권한 필요한 요청 필터단 exception 테스트")
    @WithMockUser
    void test3() {
        //given

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // when

        String n ="1" ;

        //then
    }
*/

}
