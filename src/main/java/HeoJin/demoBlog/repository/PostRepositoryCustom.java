package HeoJin.demoBlog.repository;

import HeoJin.demoBlog.domain.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findByCategoryName(String categoryName);


    // 일단 간단하게
    Page<Post> findAllPosts(Pageable pageable);

    Page<Post> findByCategoryName(String categoryName, Pageable pageable);
}
