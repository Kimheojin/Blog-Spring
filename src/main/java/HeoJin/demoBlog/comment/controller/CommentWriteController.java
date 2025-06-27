package HeoJin.demoBlog.comment.controller;


import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.dto.Response.CommentListDto;
import HeoJin.demoBlog.comment.dto.request.CommentDeleteRequest;
import HeoJin.demoBlog.comment.dto.request.CommentModifyRequest;
import HeoJin.demoBlog.comment.dto.request.CommentWriteRequest;
import HeoJin.demoBlog.comment.service.CommentReadService;
import HeoJin.demoBlog.comment.service.CommentWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommentListDto> writeComment(
            @RequestBody @Valid CommentWriteRequest commentWriteRequest){
        commentWriteService.commentWrite(commentWriteRequest);
        List<CommentDto> commentDtos = commentReadService.getCommentByPostId(commentWriteRequest.getPostId());
        return ResponseEntity.ok(new CommentListDto(commentDtos));
    }
    // 댓글 + 대댓글 임시 삭제
    @PostMapping("/comments")
    public ResponseEntity<CommentListDto> deleteComment(
            @RequestBody @Valid CommentDeleteRequest commentDeleteRequest
            ){
        commentWriteService.commentDelete(commentDeleteRequest);

        List<CommentDto> commentDtos = commentReadService.getCommentByPostId(commentDeleteRequest.getPostId());

        return ResponseEntity.ok(new CommentListDto(commentDtos));
    }


    // 댓글 수정
    @PutMapping("/comments")
    public ResponseEntity<CommentListDto> modifyComment(
            @RequestBody @Valid CommentModifyRequest commentModifyRequest
            ){

        commentWriteService.commentModify(commentModifyRequest);

        List<CommentDto> commentDtos = commentReadService.getCommentByPostId(commentModifyRequest.getPostId());

        return ResponseEntity.ok(new CommentListDto(commentDtos));
    }


}
