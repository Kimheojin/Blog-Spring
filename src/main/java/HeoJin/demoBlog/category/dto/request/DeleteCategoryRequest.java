package HeoJin.demoBlog.category.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCategoryRequest {

    private Long categoryId;
    private String categoryName;
}
