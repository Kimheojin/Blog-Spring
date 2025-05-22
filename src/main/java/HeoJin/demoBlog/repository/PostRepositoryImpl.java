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

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory QFactory;

    @Override
    public Page<Post> findAllPosts(Pageable pageable) {
        QPost post = QPost.post;
        QMember member = QMember.member;

        List<Post> posts = QFactory
                .selectFrom(post)
                .join(post.member, member).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(post.regDate.desc()) 인덱스 걸면? 하는 게 좋을듯
                .fetch();

        long total = QFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Page<Post> findByCategoryName(String categoryName, Pageable pageable) {
        QPost post = QPost.post;
        QCategory category = QCategory.category;

        List<Post> posts = QFactory
                .selectFrom(post)
                .join(post.category, category).fetchJoin()
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
}
