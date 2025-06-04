package HeoJin.demoBlog.comment.service;

import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;

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

    public static CommentDto toCommentAdminDto(Comment comment){

        String displayContent = comment.getStatus().equals(CommentStatus.ACTIVE)
                ? comment.getContent()
                : "삭제된 댓글입니다";
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
