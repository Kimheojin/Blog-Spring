package HeoJin.demoBlog.comment.service;

import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.entity.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment){
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
