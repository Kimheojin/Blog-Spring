package HeoJin.demoBlog.member.controller;

import HeoJin.demoBlog.configuration.Integration.SaveTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AuthControllerTest extends SaveTestData {



    @Test
    @DisplayName("get /api/auth/session -> session 존재 확인")
    void test1() throws Exception {
        // given - 실제 세션 생성

        //WithMockCustomUser는 실제 session을 안만드는듯..?
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", "mock_security_context");

        // when + then
        ResultActions testMock = mockMvc.perform(get("/api/auth/session")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // docs
        testMock.andDo(document("get-api-auth-session-authenticated",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("authenticated").description("인증 상태"),
                        fieldWithPath("message").description("인증 상태 메시지")
                )));
    }


}
