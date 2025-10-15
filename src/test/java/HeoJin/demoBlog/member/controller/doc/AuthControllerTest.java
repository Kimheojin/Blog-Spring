package HeoJin.demoBlog.member.controller.doc;

import HeoJin.demoBlog.configuration.Integration.SaveTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends SaveTestData {

    @Test
    @DisplayName("GET /api/auth/status - 인증된 사용자 상태 확인")
    void test1() throws Exception {
        // given
        Authentication authentication = getMockAuthentication();

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/auth/status")
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication)) // 시큐리티 컨텍스트에 Authentication 객체 설정
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.memberId").value(1L))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andDo(print());

        // docs
        resultActions.andDo(document("get-auth-status-authenticated",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("authenticated").description("인증 여부 (true)"),
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("memberId").description("인증된 사용자의 ID"),
                        fieldWithPath("role").description("인증된 사용자의 권한")
                )));
    }

    @Test
    @DisplayName("GET /api/auth/status - 인증되지 않은 사용자 상태 확인")
    void test2() throws Exception {
        // given (인증 정보 없음)

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/auth/status")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.authenticated").value(false))
                .andDo(print());

        // docs
        resultActions.andDo(document("get-auth-status-unauthenticated",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("authenticated").description("인증 여부 (false)"),
                        fieldWithPath("message").description("응답 메시지")
                )));
    }

    private Authentication getMockAuthentication() {

        Long memberId = 1L;
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ADMIN");
        return new UsernamePasswordAuthenticationToken(memberId, null, authorities);
    }
}
