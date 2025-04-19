package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.domain.Member;
import HeoJin.demoBlog.domain.Post;
import HeoJin.demoBlog.dto.request.PostRequest;
import HeoJin.demoBlog.dto.response.PostcontractionResponse;
import HeoJin.demoBlog.exception.CategoryNotFound;
import HeoJin.demoBlog.repository.CategoryRepository;
import HeoJin.demoBlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;


    @Transactional()
    public PostcontractionResponse writePost(Member member, PostRequest PostRequestDto) {

        // 카테고리 이미 존재 하는 지 안하는지 확인

        Category category = categoryRepository.findByCategoryName(PostRequestDto.getCategoryName())
                .orElseThrow(() -> new CategoryNotFound());

        Post newpost = Post.builder()
                .title(PostRequestDto.getTitle())
                .member(member)
                .regDate(LocalDateTime.now()) // 현재 시간으로
                .content(PostRequestDto.getContent())
                .category(category)
                .build();
        postRepository.save(newpost);

        return PostcontractionResponse.builder()
                .title(newpost.getTitle())
                .regDate(newpost.getRegDate())
                .build();
    }


}
