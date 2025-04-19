package HeoJin.demoBlog.repository;


import HeoJin.demoBlog.domain.Post;
import HeoJin.demoBlog.domain.QCategory;
import HeoJin.demoBlog.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory QFactory;

    @Override
    public List<Post> findByCategoryName(String categoryName) {
        QPost post = QPost.post;
        QCategory category = QCategory.category;

        return QFactory
                .selectFrom(post)
                .join(post.category, category)
                .where(category.categoryName.eq(categoryName))
                .fetch();
    }
}
