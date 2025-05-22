package HeoJin.demoBlog.repository;

import HeoJin.demoBlog.domain.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface PostRepositoryCustom {



    // 일단 간단하게
    Page<Post> findAllPosts(Pageable pageable);

    Page<Post> findByCategoryName(String categoryName, Pageable pageable);
}
