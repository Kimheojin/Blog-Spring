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
    private Long Id;


    @Column(unique = true)
    private String categoryName;

    public void updateCategoryName(String categoryName){
        this.categoryName = categoryName;
    }
}
