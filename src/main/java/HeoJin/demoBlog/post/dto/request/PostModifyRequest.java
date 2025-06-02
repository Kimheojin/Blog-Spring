package HeoJin.demoBlog.post.dto.request;


import HeoJin.demoBlog.post.entity.PostStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PostModifyRequest {
    private Long postId;
    private String title;
    private String content;
    private String categoryName;
    private PostStatus postStatus;
}
