package HeoJin.demoBlog.category.controller;

import HeoJin.demoBlog.category.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.category.dto.request.DeleteCategoryRequest;
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
    @GetMapping("/categoryList")
    public ResponseEntity<CategoryListResponse> getAllCategories() {
        List<CategoryResponse> categoryNames = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(categoryNames));
    }

    // 카테고리 삭제
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/category")
    public ResponseEntity<CategoryListResponse> deleteCategory(@RequestBody DeleteCategoryRequest deleteCategoryRequest) {
        categoryService.deleteCategory(deleteCategoryRequest);
        List<CategoryResponse> updatedCategories = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(updatedCategories));
    }

    // 카테고리 추가
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/category")
    public ResponseEntity<CategoryListResponse> postCategory(@RequestBody AddCategoryRequest addCategoryRequest) {
        categoryService.addCategory(addCategoryRequest);
        List<CategoryResponse> updatedCategories = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(updatedCategories));
    }
}