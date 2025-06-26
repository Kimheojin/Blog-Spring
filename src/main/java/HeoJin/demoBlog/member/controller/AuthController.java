package HeoJin.demoBlog.member.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/auth/session")
    public ResponseEntity<Map<String, Object>> checkAuthStatus(HttpServletRequest request){

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
