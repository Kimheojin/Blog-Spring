package HeoJin.demoBlog.image.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {
        try {

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.emptyMap());

            String imageUrl = uploadResult.get("secure_url").toString();

            return imageUrl;

        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage(), e);
        }
    }
}