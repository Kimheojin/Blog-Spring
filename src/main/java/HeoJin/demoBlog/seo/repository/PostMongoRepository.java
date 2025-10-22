package HeoJin.demoBlog.seo.repository;


import HeoJin.demoBlog.seo.entity.PostMongo;

import java.util.List;

public interface PostMongoRepository {
    List<PostMongo> getAll();
    void insertAll(List<PostMongo> postMongoList);


}
