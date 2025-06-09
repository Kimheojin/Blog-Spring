package HeoJin.demoBlog.category.dto.request;


import lombok.*;

@Getter
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ModifyCategoryNameRequest {
    private Long categoryId;
    private String wantCategoryName;

}
