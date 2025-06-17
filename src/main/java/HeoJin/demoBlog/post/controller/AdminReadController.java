package HeoJin.demoBlog.post.controller;

import HeoJin.demoBlog.post.dto.response.PagePostResponse;
import HeoJin.demoBlog.post.dto.response.PostResponse;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.service.AdminPostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminReadController {
    private final AdminPostReadService adminPostReadService;

    // 복합 조건 api
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PagePostResponse> getAdminPosts(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) PostStatus postStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PagePostResponse pagedPosts = adminPostReadService.readAdminPosts(
                categoryName, postStatus, page, size);
        return ResponseEntity.ok(pagedPosts);
    }


    // 단일 포스트 조회 (전체 상태) - URL 경로를 다르게 변경
    @GetMapping("/posts/single")  // 경로 변경: /posts → /posts/single
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> getPost(
            @RequestParam Long postId) {

        return ResponseEntity.ok(adminPostReadService.getAdminSinglePost(postId));
    }
}
