package HeoJin.demoBlog.post.dto.request;


import HeoJin.demoBlog.post.entity.PostStatus;
import lombok.*;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private String title;
    private String content;
    private String categoryName;
    private PostStatus postStatus; // 잘못된 값 오면 400에러

}
