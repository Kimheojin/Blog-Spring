package HeoJin.demoBlog.post.repository;

import HeoJin.demoBlog.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {


    // fetch조인 X
    Optional<Post> findById(Long id);

    boolean existsByCategoryId(Long categoryId);

    // no usages 가 뜨는 게 정상(ide 단계에서)
    @EntityGraph(attributePaths = {"category", "member"})
    Page<Post> findAll(Pageable pageable);


}
