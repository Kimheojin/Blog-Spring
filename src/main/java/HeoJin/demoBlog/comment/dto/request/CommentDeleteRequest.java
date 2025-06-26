package HeoJin.demoBlog.comment.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDeleteRequest {
    @NotNull(message = "포스트 ID 값을 선택 해 주세요.")
    private Long postId;
    @NotNull(message = "comment ID 값을 선택 해 주세요.")
    private Long commentId;
    private Long parentId;


    @NotBlank(message = "email을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "비밀번호를 입력 해 주세요.")
    private String password;
    @NotBlank(message = "내용을 입력 해 주세요.")
    private String content;

}
