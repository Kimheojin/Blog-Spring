package HeoJin.demoBlog.post.dto.response;


import lombok.Builder;

import java.util.List;


@Builder
public record PagePostResponse(List<PostResponse> content, int pageNumber,
                               int pageSize, long totalElements,
                               int totalPages, boolean first, boolean last) {
}
