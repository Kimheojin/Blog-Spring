package HeoJin.demoBlog.category.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ModifyCategoryNameRequest {

    @NotNull(message = "카테고링 ID는 필수입니다.")
    @Positive(message = "유효하지 않은 카테고리 ID 입니다.")
    private Long categoryId;

    @NotBlank(message = "삭제하고자 하는 카테고리 이름이 유효하지 않습니다.")
    @Size(max = 15, message = "카테고리 명은 15자를 넘을 수 없습니다. ")
    private String categoryName;

}
