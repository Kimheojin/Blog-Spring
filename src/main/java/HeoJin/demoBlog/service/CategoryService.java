package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.dto.response.CategoryResponse;
import HeoJin.demoBlog.exception.CategoryAlreadyExist;
import HeoJin.demoBlog.exception.CustomNotFound;
import HeoJin.demoBlog.repository.CategoryRepository;
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
                        .build())
                .collect(Collectors.toList());
    }
    
    
    // 카테고리 단일 삭제
    @Transactional
    public void deleteCategory(DeleteCategoryRequest deleteCategoryRequest) {
        Category category = categoryRepository.findByCategoryName(deleteCategoryRequest.getCategoryName())
                .orElseThrow(() -> new CustomNotFound("카테고리"));
        categoryRepository.delete(category);
    }

    // 카테고리 단일 추가
    @Transactional
    public void addCategory(AddCategoryRequest addCategoryRequest) {
        if(categoryRepository.findByCategoryName(addCategoryRequest.getCategoryName()).isEmpty()){
            categoryRepository.save(Category.builder()
                    .categoryName(addCategoryRequest.getCategoryName()).build());
        }else{
            throw new CategoryAlreadyExist();
        }
    }

}
