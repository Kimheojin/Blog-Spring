package HeoJin.demoBlog.post.dto.request;


import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class PostDeleteRequest {
    private Long postId;
}
