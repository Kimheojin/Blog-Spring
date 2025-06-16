package HeoJin.demoBlog.comment.service;

import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.dto.request.CommentWriteRequest;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;
import HeoJin.demoBlog.post.entity.Post;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment){
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .email(comment.getEmail())
                .postId(comment.getPost().getId()) // 페치 조인 사용해야할듯
                // null이 아니면 넣고 그거
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .regDate(comment.getRegDate())
                .build();
    }

    public static CommentDto toCommentAdminDto(Comment comment){

        String displayContent = comment.getStatus().equals(CommentStatus.ACTIVE)
                ? comment.getContent()
                : "삭제된 댓글입니다";
        return CommentDto.builder()
                .id(comment.getId())
                .content(displayContent)
                .email(comment.getEmail())
                .postId(comment.getPost().getId()) // 페치 조인 사용해야할듯
                // null이 아니면 넣고 그거
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .regDate(comment.getRegDate())
                .build();
    }

    public static Comment toComment(CommentWriteRequest commentWriteRequest, Post post, Comment parentComment){
        return Comment.builder()
                .email(commentWriteRequest.getEmail())
                .content(commentWriteRequest.getContent())
                .post(post)
                .password(commentWriteRequest.getPassword())
                .parent(parentComment)
                .regDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();

    }


}
