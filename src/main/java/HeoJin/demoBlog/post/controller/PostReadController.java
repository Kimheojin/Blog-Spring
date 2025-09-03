package HeoJin.demoBlog.post.controller;

import HeoJin.demoBlog.post.dto.response.PagePostResponse;
import HeoJin.demoBlog.post.dto.response.PostResponse;
import HeoJin.demoBlog.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
    @RequestMapping("/api")
    @RequiredArgsConstructor
public class PostReadController {
    private final PostReadService postReadService;

    // 전체 글 반환 (PUBLISHED만) + 조회 글 수 반환
    @GetMapping("/posts")
    public ResponseEntity<PagePostResponse> getPublishedPagedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PagePostResponse pagedPosts = postReadService.readPagedPosts(page, size);
        return ResponseEntity.ok(pagedPosts);
    }

    // 카테고리 별 반환 (PUBLISHED만) + 조회 글 수 반환
    @GetMapping("/posts/category")
    public ResponseEntity<PagePostResponse> getPagedPublishedCategoryPosts(
            @RequestParam String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PagePostResponse pagedPosts = postReadService.readPagingCategoryPosts(categoryName, page, size);
        return ResponseEntity.ok(pagedPosts);
    }

    // 단일 포스트 조회 (PUBLISHED)
    @GetMapping("/posts/single")
    public ResponseEntity<PostResponse> getPost(
            @RequestParam Long postId) {
        PostResponse response = postReadService.getSinglePost(postId);

        if(isCached(response.getRegDate())){
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                    .body(response);
        }else{
            // 캐시 설정 X
            return ResponseEntity.ok(response);
        }


    }

    private boolean isCached(LocalDateTime regDate){
        return regDate.isBefore(LocalDateTime.now().minusDays(14));
    }

    // 연관 포스트 조회 (PUBLISHED)
}