package HeoJin.demoBlog.category.controller;

import HeoJin.demoBlog.category.dto.response.CategoryAndStatsListResponse;
import HeoJin.demoBlog.category.dto.response.CategoryListResponse;
import HeoJin.demoBlog.category.dto.response.CategoryResponse;
import HeoJin.demoBlog.category.dto.response.CategoryWithCountResponse;
import HeoJin.demoBlog.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PublicCategoryController {

    private final CategoryService categoryService;

    // 전체 카테고리 반환
    @GetMapping("/categories")
    public ResponseEntity<CategoryListResponse> getAllCategories() {
        List<CategoryResponse> categoryNames = categoryService.getAllCategoryNames();
        return ResponseEntity.ok(new CategoryListResponse(categoryNames));
    }

    // 포스트 수가 포함된 카테고리 목록 (PUBLISHED + 전체 카테고리 수도 추가해서)
    @GetMapping("/categories/stats")
    public ResponseEntity<CategoryAndStatsListResponse> getPostCategoryAndStats(){
        List<CategoryWithCountResponse> categoryResponses = categoryService.getCategoriesWithPublishedStats();
        return ResponseEntity.ok(new CategoryAndStatsListResponse(categoryResponses));
    }


}