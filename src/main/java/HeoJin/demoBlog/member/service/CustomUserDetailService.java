package HeoJin.demoBlog.member.service;


import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.global.exception.MemberNotExist;
import HeoJin.demoBlog.member.respository.MemberRepository;
import HeoJin.demoBlog.global.util.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotExist::new);

        return new CustomUserDetail(member);
    }


}
