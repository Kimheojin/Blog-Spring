package HeoJin.demoBlog.post.dto.response;


import HeoJin.demoBlog.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long postId;
    private String title;
    private String memberName;
    private String content;
    private String categoryName;
    // 상태 관련 추가(enum 타입)
    private String status;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .memberName(post.getMember().getMemberName())
                .categoryName(post.getCategory().getCategoryName())
                .status(post.getStatus().name())
                .regDate(post.getRegDate())
                .build();
    }
}
