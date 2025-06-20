package HeoJin.demoBlog.category.repository;


import HeoJin.demoBlog.configuration.dataJpaTest.SaveDataJpaTest;
import HeoJin.demoBlog.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataJpaTest
@Import(QuerydslConfig.class)
public class CategoryQuerydslRepositoryTest extends SaveDataJpaTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp(){
        EntityManager em = entityManager.getEntityManager();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        initializeTestData(em, encoder);
    }

//    @Test
//    @DisplayName("CategoryJpaRepositoryTest  -> 정상 작동 테스트")
//    void test1(){
//        // given
//        Member testMember = createTestMember();
//        saveAllCategories();
//        saveAllPosts(testMember);
//
//        // when
//        // then
//
//    }
}
