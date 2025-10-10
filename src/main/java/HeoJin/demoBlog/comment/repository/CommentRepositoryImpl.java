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
    private static final QComment comment = QComment.comment;

    @Override
    public List<Comment> customFindCommentsByPostId(Long postId) {
        // in 절로 최적화 가능한지 확인해 보기

        return jpaQueryFactory
                .selectFrom(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.status.eq(CommentStatus.ACTIVE).or(comment.status.eq(CommentStatus.DELETED))
                )
                .orderBy(comment.id.asc())
                .fetch();
    }

    @Override
    public List<Comment> customFindAllCommentByPostIdForAdmin(Long postId){

        return jpaQueryFactory
                .selectFrom(comment)
                .where(
                        comment.post.id.eq(postId)
                )
                .orderBy(comment.id.asc())
                .fetch();

    }
}
