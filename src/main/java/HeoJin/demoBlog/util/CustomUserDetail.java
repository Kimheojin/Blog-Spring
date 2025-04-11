package HeoJin.demoBlog.util;


import HeoJin.demoBlog.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetail implements UserDetails {

    private final Member member;
    private final Collection<? extends GrantedAuthority> authorities;


    // 생성자
    public CustomUserDetail(Member member) {
        this.member = member;

        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().getName())
        );
    }


    public Member getMember() {
        return member;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() { // email 대신 반환
        return member.getEmail();
    }
}
