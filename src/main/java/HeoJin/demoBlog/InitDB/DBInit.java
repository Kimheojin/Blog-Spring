package HeoJin.demoBlog.InitDB;

import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.domain.Role;
import HeoJin.demoBlog.repository.MemberRepository;
import HeoJin.demoBlog.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Component
public class DBInit {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // DB 그거

    @PostConstruct
    public void init() {
        initRoleAndUser();
    }

    @Transactional
    public void initRoleAndUser(){

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name("ADMIN").build();
                    return roleRepository.save(newRole);
                });

        if (memberRepository.findByEmail("hurjin1109@naver.com").isEmpty()) {
            Member member = Member.builder()
                    .email("hurjin1109@naver.com")
                    .password(passwordEncoder.encode("1234"))
                    .membername("허진")// 나중에 .gitnore 등록하고 바꾸기
                    .role(adminRole)
                    .build();
            memberRepository.save(member);
            System.out.println("초기 사용자 데이터가 생성되었습니다.");
        }

    }


}
