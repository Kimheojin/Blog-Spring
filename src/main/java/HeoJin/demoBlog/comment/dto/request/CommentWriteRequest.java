package HeoJin.demoBlog.comment.dto.request;

import lombok.Data;

@Data
public class CommentWriteRequest {

    private Long postId;
    private Long parentId;

    private String email;
    private String password;
    private String content;

}
