package HeoJin.demoBlog.comment.dto;

import HeoJin.demoBlog.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Builder
@Setter
public class CommentDto {
    private Long id;
    private String content;
    private String email;
    private Long postId;
    // null 가능
    private Long parentId;

    @Builder.Default
    private List<CommentDto> replies = new ArrayList<>();

    public static CommentDto from(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .email(comment.getEmail())
                .postId(comment.getPost().getId()) // 페치 조인 사용해야할듯
                // null이 아니면 넣고 그거
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .build();
    }
}
