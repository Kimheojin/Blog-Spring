package HeoJin.demoBlog.post.dto.response;


import HeoJin.demoBlog.post.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostcontractionResponse {

    private String title;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDate;

    public static PostcontractionResponse from(Post post) {
        return PostcontractionResponse.builder()
                .title(post.getTitle())
                .regDate(post.getRegDate())
                .build();
    }
}

