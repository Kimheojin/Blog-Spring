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

import java.util.Optional;

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
            parenComment = commentRepository.findById(commentWriteRequest.getCommentId())
                    .orElseThrow(() -> new CustomNotFound("부모 댓글"));
        }

        Comment comment = Comment.builder()
                .email(commentWriteRequest.getEmail())
                .content(commentWriteRequest.getContent())
                .post(post)
                .password(commentWriteRequest.getPassword())
                .parent(parenComment)
                .build();
        commentRepository.save(comment);
    }

    public void commentDelete(CommentDeleteRequest commentDeleteRequest) {
        // fetch 조인 안하고 그냥 있나 없나만
        postRepository.findById(commentDeleteRequest.getParentId())
                .orElseThrow(() -> new CustomNotFound("포스트"));

        Comment comment = commentRepository.findById(commentDeleteRequest.getCommentId())
                .orElseThrow(() -> new CustomNotFound("커맨트"));

        if(comment.getEmail().equals(commentDeleteRequest.getEmail())&& comment.getPassword().equals(commentDeleteRequest.getPassword())){
            comment.delete();
        }else {
            throw new NotMatchException();
        }

    }


    public void commentModify(CommentModifyRequest commentModifyRequest) {
        postRepository.findById(commentModifyRequest.getParentId())
                .orElseThrow(() -> new CustomNotFound("포스트"));

        Comment comment = commentRepository.findById(commentModifyRequest.getCommentId())
                .orElseThrow(() -> new CustomNotFound("커맨트"));

        if(comment.getEmail().equals(commentModifyRequest.getEmail())&& comment.getPassword().equals(commentModifyRequest.getPassword())){
            comment.updateComment(commentModifyRequest.getContent());
        }else {
            throw new NotMatchException();
        }
    }

    public void commentAdminDelete(CommentDeleteRequest commentDeleteRequest) {
        postRepository.findById(commentDeleteRequest.getParentId())
                .orElseThrow(() -> new CustomNotFound("포스트"));

        Comment comment = commentRepository.findById(commentDeleteRequest.getCommentId())
                .orElseThrow(() -> new CustomNotFound("커맨트"));


        if(comment.getEmail().equals(commentDeleteRequest.getEmail())&& comment.getPassword().equals(commentDeleteRequest.getPassword())){
            comment.adminDelete();
        }else {
            throw new NotMatchException();
        }



    }
}
