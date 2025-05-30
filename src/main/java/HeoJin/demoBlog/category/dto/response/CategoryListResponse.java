package HeoJin.demoBlog.category.dto.response;


import lombok.Getter;

import java.util.List;

@Getter
public class CategoryListResponse {
    private List<CategoryResponse> categoryResponses;

    public CategoryListResponse(List<CategoryResponse> categoryResponses){
        this.categoryResponses = categoryResponses;
    }

}


