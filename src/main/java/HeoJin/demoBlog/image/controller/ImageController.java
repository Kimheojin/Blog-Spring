package HeoJin.demoBlog.image.controller;

import HeoJin.demoBlog.image.dto.request.DeleteImageRequest;
import HeoJin.demoBlog.image.dto.response.ImageDeleteResponse;
import HeoJin.demoBlog.image.dto.response.ImageListResponse;
import HeoJin.demoBlog.image.dto.response.UploadResponse;
import HeoJin.demoBlog.image.service.ImageService;
import HeoJin.demoBlog.image.util.ImageUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
//@Profile("image")
public class ImageController {
    
/*
* https://cloudinary.com/documentation/java_integration
* -> Java SDK docs
* */

    private final ImageService imageService;

    // folder 값 안넘어오면 그냥 ~ -> 이 위치에 저장
    @PostMapping("/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false, defaultValue = "blog-images") String folder) {

        // 파일 검증
        ImageUtil.validateFile(file);

        String imageUrl = imageService.uploadImage(file, folder);

        return ResponseEntity.ok(new UploadResponse(
                true,
                "이미지 업로드 성공",
                imageUrl,
                file.getOriginalFilename(),
                folder
        ));
    }

    @GetMapping("/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ImageListResponse> getImageList(
            @RequestParam(value = "folder", required = false, defaultValue = "blog-images") String folder) {

        List<Map<String, Object>> imageList = imageService.getImageList(folder);

        return ResponseEntity.ok(new ImageListResponse(
                true,
                "이미지 리스트 조회 성공",
                folder,
                imageList,
                imageList.size()
        ));
    }

    @DeleteMapping("/images")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ImageDeleteResponse> deleteImage(@RequestBody DeleteImageRequest request) {

        String publicId = request.getPublicId();
        boolean deleted = imageService.deleteImage(publicId);

        if (deleted) {
            return ResponseEntity.ok(new ImageDeleteResponse(
                    true,
                    "이미지 삭제 성공",
                    publicId
            ));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImageDeleteResponse(
                            false,
                            "이미지 삭제 실패",
                            publicId
                    ));
        }
    }



}