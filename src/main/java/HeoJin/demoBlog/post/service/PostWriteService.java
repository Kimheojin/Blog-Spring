package HeoJin.demoBlog.post.service;


import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.post.dto.request.PostRequest;
import HeoJin.demoBlog.post.dto.response.PostcontractionResponse;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PostWriteService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;


    @Transactional()
    public PostcontractionResponse writePost(Member member, PostRequest PostRequestDto) {

        // 카테고리 이미 존재 하는 지 안하는지 확인

        Category category = categoryRepository.findByCategoryName(PostRequestDto.getCategoryName())
                .orElseThrow(() -> new CustomNotFound("카테고리"));

        Post newpost = Post.builder()
                .title(PostRequestDto.getTitle())
                .member(member)
                .regDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) // 현재 시간으로
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
