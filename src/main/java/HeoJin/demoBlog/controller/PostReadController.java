package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostReadController {
    private final PostReadService postReadService;

    // 전체 글 반환
    // 페이지 그거 해야할듯

    // 카테고리 별 반환

}
