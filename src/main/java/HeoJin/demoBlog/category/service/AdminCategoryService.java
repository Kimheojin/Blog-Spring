package HeoJin.demoBlog.category.service;


import HeoJin.demoBlog.category.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.category.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.category.dto.request.ModifyCategoryNameRequest;
import HeoJin.demoBlog.category.dto.response.CategoryResponse;
import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.global.exception.CategoryAlreadyExist;
import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.global.exception.ExistCategoryPostException;
import HeoJin.demoBlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    // 카테고리 단일 삭제
    @Transactional
    public List<CategoryResponse> deleteCategoryAndGetAll(DeleteCategoryRequest deleteCategoryRequest) {

        Long categoryId = deleteCategoryRequest.getCategoryId();


        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomNotFound("카테고리"));
        if(postRepository.existsByCategoryId(categoryId)){
            throw new ExistCategoryPostException();
        }

        categoryRepository.delete(category);

        return categoryRepository.findAll()
                .stream().map(CategoryMapper::toCategoryResponse)
                .toList();
    }

    // 카테고리 단일 추가
    @Transactional
    public List<CategoryResponse> addCategoryAndGetAll(AddCategoryRequest addCategoryRequest) {
        if(categoryRepository.findByCategoryName(addCategoryRequest.getCategoryName()).isEmpty()){
            categoryRepository.save(Category.builder()
                    .categoryName(addCategoryRequest.getCategoryName())
                    .build());

            return categoryRepository.findAll().stream()
                    .map(CategoryMapper::toCategoryResponse)
                    .collect(Collectors.toList());
        }else{
            throw new CategoryAlreadyExist();
        }
    }

    // 카테고리 이름 수정
    @Transactional
    public List<CategoryResponse> updateCategoryAndGetAll(ModifyCategoryNameRequest modifyCategoryNameRequest) {
        Category category = categoryRepository.findById(modifyCategoryNameRequest.getCategoryId())
                .orElseThrow(() -> new CustomNotFound("해당 카테고리가 존재하지 않습니다."));

        // 변경 감지
        category.updateCategoryName(modifyCategoryNameRequest.getWantCategoryName());

        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }
}
