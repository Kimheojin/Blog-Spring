package HeoJin.demoBlog.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( // 외래키
            name = "category_id",
            foreignKey = @ForeignKey(
                    name = "fk_post_category",
                    foreignKeyDefinition = "FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE CASCADE"
            )
    ) // 카테고리 삭제될 때 모든 post 삭제
    private Category category;


}
