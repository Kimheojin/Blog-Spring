package HeoJin.demoBlog.post.dto.request;


import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private String title;
    private String content;
    private String categoryName;

}
