package HeoJin.demoBlog.comment.dto.request;


import lombok.Data;

@Data
public class CommentModifyRequest {


    private Long postId;
    private Long commentId;
    private Long ParentId;

    private String email;
    private String password;
    private String content;
}

