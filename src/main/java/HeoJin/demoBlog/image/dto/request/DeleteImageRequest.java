package HeoJin.demoBlog.image.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteImageRequest {
    @NotBlank(message = "publicId는 필수값입니다.")
    private String publicId;

}

