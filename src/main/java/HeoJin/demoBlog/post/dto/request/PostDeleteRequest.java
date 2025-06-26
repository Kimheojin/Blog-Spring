package HeoJin.demoBlog.post.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class PostDeleteRequest {
    @NotNull(message = "포스트 ID를 선택 해 주세요")
    private Long postId;
}
