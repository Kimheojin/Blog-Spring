package HeoJin.demoBlog.repository;

import HeoJin.demoBlog.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findByCategoryName(String categoryName);
}
