package HeoJin.demoBlog.seo.repository;


import HeoJin.demoBlog.post.repository.PostRepository;
import HeoJin.demoBlog.seo.entity.PostMongo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class PostMongoRepositoryImpl implements PostMongoRepository{

    private final MongoTemplate mongoTemplate;


    private final PostRepository postRepository;

    @Value("${mongo.collectionName}")
    private String collectionName;


    @Override
    public List<PostMongo> getAll() {

        List<PostMongo> all = mongoTemplate.findAll(PostMongo.class, collectionName);

        return all;
    }

    @Override
    public void insertAll(List<PostMongo> postMongoList) {
        // insert 동작
        mongoTemplate.insert(postMongoList, collectionName);

    }

    @Override
    public void updateALl(List<PostMongo> postMongoList) {
        postMongoList.forEach(
                postMongo -> mongoTemplate.save(postMongoList)
        );

    }



}
