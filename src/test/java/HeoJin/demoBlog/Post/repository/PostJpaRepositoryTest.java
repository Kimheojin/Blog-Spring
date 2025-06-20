package HeoJin.demoBlog.Post.repository;

import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.configuration.dataJpaTest.SaveDataJpaTest;
import HeoJin.demoBlog.global.config.QuerydslConfig;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@DataJpaTest
@Import(QuerydslConfig.class)
public class PostJpaRepositoryTest extends SaveDataJpaTest{


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp(){
        EntityManager em = entityManager.getEntityManager();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        initializeTestData(em, encoder);
    }

    @Test
    @DisplayName("findById -> 정상 작동 // postId 로 조회")
    void test1() {
        // given
        Member testMember = createTestMember();
        saveAllCategories();

        Category testCategory = categoryRepository.findAll().get(0);


        // when





        // then
    }

}
