package HeoJin.demoBlog.comment.service;

import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CommentReadService {

    private final CommentRepository commentRepository;
    public List<CommentDto> getCommentByPostId(Long postId) {
        // 사용자 삭제, active 부분 조회
        List<Comment> comments = commentRepository.customFindCommentsByPostId(postId);

        return comments.stream()
                .filter(comment -> comment.getParent() == null)
                // 사용자 삭제 부분 변환
                .map(comment -> buildCommentTree(comment, comments))
                .collect(toList());
    }

    // 상태 상관 없이
    public List<CommentDto> getAdminCommentByPostId(Long postId) {

        List<Comment> comments = commentRepository.customFindAllCommentByPostIdForAdmin(postId);

        return comments.stream()
                .filter(comment -> comment.getParent() == null)
                .map(comment -> buildAdminCommentTree(comment, comments))
                .collect(toList());

    }
    // 전체 commentlist 조회
    public List<CommentDto> getAdminComment() {
        List<Comment> allComments = commentRepository.findAll();

        return allComments.stream()
                .filter(comment -> comment.getParent() == null)
                .map(comment -> buildAdminCommentTree(comment, allComments))
                .collect(toList());
    }

    private CommentDto buildCommentTree(Comment comment, List<Comment> comments){

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        List<CommentDto> replies = comments.stream()
                .filter(c -> c.getParent() != null
                        && c.getParent().getId().equals(comment.getId()))
                .map(CommentMapper::toCommentDto)
                .collect(toList());

        commentDto.setReplies(replies);
        return commentDto;
    }
    private CommentDto buildAdminCommentTree(Comment comment, List<Comment> comments){

        CommentDto commentDto = CommentMapper.toCommentAdminDto(comment);

        List<CommentDto> replies = comments.stream()
                .filter(c -> c.getParent() != null && c.getParent().getId().equals(comment.getId()))
                .map(CommentMapper::toCommentAdminDto)
                .collect(toList());

        commentDto.setReplies(replies);
        return commentDto;
    }



}
