package HeoJin.demoBlog.category.repository;


import HeoJin.demoBlog.category.dto.data.CategoryWithCountDto;
import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.configuration.dataJpaTest.SaveDataJpaTest;
import HeoJin.demoBlog.global.config.QuerydslConfig;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@DataJpaTest
@Import(QuerydslConfig.class)
public class CategoryQuerydslRepositoryTest extends SaveDataJpaTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp(){
        EntityManager em = entityManager.getEntityManager();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        initializeTestData(em, encoder);
    }

    @Test
    @DisplayName("CategoryJpaRepositoryTest -> 정상 작동 테스트")
    void test1(){
        // given
        Member testMember = createTestMember();
        saveAllCategories();
        saveAllPosts(testMember);

        // when
        List<CategoryWithCountDto> allCategoriesWithCount = categoryRepository.findAllCategoriesWithCount();
        List<Category> categories = categoryRepository.findAll();

        // 포스트가 실제로 있는 카테고리 찾기
        CategoryWithCountDto categoryWithPosts = allCategoriesWithCount.stream()
                .filter(cat -> cat.getPostCount() > 0)
                .findFirst()
                .orElseThrow(() -> new AssertionError("포스트가 있는 카테고리가 없습니다"));

        long categoryPostCount = postRepository.findAll()
                .stream()
                .filter(post -> post.getCategory().getCategoryName()
                        .equals(categoryWithPosts.getCategoryName())
                                && post.getStatus() == PostStatus.PUBLISHED)
                .count();

        // then
        Assertions.assertTrue(!allCategoriesWithCount.isEmpty(),
                "최소 한개 존재 해야 할듯");
        Assertions.assertTrue(!categories.isEmpty(),
                "최소 한개 존재 해야 할듯");
        Assertions.assertEquals(categories.size(), allCategoriesWithCount.size());
        Assertions.assertEquals(categoryWithPosts.getPostCount(), categoryPostCount);
    }
}
