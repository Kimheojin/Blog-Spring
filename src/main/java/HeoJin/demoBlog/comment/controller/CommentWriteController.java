package HeoJin.demoBlog.comment.controller;


import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.dto.request.CommentDeleteRequest;
import HeoJin.demoBlog.comment.dto.request.CommentModifyRequest;
import HeoJin.demoBlog.comment.dto.request.CommentWriteRequest;
import HeoJin.demoBlog.comment.service.CommentReadService;
import HeoJin.demoBlog.comment.service.CommentWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentWriteController {

    private final CommentWriteService commentWriteService;
    private final CommentReadService commentReadService;
    // 댓글 + 대댓글 작성
    @PostMapping("/posts/comments")
    public ResponseEntity<List<CommentDto>> writeComment(
            @RequestBody CommentWriteRequest commentWriteRequest){
        commentWriteService.commentWrite(commentWriteRequest);

        return ResponseEntity.ok(commentReadService.getCommentByPostId(commentWriteRequest.getPostId()));
    }
    // 댓글 + 대댓글 임시 삭제
    @PostMapping("/comments")
    public ResponseEntity<List<CommentDto>> deleteComment(
            @RequestBody CommentDeleteRequest commentDeleteRequest
            ){
        commentWriteService.commentDelete(commentDeleteRequest);

        return ResponseEntity.ok(commentReadService.getCommentByPostId(commentDeleteRequest.getPostId()));
    }


    // 댓글 수정
    @PutMapping("/comments")
    public ResponseEntity<List<CommentDto>> modifyComment(
            @RequestBody CommentModifyRequest commentModifyRequest
            ){

        commentWriteService.commentModify(commentModifyRequest);

        return ResponseEntity.ok(commentReadService.getCommentByPostId(commentModifyRequest.getPostId()));
    }


}
