package HeoJin.demoBlog.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageListResponse {
    private boolean success;
    private String message;
    private String folder;
    private List<Map<String, Object>> images;
    private int count;

}
