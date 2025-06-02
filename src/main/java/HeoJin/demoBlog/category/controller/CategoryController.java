package HeoJin.demoBlog.category.controller;

import HeoJin.demoBlog.category.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.category.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.category.dto.request.ModifyCategoryName;
import HeoJin.demoBlog.category.dto.response.CategoryListResponse;
import HeoJin.demoBlog.category.dto.response.CategoryResponse;
import HeoJin.demoBlog.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    // 전체 카테고리 반환
    @GetMapping("/categories")
    public ResponseEntity<CategoryListResponse> getAllCategories() {
        List<CategoryResponse> categoryNames = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(categoryNames));
    }

    // 포스트 수가 포함된 카테고리 목록 (상태도 확인 해야 할듯)
    // 일단 임시로
    @GetMapping("/categories/stats")
    public ResponseEntity<CategoryListResponse> getPostCategoryAndstats(){
        List<CategoryResponse> categoryResponses = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(categoryResponses));
    }

    // 카테고리 삭제
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/categories")
    public ResponseEntity<CategoryListResponse> deleteCategory(@RequestBody DeleteCategoryRequest deleteCategoryRequest) {
        categoryService.deleteCategory(deleteCategoryRequest);
        List<CategoryResponse> updatedCategories = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(updatedCategories));
    }

    // 카테고리 추가
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/categories")
    public ResponseEntity<CategoryListResponse> postCategory(@RequestBody AddCategoryRequest addCategoryRequest) {
        categoryService.addCategory(addCategoryRequest);
        List<CategoryResponse> updatedCategories = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(updatedCategories));
    }
    // 카테고리 이름 변경
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/categories")
    public ResponseEntity<CategoryListResponse> modifyCategory(@RequestBody ModifyCategoryName modifyCategoryName){
        categoryService.updateCategory(modifyCategoryName);
        
        // 전체 카테고리 반환
        List<CategoryResponse> updatedCategories = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(updatedCategories));
    }
}