package HeoJin.demoBlog.post.service;


import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.member.entity.Member;
import HeoJin.demoBlog.post.dto.request.PostDeleteRequest;
import HeoJin.demoBlog.post.dto.request.PostModifyRequest;
import HeoJin.demoBlog.post.dto.request.PostRequest;
import HeoJin.demoBlog.post.dto.response.PostContractionResponse;
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
    public PostContractionResponse writePost(Member member, PostRequest postRequest) {

        // 카테고리 이미 존재 하는 지 안하는지 확인
        Category category = categoryRepository.findByCategoryName(postRequest.getCategoryName())
                .orElseThrow(() -> new CustomNotFound("카테고리"));

        Post newpost = Post.builder()
                .title(postRequest.getTitle())
                .member(member)
                .regDate(LocalDateTime.now(ZoneId.of("Asia/Seoul"))) // 현재 시간으로
                .content(postRequest.getContent())
                .status(postRequest.getPostStatus())
                .category(category)
                .build();

        postRepository.save(newpost);

        return PostMapper.toPostContractionResponse(newpost);
    }


    // 게시글 수정
    @Transactional()
    public PostContractionResponse updatePost( PostModifyRequest postModifyRequest) {
        // 변경감지로
        Post post = postRepository.findById(postModifyRequest.getPostId())
                .orElseThrow(() -> new CustomNotFound("해당 Post가 존재하지 않습니다."));

        post.updatePost(postModifyRequest.getTitle(),
                postModifyRequest.getContent(),
                postModifyRequest.getPostStatus());


        return PostMapper.toPostContractionResponse(post);
    }


    @Transactional
    public void deletePost(PostDeleteRequest postDeleteRequest) {
        Post post = postRepository.findById(postDeleteRequest.getPostId())
                .orElseThrow(() -> new CustomNotFound("해당 Post가 존재하지 않습니다."));

        postRepository.delete(post);

    }
}
