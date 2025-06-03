package HeoJin.demoBlog.category.service;

import HeoJin.demoBlog.category.dto.data.CategoryWithCountDto;
import HeoJin.demoBlog.category.dto.response.CategoryResponse;
import HeoJin.demoBlog.category.dto.response.CategoryWithCountResponse;
import HeoJin.demoBlog.category.entity.Category;

public class CategoryMapper {
    public static CategoryResponse toCategoryResponse(Category category){
        return CategoryResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }

    public static CategoryWithCountResponse toCategoryWithCountResponse (CategoryWithCountDto categoryWithCountDto){
        return CategoryWithCountResponse.builder()
                .categoryId(categoryWithCountDto.getCategoryId())
                .categoryName(categoryWithCountDto.getCategoryName())
                .postCount(categoryWithCountDto.getPostCount())
                .build();
    }

}
