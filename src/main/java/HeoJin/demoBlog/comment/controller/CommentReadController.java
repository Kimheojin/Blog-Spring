package HeoJin.demoBlog.comment.controller;


import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.dto.Response.CommentListDto;
import HeoJin.demoBlog.comment.service.CommentReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentReadController {


    private final CommentReadService commentReadService;

    // postId에 따른 전체 댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentListDto> getComments(@PathVariable Long postId){
        List<CommentDto> commentDtos = commentReadService.getCommentByPostId(postId);
        return ResponseEntity.ok(new CommentListDto(commentDtos));
    }
}
