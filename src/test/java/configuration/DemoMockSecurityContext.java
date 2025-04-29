package configuration;

import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.domain.Role;
import HeoJin.demoBlog.repository.MemberRepository;
import HeoJin.demoBlog.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class DemoMockSecurityContext implements WithSecurityContextFactory<WithMockCustomUser> {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        String email = annotation.email();
        String password = annotation.password();
        String memberName = annotation.memberName();
        String[] roles = annotation.roles();


        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member testMember = memberRepository.findByEmail(email)
                .orElseGet(() -> createMember(email, password, memberName, roles));

        List<SimpleGrantedAuthority> authorities = testMember.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Authentication auth = new UsernamePasswordAuthenticationToken(testMember.getEmail(), null, authorities);
        context.setAuthentication(auth);


        return context;
    }


    @Transactional(readOnly = false)
    protected Member createMember(String email, String password, String memberName, String[] roles){
        // DB에 저장 후 반환

        // role
        Role mockRole = roleRepository.findByRoleName(roles[0])
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .roleName(roles[0]).build();
                    return roleRepository.save(newRole);
                });

        // member
        Member member = Member.builder()
                .memberName(memberName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(mockRole).build();

        memberRepository.save(member);

        return member;
    }
}
