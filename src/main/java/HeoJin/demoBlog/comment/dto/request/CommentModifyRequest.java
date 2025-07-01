package HeoJin.demoBlog.comment.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentModifyRequest {
    
    @NotNull(message = "포스트 ID를 선택해 주세요")
    private Long postId;
    @NotNull(message = "comment ID를 선택해 주세요")
    private Long commentId;
    // 이거 null 허용
    @PositiveOrZero(message = "parentId는 0 이상이어야 합니다.")
    private Long parentId;

    @NotBlank(message = "비밀번호를 입력 해 주세요")
    private String password;
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "유효하지 않은 이메일 입니다.")
    private String email;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;
}

