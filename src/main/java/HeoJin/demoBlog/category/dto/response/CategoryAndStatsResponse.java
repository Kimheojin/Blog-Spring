package HeoJin.demoBlog.category.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAndStatsResponse {

    private Long categoryId;
    private String categoryName;
    private Long stats;
}
