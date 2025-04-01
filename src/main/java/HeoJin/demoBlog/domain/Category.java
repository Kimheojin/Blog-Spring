package HeoJin.demoBlog.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long Id;
    @Column(name = "category_name")
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

}
