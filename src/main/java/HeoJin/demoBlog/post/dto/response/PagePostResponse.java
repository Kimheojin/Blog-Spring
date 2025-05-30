package HeoJin.demoBlog.post.dto.response;


import HeoJin.demoBlog.post.entity.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagePostResponse {

    private List<PostResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements; // 전체 게시글 수
    private int totalPages;
    private boolean first; // 첫 페이지여부
    private boolean last; // 마지막 페이지 여부

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
