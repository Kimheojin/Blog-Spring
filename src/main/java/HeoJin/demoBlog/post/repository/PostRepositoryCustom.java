package HeoJin.demoBlog.post.repository;

import HeoJin.demoBlog.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface PostRepositoryCustom {



    // 일단 간단하게
    Page<Post> findPublishedPostsWithFetch(Pageable pageable);

    Page<Post> findPublishedCategoryWithFetch(String categoryName, Pageable pageable);

    Optional<Post> findByIdWithMemberAndCategory(Long postId);
}
