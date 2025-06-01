package HeoJin.demoBlog.comment.repository;

import HeoJin.demoBlog.comment.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> customFindCommentsByPostId(Long postId);
}
