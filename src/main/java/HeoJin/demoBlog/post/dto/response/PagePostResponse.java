package HeoJin.demoBlog.post.dto.response;


import HeoJin.demoBlog.post.entity.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagePostResponse {

    private final List<PostResponse> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements; // 전체 게시글 수
    private final int totalPages;
    private final boolean first; // 첫 페이지여부
    private final boolean last; // 마지막 페이지 여부

    public PagePostResponse(List<PostResponse> content, Page<Post> postPage) {
        this.content = content;
        this.pageNumber = postPage.getNumber();
        this.pageSize = postPage.getSize();
        this.totalElements = postPage.getTotalElements();
        this.totalPages = postPage.getTotalPages();
        this.first = postPage.isFirst();
        this.last = postPage.isLast();
    }
}
