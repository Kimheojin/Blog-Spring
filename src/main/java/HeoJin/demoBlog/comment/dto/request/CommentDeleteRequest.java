package HeoJin.demoBlog.comment.dto.request;


import lombok.Data;

@Data
public class CommentDeleteRequest {

    private Long postId;
    private Long commentId;
    private Long parentId;

    private String email;
    private String password;
    private String content;
}
