package HeoJin.demoBlog.post.repository;

import HeoJin.demoBlog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {


    // fetch조인 X
    Optional<Post> findById(Long id);

    boolean existsByCategoryId(Long categoryId);


}
