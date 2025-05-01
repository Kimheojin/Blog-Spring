package HeoJin.demoBlog.controller;

import HeoJin.demoBlog.dto.request.CategoryRequest;
import HeoJin.demoBlog.dto.response.PostListResponse;
import HeoJin.demoBlog.dto.response.PostResponse;
import HeoJin.demoBlog.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostReadController {
    private final PostReadService postReadService;

    // 전체 글 반환
    @GetMapping("/posts")
    public ResponseEntity<PostListResponse> getAllPost(){
        List<PostResponse> posts = postReadService.readEntirePost();
        return ResponseEntity.ok(new PostListResponse(posts));
    }

    // 카테고리 별 반환
    @GetMapping("/categoryPosts")
    public ResponseEntity<PostListResponse> getCategoryPosts(@RequestBody
                                                             CategoryRequest categoryRequest)
    {
        List<PostResponse> posts = postReadService.CategoryPost(categoryRequest);
        return ResponseEntity.ok(new PostListResponse(posts));
    }
}