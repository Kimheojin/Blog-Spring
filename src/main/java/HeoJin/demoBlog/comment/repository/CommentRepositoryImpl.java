package HeoJin.demoBlog.comment.repository;


import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.QComment;
import HeoJin.demoBlog.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> customFindCommentsByPostId(Long postId) {
        QComment comment = QComment.comment;
        QPost post = QPost.post;
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.post, post).fetchJoin()
                .leftJoin(comment.parent).fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.id.asc())
                .fetch();
    }
}
