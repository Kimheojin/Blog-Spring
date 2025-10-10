package HeoJin.demoBlog.global.InitDB;


import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.comment.entity.Comment;
import HeoJin.demoBlog.comment.entity.CommentStatus;
import HeoJin.demoBlog.comment.repository.CommentRepository;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.member.repository.MemberRepository;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
@Profile({"local"})
public class TestDbInit  {
    
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // postConstruct -> commandLineRunner
    @Transactional
    protected void createCategory(){
        String[] categoryNames = {
                // 나중에 좀 다양하게
                "Java1", "Java2", "Java3", "Java4", "Java5",
                "Java6", "Java7", "Java8", "Java9", "Java10"
        };

        for (String categoryName : categoryNames) {
            // 중복 체크
            if (categoryRepository.findByCategoryName(categoryName).isEmpty()) {
                Category category = Category.builder()
                        .categoryName(categoryName)
                        .build();

                categoryRepository.save(category);
                log.info("카테고리 생성: {}", categoryName);
            }
        }
    }

    @Transactional
    protected void createPost() {
        // 기존 사용자와 카테고리 조회
        Member admin = memberRepository.findByEmail("hurjin1109@naver.com")
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.warn("카테고리가 없어서 포스트를 생성할 수 없습니다.");
            return;
        }

        // 포스트 제목과 내용 데이터
        String[][] postData = {
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

        PostStatus[] statuses = {PostStatus.PUBLISHED, PostStatus.PRIVATE, PostStatus.DRAFT};

        for (int i = 0; i < postData.length; i++) {
            // 랜덤 카테고리와 상태 선택
            Category randomCategory = categories.get(i % categories.size());
            PostStatus randomStatus = statuses[i % 2];


            Post post = Post.builder()
                    .title(postData[i][0])
                    .content(postData[i][1])
                    .member(admin)
                    .category(randomCategory)
                    .status(randomStatus)
                    .regDate(LocalDateTime.now())
                    .build();

            postRepository.save(post);
            log.info("포스트 생성: {} (카테고리: {}, 상태: {})",
                    post.getTitle(), randomCategory.getCategoryName(), randomStatus);
        }

        log.info("총 {} 개의 포스트가 생성되었습니다.", postData.length);
    }


    @Transactional
    protected void createComment() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            log.warn("포스트가 없어서 댓글을 생성할 수 없습니다.");
            return;
        }

        String[] commentContents = {
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

        int commentIndex = 0;
        int totalComments = 0;

        // 각 포스트마다 2-3개의 댓글 생성
        for (Post post : posts) {
            int commentCount = 2 + (commentIndex % 2); // 2개 또는 3개
            Comment parentComment = null;

            for (int i = 0; i < commentCount && commentIndex < commentContents.length; i++) {
                Comment comment = Comment.builder()
                        .content(commentContents[commentIndex])
                        .email("test@naver.com") // 고정 이메일
                        .password("1234") // 고정 비밀번호
                        .post(post)
                        .parent(i == 1 ? parentComment : null) // 두 번째 댓글은 대댓글
                        .status(CommentStatus.ACTIVE)
                        .build();

                commentRepository.save(comment);

                // 첫 번째 댓글을 부모로 설정
                if (i == 0) {
                    parentComment = comment;
                }

                log.info("댓글 생성: {} (포스트: {})",
                        comment.getContent().substring(0, Math.min(15, comment.getContent().length())) + "...",
                        post.getTitle());

                commentIndex++;
                totalComments++;
            }
        }

        log.info("총 {} 개의 댓글이 생성되었습니다.", totalComments);
    }
}