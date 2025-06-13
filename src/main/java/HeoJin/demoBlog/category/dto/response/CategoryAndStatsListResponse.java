package HeoJin.demoBlog.category.dto.response;

import java.util.List;

public record CategoryAndStatsListResponse(List<CategoryWithCountResponse> CategoryWithCountResponses) {
}
