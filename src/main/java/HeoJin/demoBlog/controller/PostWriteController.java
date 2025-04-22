package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.dto.request.PostRequest;
import HeoJin.demoBlog.dto.response.PostcontractionResponse;
import HeoJin.demoBlog.service.PostWriteService;
import HeoJin.demoBlog.util.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class PostWriteController {

    private final PostWriteService postWriteService;


    // 게시글 작성


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/post")
    public ResponseEntity<PostcontractionResponse> writepost(@AuthenticationPrincipal CustomUserDetail userDetail,
                                                             @RequestBody PostRequest postDto) {
        return ResponseEntity.ok(postWriteService.writePost(userDetail.getMember(), postDto));
    }


}
