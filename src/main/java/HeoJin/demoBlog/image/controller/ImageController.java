package HeoJin.demoBlog.image.controller;

import HeoJin.demoBlog.global.exception.image.InvalidFileException;
import HeoJin.demoBlog.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Profile("image")
public class ImageController {
    
/*
* https://cloudinary.com/documentation/java_integration
* -> Java SDK docs
* */

    private final ImageService imageService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10메가
    
    // 주로 avif로 저장 할듯
    private static final String[] ALLOWED_CONTENT_TYPES = {
            "image/png", "image/avif", "image/jpg", "image/jpeg", "image/gif"
    };


    // folder 값 안넘어오면 그냥 ~ -> 이 위치에 저장
    @PostMapping("/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "blog-images") String folder) {

        // 파일 검증
        validateFile(file);

        String imageUrl = imageService.uploadImage(file, folder);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "이미지 업로드 성공",
                "imageUrl", imageUrl,
                "originalFilename", file.getOriginalFilename(),
                "folder", folder
        ));
    }

    @GetMapping("/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getImageList(
            @RequestParam(value = "folder", required = false, defaultValue = "blog-images") String folder) {

        List<Map<String, Object>> imageList = imageService.getImageList(folder);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "이미지 리스트 조회 성공",
                "folder", folder,
                "images", imageList,
                "count", imageList.size()
        ));
    }


    private void validateFile(MultipartFile file) {
        // 파일 존재 검증
        if (file.isEmpty()) {
            throw new InvalidFileException("파일이 비어있습니다");
        }

        // 파일 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException(
                    // 10메가
                    String.format("파일 크기는 %dMB를 초과할 수 없습니다", MAX_FILE_SIZE / 1024 / 1024)
            );
        }

        // 파일 타입 검증
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedImageType(contentType)) {
            throw new InvalidFileException(
                    "지원하지 않는 파일 형식입니다. (지원형식: JPEG, PNG, GIF, AVIF)"
            );
        }
    }

    private boolean isAllowedImageType(String contentType) {
        for (String allowedType : ALLOWED_CONTENT_TYPES) {
            if (allowedType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }
}