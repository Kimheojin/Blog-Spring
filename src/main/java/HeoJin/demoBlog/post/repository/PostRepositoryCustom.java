package HeoJin.demoBlog.post.repository;

import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface PostRepositoryCustom {


    Page<Post> findPublishedPostsWithFetch(Pageable pageable);

    Page<Post> findPublishedCategoryWithFetch(String categoryName, Pageable pageable);

    Optional<Post> findPublishedWithPostId(Long postId);
    Optional<Post> findWithPostId(Long postId);

    Page<Post> findPostsWithFilters(String categoryName, PostStatus postStatus, Pageable pageable);

}
