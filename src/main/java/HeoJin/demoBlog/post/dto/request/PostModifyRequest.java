package HeoJin.demoBlog.post.dto.request;


import HeoJin.demoBlog.post.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModifyRequest {
    private Long postId;
    private String title;
    private String content;
    private String categoryName;
    private PostStatus postStatus;
}
