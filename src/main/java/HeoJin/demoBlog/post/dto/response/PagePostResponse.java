package HeoJin.demoBlog.post.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @param totalElements 전체 게시글 수
 * @param first         첫 페이지여부
 * @param last          마지막 페이지 여부
 */
@Getter
@AllArgsConstructor
@Builder
public record PagePostResponse(List<PostResponse> content, int pageNumber, int pageSize, long totalElements,
                               int totalPages, boolean first, boolean last) {

}
