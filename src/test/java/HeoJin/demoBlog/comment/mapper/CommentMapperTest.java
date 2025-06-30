package HeoJin.demoBlog.comment.mapper;

import HeoJin.demoBlog.comment.dto.Response.CommentDto;
import HeoJin.demoBlog.comment.dto.request.CommentWriteRequest;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;
import HeoJin.demoBlog.comment.service.CommentMapper;
import HeoJin.demoBlog.post.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CommentMapperTest {

    @Test
    @DisplayName("Comment -> CommentDto 정상 작동 테스트")
    void test1() {
        // given
        Post mockPost = Post.builder()
                .id(1L)
                .build();
        Comment parentComment = Comment.builder()
                .id(2L)
                .build();

        Comment comment = Comment.builder()
                .id(10L)
                .content("테스트 댓글입니다")
                .email("test@example.com")
                .post(mockPost)
                .parent(parentComment)
                .status(CommentStatus.ACTIVE)
                .regDate(LocalDateTime.now())
                .build();


        // when
        CommentDto result = CommentMapper.toCommentDto(comment);
        // then
        Assertions.assertEquals(comment.getId(), result.getId());
        Assertions.assertEquals(comment.getContent(), result.getContent());
        Assertions.assertEquals(comment.getEmail(), result.getEmail());
        Assertions.assertEquals(comment.getPost().getId(), result.getPostId());
        Assertions.assertEquals(comment.getParent().getId(), result.getParentId());
        Assertions.assertEquals(comment.getStatus(), result.getStatus());
        Assertions.assertEquals(comment.getRegDate(), result.getRegDate());
    }

    @Test
    @DisplayName("toCommentDto -> parent가 null일 때 parentId도 null")
    void test2() {
        // given
        Post mockPost = Post.builder()
                .id(1L)
                .build();

        Comment comment = Comment.builder()
                .id(10L)
                .content("부모 댓글이 없는 댓글")
                .email("test@example.com")
                .post(mockPost)
                .status(CommentStatus.ACTIVE)
                .parent(null)  // 부모 댓글 없음
                .regDate(LocalDateTime.now())
                .build();

        // when
        CommentDto result = CommentMapper.toCommentDto(comment);

        // then
        Assertions.assertEquals(comment.getId(), result.getId());
        Assertions.assertEquals(comment.getStatus(), result.getStatus());
        Assertions.assertNull(result.getParentId());
    }

    @Test
    @DisplayName("toCommentAdminDto -> ACTIVE 상태일 때 원본 내용 반환")
    void test3() {
        // given
        Post mockPost = Post.builder()
                .id(1L)
                .build();

        Comment parentComment = Comment.builder()
                .id(2L)
                .build();

        Comment comment = Comment.builder()
                .id(10L)
                .content("활성 댓글입니다")
                .email("test@example.com")
                .status(CommentStatus.ACTIVE)  // 활성 상태
                .post(mockPost)
                .parent(parentComment)
                .regDate(LocalDateTime.now())
                .build();

        // when
        CommentDto result = CommentMapper.toCommentAdminDto(comment);

        // then
        Assertions.assertEquals(comment.getId(), result.getId());
        Assertions.assertEquals(comment.getContent(), result.getContent());
        Assertions.assertEquals(comment.getEmail(), result.getEmail());
        Assertions.assertEquals(comment.getPost().getId(), result.getPostId());
        Assertions.assertEquals(comment.getParent().getId(), result.getParentId());
        Assertions.assertEquals(comment.getStatus(), result.getStatus());
        Assertions.assertEquals(comment.getRegDate(), result.getRegDate());
    }

    @Test
    @DisplayName("toCommentDto -> DELETED 상태일 때 '사용자 삭제된 댓글입니다' 반환")
    void test4() {
        // given
        Post mockPost = Post.builder()
                .id(1L)
                .build();

        Comment comment = Comment.builder()
                .id(10L)
                .content("원본 댓글 내용")
                .email("test@example.com")
                .status(CommentStatus.DELETED)
                .post(mockPost)
                .parent(null)
                .regDate(LocalDateTime.now())
                .build();

        // when
        CommentDto result = CommentMapper.toCommentDto(comment);

        // then
        Assertions.assertEquals(comment.getId(), result.getId());
        Assertions.assertEquals("사용자 삭제된 댓글입니다", result.getContent());
        Assertions.assertEquals(comment.getEmail(), result.getEmail());
        Assertions.assertEquals(comment.getStatus(), result.getStatus());
        Assertions.assertNull(result.getParentId());
    }

    @Test
    @DisplayName("toComment -> CommentWriteRequest를 Comment로 변환 (부모 댓글 있음)")
    void test5() {
        // given
        CommentWriteRequest request = CommentWriteRequest.builder()
                .email("writer@example.com")
                .content("새로운 댓글입니다")
                .password("password123")
                .build();

        Post post = Post.builder()
                .id(1L)
                .build();

        Comment parentComment = Comment.builder()
                .id(5L)
                .build();

        // when
        Comment result = CommentMapper.toComment(request, post, parentComment);

        // then
        Assertions.assertEquals(request.getEmail(), result.getEmail());
        Assertions.assertEquals(request.getContent(), result.getContent());
        Assertions.assertEquals(request.getPassword(), result.getPassword());
        Assertions.assertEquals(post, result.getPost());
        Assertions.assertEquals(parentComment, result.getParent());
    }

    @Test
    @DisplayName("toComment -> CommentWriteRequest를 Comment로 변환 (부모 댓글 없음)")
    void test6() {
        // given
        CommentWriteRequest request = CommentWriteRequest.builder()
                .email("writer@example.com")
                .content("최상위 댓글입니다")
                .password("password123")
                .build();

        Post post = Post.builder()
                .id(1L)
                .build();

        // when
        Comment result = CommentMapper.toComment(request, post, null); // 부모 댓글 없음

        // then
        Assertions.assertEquals(request.getEmail(), result.getEmail());
        Assertions.assertEquals(request.getContent(), result.getContent());
        Assertions.assertEquals(request.getPassword(), result.getPassword());
        Assertions.assertEquals(post, result.getPost());
        Assertions.assertNull(result.getParent()); // 부모 댓글 null
        Assertions.assertNotNull(result.getRegDate());
    }
}
