package HeoJin.demoBlog.category.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryRequest {

    @NotBlank(message = "유효하지 않은 카테고리 명 입니다.")
    @Size(max = 15, message = "카테고리 명은 15자를 넘을 수 없습니다.")
    private String categoryName;

    @NotNull(message = "우선순위를 선택해주세요")
    @PositiveOrZero(message = "음수는 입력이 불가합니다.")
    private Long priority;
}
