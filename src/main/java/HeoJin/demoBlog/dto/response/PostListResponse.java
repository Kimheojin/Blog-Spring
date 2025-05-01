package HeoJin.demoBlog.dto.response;


import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponse {

    private List<PostResponse> posts;

    public PostListResponse(List<PostResponse> posts){
        this.posts = posts;
    }
}
