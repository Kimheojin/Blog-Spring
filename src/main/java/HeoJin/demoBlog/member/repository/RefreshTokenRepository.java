package HeoJin.demoBlog.member.repository;


import HeoJin.demoBlog.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    // 만료된 토큰 삭제 (배치 작업용)
    void deleteByExpiryDateBefore(LocalDateTime now);
}