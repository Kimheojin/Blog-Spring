package HeoJin.demoBlog.category.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;


    @Column(unique = true)
    private String categoryName;

    @Column(nullable = false)
    @Builder.Default
    private Long priority = 0L;

    public void updatePriority(Long priority){
        this.priority = priority;
    }

    public void updateCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
