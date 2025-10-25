package HeoJin.demoBlog.seo.service;


import HeoJin.demoBlog.seo.dto.response.ListPostSearchResponseDto;
import HeoJin.demoBlog.seo.repository.PostMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeoService {
    private final PostMongoRepository postMongoRepository;


    public ListPostSearchResponseDto getUnifiedSearch(String term) {
        ListPostSearchResponseDto unifiedSearch = postMongoRepository.getUnifiedSearch(term);
        return unifiedSearch;
    }
}
