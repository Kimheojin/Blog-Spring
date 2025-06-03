package HeoJin.demoBlog.category.service;

import HeoJin.demoBlog.category.dto.response.CategoryResponse;
import HeoJin.demoBlog.category.entity.Category;

public class CategoryMapper {

    // 전체 조회 반환 관련
    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
