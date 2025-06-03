package HeoJin.demoBlog.post.controller;


import HeoJin.demoBlog.global.util.CustomUserDetail;
import HeoJin.demoBlog.post.dto.request.PostDeleteRequest;
import HeoJin.demoBlog.post.dto.request.PostModifyRequest;
import HeoJin.demoBlog.post.service.PostWriteService;
import HeoJin.demoBlog.post.dto.request.PostRequest;
import HeoJin.demoBlog.post.dto.response.PostContractionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class PostWriteController {

    private final PostWriteService postWriteService;


    // 게시글 작성
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/posts")
    public ResponseEntity<PostContractionResponse> writePost(@AuthenticationPrincipal CustomUserDetail userDetail,
                                                             @RequestBody @Valid  PostRequest postDto) {
        
        // 중복 관련 로직 추가하기
        return ResponseEntity.ok(postWriteService.writePost(userDetail.getMember(), postDto));
    }

    // 게시글 수정
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/posts")
    public ResponseEntity<PostContractionResponse> modifyPost(@AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid PostModifyRequest postModifyRequest){
        return ResponseEntity.ok(postWriteService.updatePost(postModifyRequest));
    }

    // 게시글 삭제
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/posts")
    public ResponseEntity<String> DeletePost(@AuthenticationPrincipal CustomUserDetail userDetail,
                                       @RequestBody PostDeleteRequest postDeleteRequest){
        postWriteService.deletePost(postDeleteRequest);
        return ResponseEntity.ok("게시글이 성골적으로 삭제 되었습니다.");
    }


    // 연관 포스트 조회

    // 예약 발행
}
