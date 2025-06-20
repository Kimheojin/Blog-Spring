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
import java.time.ZoneId;
import java.util.List;

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

    // Post 관련
    @Transactional
    protected void saveAllPosts(Member member) {
        String[][] posts = getPostDataSet();
        String[] categories = getCategoryDataSet();

        // 카테고리가 없으면 먼저 생성
        saveAllCategories();
        entityManager.flush();

        // PostStatus 배열 (균등 분배용)
        PostStatus[] statusArray = {PostStatus.PRIVATE, PostStatus.PUBLISHED};

        for (int i = 0; i < posts.length; i++) {
            Category category = findCategoryByName(categories[i % categories.length]);
            if (category == null) {
                throw new RuntimeException("카테고리를 찾을 수 없습니다");
            }

            // 4개 상태를 순환하며 균등하게 분배
            PostStatus status = statusArray[i % statusArray.length];
            savePost(posts[i][0], posts[i][1], member, category, status);
        }
    }

    protected void savePost(String title, String content, Member member, Category category, PostStatus status) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .category(category)
                .status(status)
                .regDate(LocalDateTime.now())
                .build();
        entityManager.persist(post);
    }

    // Comment 관련
    protected Comment saveCommentWithStatus(String content, String email, String password, Post post, Comment parent, LocalDateTime regDate, CommentStatus status) {
        Comment comment = Comment.builder()
                .content(content)
                .email(email)
                .password(password)
                .post(post)
                .parent(parent)
                .status(status)  // 전달받은 status 사용
                .regDate(regDate)
                .build();
        entityManager.persist(comment);
        return comment;
    }

    // 기존 메서드는 ACTIVE로 고정 (하위 호환성)
    protected Comment saveComment(String content, String email, String password, Post post, Comment parent, LocalDateTime regDate) {
        return saveCommentWithStatus(content, email, password, post, parent, regDate, CommentStatus.ACTIVE);
    }

    @Transactional
    protected void saveAllComments() {
        String[] comments = getCommentDataSet();
        List<Post> posts = findAllPosts();

        if (posts.isEmpty()) {
            return;
        }

        // CommentStatus 배열 (균등 분배용)
        CommentStatus[] statusArray = {CommentStatus.ACTIVE, CommentStatus.DELETED};

        int commentIndex = 0;
        LocalDateTime baseTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(30);

        for (Post post : posts) {
            int commentCount = 2 + (commentIndex % 2);
            Comment parentComment = null;

            for (int i = 0; i < commentCount && commentIndex < comments.length; i++) {
                LocalDateTime commentTime = baseTime.plusHours(commentIndex * 2);

                // 3개 상태를 순환하며 균등하게 분배
                CommentStatus status = statusArray[commentIndex % statusArray.length];

                Comment comment = saveCommentWithStatus(
                        comments[commentIndex],
                        "test@naver.com",
                        "1234",
                        post,
                        i == 1 ? parentComment : null,
                        commentTime,
                        status
                );

                if (i == 0) {
                    parentComment = comment;
                }
                commentIndex++;
            }
        }
    }

    private List<Post> findAllPosts() {
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post p", Post.class);
        return query.getResultList();
    }

    // 데이터셋 메서드
    protected String[] getCategoryDataSet() {
        return new String[]{
                "Java1", "Java2"
        };
    }

    protected String[][] getPostDataSet() {
        return new String[][]{
                {"Java 기초 문법 정리", "Java의 기본 문법과 객체지향 프로그래밍에 대해 알아보겠습니다."},
                {"Spring Boot 시작하기", "Spring Boot로 웹 애플리케이션을 개발하는 방법을 소개합니다."},
                {"React Hook 사용법", "React의 useState, useEffect 등 주요 Hook들의 사용법을 정리했습니다."},
                {"JavaScript ES6+ 문법", "최신 JavaScript 문법과 기능들을 예제와 함께 설명합니다."},
                {"MySQL 최적화 팁", "데이터베이스 성능을 향상시키는 다양한 방법들을 알아봅시다."},
                {"알고리즘 문제 해결", "코딩테스트에서 자주 나오는 알고리즘 문제 풀이법입니다."},
                {"Docker 컨테이너 활용", "Docker를 이용한 개발 환경 구축과 배포 방법을 다룹니다."},
                {"AWS EC2 배포하기", "AWS EC2 인스턴스에 애플리케이션을 배포하는 과정을 설명합니다."},
                {"Git 브랜치 전략", "효율적인 Git 브랜치 관리와 협업 방법을 소개합니다."},
                {"DevOps 파이프라인", "CI/CD 파이프라인 구축과 자동화에 대해 알아봅시다."}
        };
    }

    protected String[] getCommentDataSet() {
        return new String[]{
                "정말 유익한 글이네요! 감사합니다.",
                "이해하기 쉽게 설명해주셔서 고맙습니다.",
                "궁금했던 내용인데 도움이 많이 되었어요.",
                "실무에서 바로 적용해볼 수 있을 것 같습니다.",
                "더 자세한 설명도 부탁드립니다.",
                "예제 코드가 정말 도움이 되었습니다.",
                "이런 내용을 찾고 있었는데 딱이네요!",
                "초보자도 이해하기 쉽게 작성해주셨네요.",
                "다음 편도 기대하겠습니다.",
                "공유해주셔서 감사합니다!"
        };
    }
}