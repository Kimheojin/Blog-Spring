package HeoJin.demoBlog.post.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor // 프록시 혹시 몰라서
public class PostModifyRequest {
    private Long postId;
    private String title;
    private String content;
    private String categoryName;
}
