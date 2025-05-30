package HeoJin.demoBlog.member.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
