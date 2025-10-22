package HeoJin.demoBlog.seo.service;


import HeoJin.demoBlog.post.repository.PostRepository;
import HeoJin.demoBlog.seo.repository.PostMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final PostMongoRepository postMongoRepository;
    private final PostRepository postRepository;
    public triggerResponseDto triggerSync() {

        // post 에서 전체 목록 가져오고
        // mongo 에서도 all 로 가져오고
        // 비교하면 될듯
        return null;
    }

    public
}
