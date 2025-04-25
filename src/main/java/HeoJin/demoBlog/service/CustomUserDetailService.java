package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.exception.MemberNotExist;
import HeoJin.demoBlog.repository.MemberRepository;
import HeoJin.demoBlog.util.CustomUserDetail;
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
