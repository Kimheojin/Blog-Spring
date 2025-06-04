package HeoJin.demoBlog.comment.service;

import HeoJin.demoBlog.comment.dto.request.CommentDeleteRequest;
import HeoJin.demoBlog.comment.dto.request.CommentModifyRequest;
import HeoJin.demoBlog.comment.dto.request.CommentWriteRequest;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.repository.CommentRepository;
import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.global.exception.NotMatchException;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentWriteService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void commentWrite(CommentWriteRequest commentWriteRequest) {
        Post post = postRepository.findById(commentWriteRequest.getPostId())
                .orElseThrow(() -> new CustomNotFound("포스트"));

        Comment parenComment = null;
        if(commentWriteRequest.getParentId() != null){
            parenComment = commentRepository.findById(commentWriteRequest.getParentId())
                    .orElseThrow(() -> new CustomNotFound("부모 댓글"));
        }

        commentRepository.save(CommentMapper.toComment(commentWriteRequest,
                post, parenComment));
    }

    public void commentDelete(CommentDeleteRequest request) {
        Comment comment = validateCommentAccess(request.getPostId(),
                request.getCommentId(),
                request.getEmail(),
                request.getPassword());

        comment.delete();
    }

    public void commentModify(CommentModifyRequest request) {
        Comment comment = validateCommentAccess(request.getPostId(),
                request.getCommentId(),
                request.getEmail(),
                request.getPassword());

        comment.updateComment(request.getContent());
    }

    public void commentAdminDelete(CommentDeleteRequest request) {
        Comment comment = validateCommentAccess(request.getPostId(),
                request.getCommentId(),
                request.getEmail(),
                request.getPassword());

        comment.adminDelete();
    }

    // 공통 검증 로직
    private Comment validateCommentAccess(Long postId, Long commentId, String email, String password) {

        postRepository.findById(postId)
                .orElseThrow(() -> new CustomNotFound("포스트"));


        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomNotFound("커맨트"));


        if (!isMatchAboutEmailAndPassword(comment, email, password)) {
            throw new NotMatchException();
        }

        return comment;
    }

    private boolean isMatchAboutEmailAndPassword(Comment comment, String email, String password ){
        return comment.getEmail().equals(email)
                && comment.getPassword().equals(password);
    }
}
