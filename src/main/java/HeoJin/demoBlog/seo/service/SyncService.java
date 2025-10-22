package HeoJin.demoBlog.seo.service;


import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.repository.PostRepository;
import HeoJin.demoBlog.seo.dto.response.TriggerResponseDto;
import HeoJin.demoBlog.seo.entity.PostMongo;
import HeoJin.demoBlog.seo.repository.PostMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final PostMongoRepository postMongoRepository;
    private final PostRepository postRepository;
    public TriggerResponseDto triggerSync() {

        try {
            List<Post> allPost = postRepository.findAll();
            if (allPost.isEmpty()) {
                throw new CustomNotFound("post 가 존재하지 않습니다.");
            }
            Map<Long, PostMongo> postMysqlMap = new HashMap<>();
            for (Post post : allPost) {
                PostMongo postMongo = PostMongo.builder()
                        .postId(post.getId())
                        .content(post.getContent())
                        .title(post.getTitle())
                        .syncedDate(LocalDateTime.now())
                        .build();
                postMysqlMap.put(postMongo.getPostId(), postMongo);

            }
            TriggerResponseDto triggerResponseDto = compareData(postMysqlMap);
            return triggerResponseDto;

        }catch (Exception e){
            return null;
        }


    }
    
    private TriggerResponseDto compareData(Map<Long, PostMongo> postMysqlMap){
        List<PostMongo> postMongoRepositoryAll = postMongoRepository.getAll();

        List<PostMongo> postsToUpdate = new ArrayList<>();
        List<PostMongo> postsToInsert = new ArrayList<>();


        for(PostMongo postFromMongo : postMongoRepositoryAll) {
            // null 반환 가능
            PostMongo existingPost = postMysqlMap.get(postFromMongo.getPostId());

            if(existingPost != null) {
                // 기존에 존재하는 경우
                postsToUpdate.add(postFromMongo.update(existingPost));
            } else {
                // 새로운 데이터인 경우
                postsToInsert.add(existingPost);
            }
        }

        postMongoRepository.insertAll(postsToInsert);
        postMongoRepository.updateALl(postsToUpdate);


        return  TriggerResponseDto.builder()
                .insertCount(postsToInsert.size())
                .updateCount(postsToUpdate.size())
                .build();

    }
}
