package HeoJin.demoBlog.member.controller;


import HeoJin.demoBlog.member.dto.request.LoginDto;
import HeoJin.demoBlog.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PostMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        // JWT 기반 로그아웃 처리
        authService.logout(request, response);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃 되었습니다.");
        responseBody.put("statusCode", HttpStatus.OK.value());

        return ResponseEntity.ok(responseBody);
    }

    // Filter에서 이미 인증 처리되므로 SecurityContext에서 정보만 가져옴
    @GetMapping("/auth/session")
    public ResponseEntity<Map<String, Object>> checkAuthStatus() {

        Map<String, Object> response = new HashMap<>();

        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            // 이거 한겹인가
            Long memberId = (Long) authentication.getPrincipal();

            // 권한 정보 추출
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("");

            response.put("authenticated", true);
            response.put("message", "인증됨");
            response.put("memberId", memberId);
            response.put("role", role);
            return ResponseEntity.ok(response);
        } else {
            // 아마 발생 안할듯
            response.put("authenticated", false);
            response.put("message", "인증되지 않음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
