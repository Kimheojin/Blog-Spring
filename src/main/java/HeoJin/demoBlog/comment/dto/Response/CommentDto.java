package HeoJin.demoBlog.comment.dto.Response;

import HeoJin.demoBlog.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Builder
@Data
public class CommentDto {
    private Long id;
    private String content;
    private String email;
    private Long postId;
    // null 가능
    private Long parentId;

    @Setter
    @Builder.Default
    private List<CommentDto> replies = new ArrayList<>();

}
