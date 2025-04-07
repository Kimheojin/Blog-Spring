package HeoJin.demoBlog.domain;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginDto {

    private String email;
    private String password;
}
