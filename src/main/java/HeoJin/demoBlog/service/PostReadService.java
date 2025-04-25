package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.dto.request.CategoryRequest;
import HeoJin.demoBlog.dto.response.PostResponse;
import HeoJin.demoBlog.exception.CustomNotFound;
import HeoJin.demoBlog.repository.CategoryRepository;
import HeoJin.demoBlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostReadService {
    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> readEntirePost() {


        return postRepository.findAll()
                .stream().map(post -> PostResponse
                        .builder()
                        .regDate(post.getRegDate())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> CategoryPost(CategoryRequest categoryRequest){

        Category category = categoryRepository.findByCategoryName(categoryRequest.getCategoryName())
                .orElseThrow(() -> new CustomNotFound("카테고리"));

        return postRepository.findByCategoryName(category.getCategoryName())
                .stream().map(post -> PostResponse.builder()
                        .title(post.getTitle())
                        .regDate(post.getRegDate())
                        .content(post.getContent())
                        .build()
        ).collect(Collectors.toList());
    }
}
