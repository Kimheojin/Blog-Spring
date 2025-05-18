package HeoJin.demoBlog.repository;


import HeoJin.demoBlog.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    
    // entity 설정 안해놓으면 eager 해놔도 쿼리 두번 나감
    @EntityGraph(attributePaths = "role")
    Optional<Member> findByEmail(String email);
}
