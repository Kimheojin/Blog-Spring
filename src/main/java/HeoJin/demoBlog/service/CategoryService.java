package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.dto.response.CategoryResponse;
import HeoJin.demoBlog.exception.CategoryAlreadyExist;
import HeoJin.demoBlog.exception.CategoryNotFound;
import HeoJin.demoBlog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {


    private final CategoryRepository categoryRepository;

    
    // 모든 카테고리 목록 반환
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoryNames() {

        List<Category> categories = new ArrayList<>();


        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .categoryName(category.getCategoryname())
                        .build())
                .collect(Collectors.toList());
    }
    
    
    // 카테고리 단일 삭제
    @Transactional
    public void deleteCategory(DeleteCategoryRequest deleteCategoryRequest) {
        Category category = categoryRepository.findByCategoryname(deleteCategoryRequest.getCategoryname())
                .orElseThrow(() -> new CategoryNotFound());
        categoryRepository.delete(category);
    }

    // 카테고리 단일 추가
    @Transactional
    public void addCategory(AddCategoryRequest addCategoryRequest) {
        if(categoryRepository.findByCategoryname(addCategoryRequest.getCategoryname()).isEmpty()){
            categoryRepository.save(Category.builder()
                    .categoryname(addCategoryRequest.getCategoryname()).build());
        }else{
            throw new CategoryAlreadyExist();
        }
    }

}
