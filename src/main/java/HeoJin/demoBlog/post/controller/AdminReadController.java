package HeoJin.demoBlog.post.controller;


import HeoJin.demoBlog.post.dto.response.PagePostResponse;
import HeoJin.demoBlog.post.dto.response.PostResponse;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.service.AdminPostReadService;
import lombok.NoArgsConstructor;
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



    // 상태 상관 X + 전체 포스트 조회
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PagePostResponse> getPublishedPagedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PagePostResponse pagedPosts = adminPostReadService.readAdminPagedPosts(page, size);
        return ResponseEntity.ok(pagedPosts);
    }

    // 카테고리 별 반환 (전체 status)
    @GetMapping("/posts/category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PagePostResponse> getPagedPublishedCategoryPosts(
            @RequestParam String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PagePostResponse pagedPosts = adminPostReadService.readAdminPagingCategoryPosts(categoryName ,page, size);
        return ResponseEntity.ok(pagedPosts);
    }

    // 단일 포스트 조회 (전체 상태)
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostResponse> getPost(
            @RequestParam String postId
    ){
        return ResponseEntity.ok(adminPostReadService.getAdminSinglePost(postId));
    }

    // 상태 별 post 반환
    @GetMapping("/statusPosts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PagePostResponse> getStatusPosts(
            @RequestParam PostStatus postStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PagePostResponse pagedPosts = adminPostReadService.readAdminPagingStatusPosts(postStatus ,page, size);
        return ResponseEntity.ok(pagedPosts);
    }

}
