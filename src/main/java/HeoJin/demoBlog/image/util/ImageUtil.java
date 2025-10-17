package HeoJin.demoBlog.image.util;

import HeoJin.demoBlog.global.exception.image.InvalidFileException;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;


@Profile("!test")
public class ImageUtil {
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10메가
    // 주로 avif로 저장 할듯
    private static final String[] ALLOWED_CONTENT_TYPES = {
            "image/png", "image/avif", "image/jpg", "image/jpeg", "image/gif"
    };

    public static void validateFile(MultipartFile file) {
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

    protected static boolean isAllowedImageType(String contentType) {
        for (String allowedType : ALLOWED_CONTENT_TYPES) {
            if (allowedType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

}
