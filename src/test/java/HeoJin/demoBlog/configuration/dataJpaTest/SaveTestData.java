package HeoJin.demoBlog.configuration.dataJpaTest;

import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;
import HeoJin.demoBlog.comment.repository.CommentRepository;
import HeoJin.demoBlog.configuration.Integration.BaseController;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.member.entity.Role;
import HeoJin.demoBlog.member.repository.MemberRepository;
import HeoJin.demoBlog.member.repository.RoleRepository;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class SaveTestData extends BaseController {

    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;

    // Member 관련

    @Transactional
    protected Member createTestMember() {
        String email = "test@test.com";
        String password = "testPassword";
        String memberName = "testName";
        String roleName = "ADMIN";

        // annotation과 동일 - 이미 존재하는지 확인 (중복 방지)
        return memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Role 생성 또는 조회
                    Role role = roleRepository.findByRoleName(roleName)
                            .orElseGet(() -> {
                                Role newRole = Role.builder()
                                        .roleName(roleName)
                                        .build();
                                return roleRepository.save(newRole);
                            });

                    // Member 생성
                    Member member = Member.builder()
                            .memberName(memberName)
                            .email(email)
                            .password(passwordEncoder.encode(password))
                            .role(role)
                            .build();

                    return memberRepository.save(member);
                });
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
        // 이미 존재하는지 확인 (중복 방지)
        categoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    Category category = Category.builder()
                            .categoryName(categoryName)
                            .build();
                    return categoryRepository.save(category);
                });
    }

    // Post관련
    @Transactional
    protected void saveAllPosts(Member member) {
        String[][] posts = getPostDataSet();
        String[] categories = getCategoryDataSet();

        // 카테고리가 없으면 먼저 생성
        saveAllCategories();

        for (int i = 0; i < posts.length; i++) {
            Category category = categoryRepository.findByCategoryName(categories[i % categories.length])
                    .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));

            PostStatus status = (i % 2 == 0) ? PostStatus.PUBLISHED : PostStatus.PRIVATE;
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
        postRepository.save(post);
    }



    // 시간 간격을 두고 댓글 생성하는 메서드 추가
    protected Comment saveComment(String content, String email, String password, Post post, Comment parent, LocalDateTime regDate) {
        Comment comment = Comment.builder()
                .content(content)
                .email(email)
                .password(password)
                .post(post)
                .parent(parent)
                .status(CommentStatus.ACTIVE)
                .regDate(regDate)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    protected void saveAllComments() {
        String[] comments = getCommentDataSet();
        var posts = postRepository.findAll();

        if (posts.isEmpty()) {
            return;
        }

        int commentIndex = 0;
        LocalDateTime baseTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(30); // 30일 전부터 시작

        for (Post post : posts) {
            int commentCount = 2 + (commentIndex % 2); // 2개 또는 3개
            Comment parentComment = null;

            for (int i = 0; i < commentCount && commentIndex < comments.length; i++) {
                // 댓글마다 시간 간격을 두어 realistic한 데이터 생성
                LocalDateTime commentTime = baseTime.plusHours(commentIndex * 2); // 2시간씩 간격

                Comment comment = saveComment(
                        comments[commentIndex],
                        "test@naver.com",
                        "1234",
                        post,
                        i == 1 ? parentComment : null, // 두 번째 댓글은 대댓글
                        commentTime
                );

                if (i == 0) {
                    parentComment = comment;
                }
                commentIndex++;
            }
        }
    }

    // 데이터셋

    // 카테고리 데이터셋
    protected String[] getCategoryDataSet() {
        return new String[]{
                "Java1", "Java2", "Java3", "Java4", "Java5",
                "Java6", "Java7", "Java8", "Java9", "Java10"
        };
    }

    // 포스트 데이터셋 [제목, 내용]
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

    // 댓글 데이터셋
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