package HeoJin.demoBlog.member.controller;


import HeoJin.demoBlog.member.dto.request.LoginDto;
import HeoJin.demoBlog.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response) {

        Map<String, Object> responseBody = new HashMap<>();

        try {
            authService.login(loginDto, request, response);

            responseBody.put("message", "로그인 성공");
            responseBody.put("statusCode", HttpStatus.OK.value());

            return ResponseEntity.ok(responseBody);

        } catch (AuthenticationException e) {
            responseBody.put("message", "로그인 실패: " + e.getMessage());
            responseBody.put("statusCode", HttpStatus.UNAUTHORIZED.value());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
        }
    }
    @GetMapping("/auth/session")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(HttpServletRequest request) {

        // 세션이 있으면 기존 세션 반환
        // 세션이 없으면 null 반환
        HttpSession session = request.getSession(false);

        Map<String, Object> response = new HashMap<>();

        if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            response.put("authenticated", true);
            response.put("message", "인증됨");
            return ResponseEntity.ok(response);
        } else {
            // 따로 빼야하나..?
            response.put("authenticated", false);
            response.put("message", "세션 쿠키가 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
