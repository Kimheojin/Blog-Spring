package HeoJin.demoBlog.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
        private Long categoryId;
        private String categoryName;
}
