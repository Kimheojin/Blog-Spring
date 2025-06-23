package HeoJin.demoBlog.comment.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentModifyRequest {


    private Long postId;
    private Long commentId;
    private Long parentId;

    private String email;
    private String password;
    private String content;
}

