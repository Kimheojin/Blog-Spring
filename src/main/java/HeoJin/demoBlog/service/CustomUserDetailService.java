package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.exception.MemberNotFound;
import HeoJin.demoBlog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFound::new);

        return new User( //spring security authentication 그거
                member.getEmail(),                  // username (이메일을 사용)
                member.getPassword(),               // password
                Collections.emptyList()             // 빈 권한 목록
        );
    }


}
