package HeoJin.demoBlog.category.service;


import HeoJin.demoBlog.category.dto.response.CategoryResponse;
import HeoJin.demoBlog.category.dto.data.CategoryWithCountDto;
import HeoJin.demoBlog.category.dto.response.CategoryWithCountResponse;
import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    
    // 모든 카테고리 목록 반환
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoryNames() {

        List<Category> categories = categoryRepository.findAll();


        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .categoryName(category.getCategoryName())
                        .categoryId(category.getId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<CategoryWithCountResponse> getCategoriesWithPublishedStats() {
        List<CategoryWithCountDto> results = categoryRepository.findALlCategoriesWithCount();

        return results.stream()
                .map(dto -> CategoryWithCountResponse.builder()
                        .categoryId(dto.getCategoryId())
                        .categoryName(dto.getCategoryName())
                        .postCount(dto.getPostCount())
                        .build())
                .collect(Collectors.toList());
    }
    
    

}
