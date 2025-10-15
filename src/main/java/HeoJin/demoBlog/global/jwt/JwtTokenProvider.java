package HeoJin.demoBlog.global.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET;


    @Value("${jwt.accesstoken.expiration}")
    private String ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refreshtoken.expiration}")
    private String REFRESH_TOKEN_EXPIRATION;

    private SecretKey getSigningKey() {
        // Base64 로 인코딩 되어 있음
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long member_id, String email, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(ACCESS_TOKEN_EXPIRATION));

        return Jwts.builder()
                .subject(String.valueOf(member_id))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long member_id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Long.parseLong(REFRESH_TOKEN_EXPIRATION));

        return Jwts.builder()
                .subject(String.valueOf(member_id))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // RefreshToken 만료 시간을 LocalDateTime으로 반환
    public long getRefreshTokenExpirationMillis() {
        return Long.parseLong(REFRESH_TOKEN_EXPIRATION);
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    // 토큰이 만료되었는지만 확인 (서명은 검증)
    public boolean isTokenExpired(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return false; // 만료되지 않음
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return true; // 만료됨
        } catch (Exception e) {
            return false; // 다른 오류 (유효하지 않은 토큰)
        }
    }

    // 만료된 토큰에서도 Claims 추출 (서명 검증은 함)
    public Claims getClaimsFromExpiredToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // 만료된 경우에도 Claims 반환
            return e.getClaims();
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public Long getMemberId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public Long getMemberIdFromExpiredToken(String token) {
        return Long.parseLong(getClaimsFromExpiredToken(token).getSubject());
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}
