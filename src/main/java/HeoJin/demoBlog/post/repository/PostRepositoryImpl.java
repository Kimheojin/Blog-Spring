package HeoJin.demoBlog.post.repository;


import HeoJin.demoBlog.category.entity.QCategory;
import HeoJin.demoBlog.member.entity.QMember;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory QFactory;

    // Q객체
    private static final QPost post = QPost.post;
    private static final QMember member = QMember.member;
    private static final QCategory category = QCategory.category;

    @Override
    public Page<Post> findAllWithFetch(Pageable pageable) {

        List<Post> posts = QFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .join(post.category,category).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = QFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findByCategoryWithFetch(String categoryName, Pageable pageable) {

        List<Post> posts = QFactory
                .selectFrom(post)
                .join(post.category, category).fetchJoin()
                .join(post.member, member).fetchJoin()
                .where(category.categoryName.eq(categoryName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = QFactory
                .select(post.count())
                .from(post)
                .join(post.category, category)
                // 내부조인이라 그냥 where
                .where(category.categoryName.eq(categoryName))
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Optional<Post> findByIdWithMemberAndCategory(Long postId) {

        Post result = QFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .join(post.category, category).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
