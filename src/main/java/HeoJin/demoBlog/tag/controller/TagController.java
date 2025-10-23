package HeoJin.demoBlog.tag.controller;


import HeoJin.demoBlog.tag.dto.request.TagUpdateRequestDto;
import HeoJin.demoBlog.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    // 태그 수정 엔드포인트
    @PutMapping("/tag/list")
    public ResponseEntity<String> updateTag(
            TagUpdateRequestDto tagUpdateRequestDto
    ){

        tagService.updateTag(tagUpdateRequestDto);
        return ResponseEntity.ok("hello");
    }

    // 태그 목록 반환
    @GetMapping("/tag/list")
    public ResponseEntity<String> getTagList(
    ){
        return ResponseEntity.ok("hello");
    }

    // 태그 목록 정리 (수동)
    @PatchMapping("/tag/orphaned")
    public ResponseEntity<> Tag(

    )


}
