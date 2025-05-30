package HeoJin.demoBlog.post.controller;

import HeoJin.demoBlog.post.dto.response.PagePostResponse;
import HeoJin.demoBlog.post.service.PostReadService;
import HeoJin.demoBlog.post.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostReadController {
    private final PostReadService postReadService;

    // 전체 글 반환
    @GetMapping("/posts/paged")
    public ResponseEntity<PagePostResponse> getPagedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagePostResponse pagedPosts = postReadService.readPagedPosts(page, size);
        return ResponseEntity.ok(pagedPosts);
    }

    // 카테고리 별 반환
    @GetMapping("/posts/categoryPaged")
    public ResponseEntity<PagePostResponse> getPagedCategoryPosts(
            @RequestParam String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagePostResponse pagedPosts = postReadService.readPagingCategoryPosts(categoryName ,page, size);
        return ResponseEntity.ok(pagedPosts);
    }

    // 단일 포스트 조회
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getPost(
            @RequestParam String postId
    ){
        return ResponseEntity.ok(postReadService.getSinglePost(postId));
    }

}