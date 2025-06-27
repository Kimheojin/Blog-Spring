package HeoJin.demoBlog.comment.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentWriteRequest {

    @NotNull(message = "포스트 ID를 선택해 주세요")
    private Long postId;
    @PositiveOrZero(message = "parent ID는 양수여야 합니다.")
    private Long parentId;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "유효하지 않은 이메일 값 입니다.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해 주세요")
    private String password;
    @NotBlank(message = "내용을 입력해 주세요")
    private String content;

}
