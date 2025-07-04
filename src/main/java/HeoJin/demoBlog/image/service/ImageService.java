package HeoJin.demoBlog.image.service;

import HeoJin.demoBlog.global.exception.image.CloudinaryCustomRuntimeException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
// @Profile("image")
public class ImageService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            // 폴더가 null이거나 빈 문자열이면 기본 폴더 사용
            if (folder == null || folder.trim().isEmpty()) {
                folder = "test";
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    ));

            String imageUrl = uploadResult.get("secure_url").toString();
            return imageUrl;

        } catch (Exception e) {
            throw new CloudinaryCustomRuntimeException("이미지 업로드 실패", e);
        }
    }

    public List<Map<String, Object>> getImageList(String folder) {
        try {
            // 폴더가 null이거나 빈 문자열이면 기본 폴더 사용
            if (folder == null || folder.trim().isEmpty()) {
                folder = "blog-images";
            }

            Map result = cloudinary.search()
                    .expression("folder:" + folder)
                    .sortBy("created_at", "desc")
                    .maxResults(100)
                    .execute();

            List<Map> resources = (List<Map>) result.get("resources");
            List<Map<String, Object>> imageList = new ArrayList<>();

            for (Map resource : resources) {
                Map<String, Object> imageInfo = Map.of(
                        "publicId", resource.get("public_id").toString(),
                        "secureUrl", resource.get("secure_url").toString(),
                        "originalFilename", resource.getOrDefault("original_filename", "unknown"),
                        "createdAt", resource.get("created_at").toString(),
                        "format", resource.get("format").toString(),
                        "bytes", resource.get("bytes"),
                        "width", resource.get("width"),
                        "height", resource.get("height")
                );
                imageList.add(imageInfo);
            }

            return imageList;

        } catch (Exception e) {
            throw new CloudinaryCustomRuntimeException("이미지 리스트 조회 실패", e);
        }
    }

    // 파일 삭제 로직
    public boolean deleteImage(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = result.get("result").toString();

            // ok or not found (이미 없는 경우도 성공?으로)
            return "ok".equals(resultStatus) || "not found".equals(resultStatus);

        } catch (Exception e) {
            throw new CloudinaryCustomRuntimeException("이미지 삭제 실패", e);
        }
    }
}