package HeoJin.demoBlog.category.dto.data;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryWithCountDto {
    private Long categoryId;
    private String categoryName;
    private Long postCount;
    private Long priority;
}
