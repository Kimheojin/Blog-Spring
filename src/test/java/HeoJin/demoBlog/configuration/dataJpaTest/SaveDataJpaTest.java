package HeoJin.demoBlog.configuration.dataJpaTest;

import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.member.entity.Role;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.PostStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public abstract class SaveDataJpaTest {
    protected EntityManager entityManager;
    protected BCryptPasswordEncoder passwordEncoder;

    protected void initializeTestData(EntityManager entityManager, BCryptPasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    // Member 관련
    @Transactional
    protected Member createTestMember() {
        String email = "test@test.com";
        String password = "testPassword";
        String memberName = "testName";
        String roleName = "ADMIN";

        // 이미 존재하는 멤버 확인
        Member existingMember = findMemberByEmail(email);
        if (existingMember != null) {
            return existingMember;
        }

        // Role 생성 또는 조회
        Role role = findRoleByName(roleName);
        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .build();
            entityManager.persist(role);
            entityManager.flush();
        }

        // Member 생성
        Member member = Member.builder()
                .memberName(memberName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        entityManager.persist(member);
        entityManager.flush();
        return member;
    }

    private Member findMemberByEmail(String email) {
        try {
            TypedQuery<Member> query = entityManager.createQuery(
                    "SELECT m FROM Member m WHERE m.email = :email", Member.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private Role findRoleByName(String roleName) {
        try {
            TypedQuery<Role> query = entityManager.createQuery(
                    "SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class);
            query.setParameter("roleName", roleName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Category 관련
    @Transactional
    protected void saveAllCategories() {
        String[] categories = getCategoryDataSet();
        for (String categoryName : categories) {
            saveCategory(categoryName);
        }
    }

    protected void saveCategory(String categoryName) {
        Category existingCategory = findCategoryByName(categoryName);
        if (existingCategory == null) {
            Category category = Category.builder()
                    .categoryName(categoryName)
                    .build();
            entityManager.persist(category);
        }
    }

    private Category findCategoryByName(String categoryName) {
        try {
            TypedQuery<Category> query = entityManager.createQuery(
                    "SELECT c FROM Category c WHERE c.categoryName = :categoryName", Category.class);
            query.setParameter("categoryName", categoryName);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // Post 생성 메서드
    protected Post createPost(Member member, Category category, PostStatus status, String suffix) {
        return Post.builder()
                .member(member)
                .category(category)
                .status(status)
                .content("test 내용입니다 " + suffix)
                .title("test 제목입니다 " + suffix)
                .regDate(LocalDateTime.now())
                .build();
    }

    // 데이터셋 메서드
    protected String[] getCategoryDataSet() {
        return new String[]{
                "Java1", "Java2"
        };
    }

    protected Comment createComment(String email, Post post, String content, CommentStatus commentStatus) {
        return Comment.builder()
                .email(email)  // 또는 email/password 방식이면 그에 맞게
                .post(post)
                .content(content)
                .status(commentStatus)
                .regDate(LocalDateTime.now())
                .build();
    }
    protected Comment createCommentWithParent(String email, Post post, String content, CommentStatus status, Comment parent) {
        return Comment.builder()
                .email(email)
                .password("1234")
                .post(post)
                .content(content)
                .status(status)
                .parent(parent)
                .regDate(LocalDateTime.now())
                .build();
    }
}