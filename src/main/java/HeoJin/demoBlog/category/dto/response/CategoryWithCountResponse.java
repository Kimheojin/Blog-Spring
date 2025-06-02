package HeoJin.demoBlog.category.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CategoryWithCountResponse {
    private Long categoryId;
    private String categoryName;
    private Long postCount;
}
