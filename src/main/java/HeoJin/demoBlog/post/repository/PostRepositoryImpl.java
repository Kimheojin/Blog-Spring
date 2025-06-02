package HeoJin.demoBlog.post.repository;


import HeoJin.demoBlog.category.entity.QCategory;
import HeoJin.demoBlog.member.entity.QMember;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.PostStatus;
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
    public Page<Post> findPublishedPostsWithFetch(Pageable pageable) {

        List<Post> posts = QFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .join(post.category,category).fetchJoin()
                .where(post.status.eq(PostStatus.PUBLISHED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = QFactory
                .select(post.count())
                .where(post.status.eq(PostStatus.PUBLISHED))
                .from(post)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findPublishedCategoryWithFetch(String categoryName, Pageable pageable) {

        List<Post> posts = QFactory
                .selectFrom(post)
                .join(post.category, category).fetchJoin()
                .join(post.member, member).fetchJoin()
                .where(category.categoryName.eq(categoryName))
                .where(post.status.eq(PostStatus.PUBLISHED))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long totalCount = QFactory
                .select(post.count())
                .where(category.categoryName.eq(categoryName))
                .where(post.status.eq(PostStatus.PUBLISHED))
                .from(post)
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;


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
