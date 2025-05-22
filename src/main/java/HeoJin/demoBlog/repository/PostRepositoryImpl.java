package HeoJin.demoBlog.repository;


import HeoJin.demoBlog.domain.Post;
import HeoJin.demoBlog.domain.QCategory;
import HeoJin.demoBlog.domain.QMember;
import HeoJin.demoBlog.domain.QPost;
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
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory QFactory;

    // Q객체
    private static final QPost post = QPost.post;
    private static final QMember member = QMember.member;
    private static final  QCategory category = QCategory.category;

    @Override
    public Page<Post> findAllPosts(Pageable pageable) {

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
    public Page<Post> findByCategoryName(String categoryName, Pageable pageable) {

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
    public Optional<Post> findPostWithMemberAndCategory(Long postId) {

        Post result = QFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .join(post.category, category).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
