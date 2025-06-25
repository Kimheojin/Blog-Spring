package HeoJin.demoBlog.image.controller;

import HeoJin.demoBlog.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_CONTENT_TYPES = {
            // 3개 형식
            "image/png", "image/avif", "image/jpg"
    };

    @PostMapping("/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {

        // 파일 검증
        validateFile(file);

        try {
            String imageUrl = imageService.uploadImage(file);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "이미지 업로드 성공",
                    "imageUrl", imageUrl,
                    "originalFilename", file.getOriginalFilename()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "이미지 업로드 실패: " + e.getMessage()
            ));
        }
    }

    private void validateFile(MultipartFile file) {
        // 파일 존재 검증
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다");
        }

        // 파일 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("파일 크기는 %dMB를 초과할 수 없습니다", MAX_FILE_SIZE / 1024 / 1024)
            );
        }

        // 파일 타입 검증
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedImageType(contentType)) {
            throw new IllegalArgumentException(
                    "지원하지 않는 파일 형식입니다. (지원형식: JPEG, PNG, GIF, WebP)"
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