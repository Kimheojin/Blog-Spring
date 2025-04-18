package HeoJin.demoBlog.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private String title;
    private String membername;
    private String content;
    private LocalDateTime regDate;


}
