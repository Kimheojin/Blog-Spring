package HeoJin.demoBlog.seo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostMongo {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime syncedDate;


}
