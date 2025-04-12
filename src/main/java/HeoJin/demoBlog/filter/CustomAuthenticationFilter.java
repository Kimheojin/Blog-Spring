package HeoJin.demoBlog.filter;

import HeoJin.demoBlog.dto.request.LoginDto;
import HeoJin.demoBlog.util.CustomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        if (!("/api/login".equals(request.getServletPath()) && "POST".equals(request.getMethod()))){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 로그인 부분

            LoginDto loginDto = objectMapper.readValue(
                    request.getInputStream(), LoginDto.class
            );

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            response.setStatus(HttpServletResponse.SC_OK);


            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "로그인 성공");
            successResponse.put("statusCode", HttpServletResponse.SC_OK);

            // 세션값 반환
            // 헤더랑 쿠키 둘다 오는 듯
            HttpSession session = request.getSession();
            successResponse.put("sessionId", session.getId());
            

            CustomUtil.setUTF(response).getWriter().write(objectMapper.writeValueAsString(successResponse));
        } catch (AuthenticationException e) {
            // 인증 실패 시 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "로그인 실패: " + e.getMessage());
            errorResponse.put("statusCode", HttpServletResponse.SC_UNAUTHORIZED);

            CustomUtil.setUTF(response).getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

}
