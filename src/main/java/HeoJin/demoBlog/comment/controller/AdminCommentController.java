package HeoJin.demoBlog.comment.controller;


import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.dto.request.CommentDeleteRequest;
import HeoJin.demoBlog.comment.service.CommentReadService;
import HeoJin.demoBlog.comment.service.CommentWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminCommentController {


    private final CommentWriteService commentWriteService;
    private final CommentReadService commentReadService;


    //상태 상관 X 전체 댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId){
        List<CommentDto> commentDtos = commentReadService.getAdminCommentByPostId(postId);
        return ResponseEntity.ok(commentDtos);
    }


    // 댓글 + 대댓글 admin 삭제
    @DeleteMapping("/comments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CommentDto>> adminDeleteComment(
            @RequestBody CommentDeleteRequest commentDeleteRequest
    ){
        commentWriteService.commentAdminDelete(commentDeleteRequest);

        return ResponseEntity.ok(commentReadService.getCommentByPostId(commentDeleteRequest.getPostId()));
    }





}
