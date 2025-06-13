package HeoJin.demoBlog.comment.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDeleteRequest {

    private Long postId;
    private Long commentId;
    private Long parentId;

    private String email;
    private String password;
    private String content;
}
