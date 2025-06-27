package HeoJin.demoBlog.image.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDeleteResponse {
    private boolean success;
    private String message;
    private String publicId;

}
