package HeoJin.demoBlog.category.dto.request;


import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ModifyCategoryNameRequest {
    private Long categoryId;
    private String wantCategoryName;

}
