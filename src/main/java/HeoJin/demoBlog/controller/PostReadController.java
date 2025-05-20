package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.dto.request.CategoryRequest;
import HeoJin.demoBlog.dto.response.PagePostResponse;
import HeoJin.demoBlog.service.PostReadService;
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

    @GetMapping("/posts/paged")
    public ResponseEntity<PagePostResponse> getPagedCategoryPosts(
            @RequestBody CategoryRequest categoryRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagePostResponse pagedPosts = postReadService.readPagingCategoryPosts(categoryRequest,page, size);
        return ResponseEntity.ok(pagedPosts);
    }

}