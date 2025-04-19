package HeoJin.demoBlog.controller;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.AddCategoryRequest;
import HeoJin.demoBlog.dto.request.DeleteCategoryRequest;
import HeoJin.demoBlog.dto.response.CategoryResponse;
import HeoJin.demoBlog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.CallSite;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CategoryController {

    // 전체 카테고리 반환
    private final CategoryService categoryService;

    // 전체 카테고리 반환

    @GetMapping("/categoryList")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryNames = categoryService.getAllCategoryNames();

        return ResponseEntity.ok(categoryNames);
    }

    // 카테고리 삭제
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/category")
    public ResponseEntity<List<CategoryResponse>> deleteCategory(@RequestBody DeleteCategoryRequest deleteCategoryRequest) {
        categoryService.deleteCategory(deleteCategoryRequest);

        return ResponseEntity.ok(categoryService.getAllCategoryNames());
    }

    // 카테고리 추가
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/category")
    public ResponseEntity<List<CategoryResponse>> postCategory(@RequestBody AddCategoryRequest addCategoryRequest) {
        categoryService.addCategory(addCategoryRequest);
        return ResponseEntity.ok(categoryService.getAllCategoryNames());
    }
}
