package HeoJin.demoBlog.member.service;


import HeoJin.demoBlog.global.util.CustomUserDetail;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.member.entity.Role;
import HeoJin.demoBlog.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Test
    @DisplayName("loadUserByUsername -> 정상 동작 테스트")
    void test1() {
        // given
        String email = "test@naver.com";
        Member mockMember = Member.builder()
                .id(1L)
                .email(email)
                .password("encoded_password")
                .memberName("테스트유저")
                .role(Role.builder()
                        .id(1L)
                        .roleName("ADMIN").build())
                .build();

        Mockito.when(memberRepository.findByEmail(email))
                .thenReturn(Optional.of(mockMember));

        // when
        UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

        // then
        Assertions.assertNotNull(userDetails);
        Assertions.assertInstanceOf(CustomUserDetail.class, userDetails);
        Assertions.assertEquals(email, userDetails.getUsername());

        Mockito.verify(memberRepository).findByEmail(email);
    }


}
