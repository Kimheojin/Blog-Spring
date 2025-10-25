package HeoJin.demoBlog.seo.repository;


import HeoJin.demoBlog.seo.dto.response.ListPostSearchResponseDto;
import HeoJin.demoBlog.seo.entity.PostMongo;

import java.util.List;

public interface PostMongoRepository {
    List<PostMongo> getAll();
    void insertAll(List<PostMongo> postMongoList);
    void updateALl(List<PostMongo> postMongoList);
    ListPostSearchResponseDto getUnifiedSearch(String term);

}
