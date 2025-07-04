package HeoJin.demoBlog.global.filter;

import HeoJin.demoBlog.global.util.CustomUtil;
import HeoJin.demoBlog.member.dto.request.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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


        // 경로 확인 방식 추가
        String path = request.getServletPath();
        if (!path.equals("/api/auth/login") || !"POST".equals(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // 로그인 로직 부분

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


            // 세션에 저장
            SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);


            response.setStatus(HttpServletResponse.SC_OK);


            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("message", "로그인 성공");
            successResponse.put("statusCode", HttpServletResponse.SC_OK);


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
