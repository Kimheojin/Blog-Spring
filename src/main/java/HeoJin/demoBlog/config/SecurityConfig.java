package HeoJin.demoBlog.config;

import HeoJin.demoBlog.filter.CustomAuthenticationFilter;
import HeoJin.demoBlog.service.CustomUserDetailService;
import HeoJin.demoBlog.util.CustomUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

import java.util.HashMap;
import java.util.Map;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final CorsFilter corsFilter;


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
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/categoryList", "/api/posts",
                                "/api/categoryPosts").permitAll()
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