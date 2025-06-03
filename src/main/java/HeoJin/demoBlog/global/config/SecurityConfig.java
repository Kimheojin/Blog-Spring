package HeoJin.demoBlog.global.config;

import HeoJin.demoBlog.global.filter.CustomAuthenticationFilter;
import HeoJin.demoBlog.member.service.CustomUserDetailService;
import HeoJin.demoBlog.global.util.CustomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashMap;
import java.util.Map;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   ObjectMapper objectMapper,
                                                   AuthenticationManager authenticationManager) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // 세션 고정 공격 방지
                        .sessionFixation().changeSessionId()
                        .maximumSessions(1))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/categories", "/api/categories/stats",
                                "/api/posts/*/comments", "/api/posts/comments", "/api/comments",
                                "/api/auth/", "/api/posts", "/api/posts/single", "/api/posts/category").permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(
                        new CustomAuthenticationFilter(objectMapper, authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                )

                .exceptionHandling(ex -> ex.
                        authenticationEntryPoint((request, response, authException) -> {
                            int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
                            response.setStatus(statusCode);

                            Map<String, Object> errorResponse = new HashMap<>();
                            errorResponse.put("message", "인증이 필요합니다.");
                            errorResponse.put("statusCode", statusCode);
                            // 에러 코드
                            errorResponse.put("code", "401");

                            CustomUtil.setUTF(response).getWriter().write(objectMapper.writeValueAsString(errorResponse));
                        }))
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "POST")) // POST 메서드로 제한
                        .logoutSuccessHandler((request, response, authentication) ->
                        {
                            int statusCode = HttpServletResponse.SC_OK;
                            response.setStatus(statusCode); // 200
                            Map<String, Object> successResponse = new HashMap<>();
                            successResponse.put("message", "로그아웃 되었습니다.");
                            successResponse.put("statusCode", statusCode);

                            CustomUtil.setUTF(response).getWriter().write(objectMapper.writeValueAsString(successResponse));

                        }).invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}