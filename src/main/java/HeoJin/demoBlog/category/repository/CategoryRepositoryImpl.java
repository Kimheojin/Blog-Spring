package HeoJin.demoBlog.category.repository;


import HeoJin.demoBlog.category.dto.data.CategoryWithCountDto;
import HeoJin.demoBlog.category.entity.QCategory;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.entity.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryWithCountDto> findAllCategoriesWithCount() {
        QCategory category = QCategory.category;
        QPost post = QPost.post;

        return jpaQueryFactory
                .select(Projections.constructor(CategoryWithCountDto.class,
                        // 이거 순서 중요
                        category.id,
                        category.categoryName,
                        JPAExpressions
                                .select(post.count())
                                .from(post)
                                .where(post.category.eq(category)
                                        .and(post.status.eq(PostStatus.PUBLISHED))),
                        category.priority))
                .from(category)
                .orderBy(category.priority.asc(), category.categoryName.asc())
                .fetch();
    }
}
