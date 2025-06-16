package HeoJin.demoBlog.comment.repository;


import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;
import HeoJin.demoBlog.comment.entity.QComment;
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

        return jpaQueryFactory
                .selectFrom(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.status.eq(CommentStatus.ACTIVE)
                )
                .orderBy(comment.id.asc())
                .fetch();
    }

    @Override
    public List<Comment> customFindAllCommentByPostIdForAdmin(Long postId){
        QComment comment = QComment.comment;

        return jpaQueryFactory
                .selectFrom(comment)
                .where(
                        comment.post.id.eq(postId)
                )
                .orderBy(comment.id.asc())
                .fetch();

    }
}
