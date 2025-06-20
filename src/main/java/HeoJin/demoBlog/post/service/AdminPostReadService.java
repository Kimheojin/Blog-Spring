package HeoJin.demoBlog.post.service;


import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.post.dto.response.PagePostResponse;
import HeoJin.demoBlog.post.dto.response.PostResponse;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.entity.PostStatus;
import HeoJin.demoBlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPostReadService {
    private final PostRepository postRepository;

    // 상태 상관 X + 단일 포스트 조회
    @Transactional(readOnly = true)
    public PostResponse getAdminSinglePost(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomNotFound("포스트"));


        return PostMapper.toPostResponse(post);
    }


    @Transactional(readOnly = true)
    public PagePostResponse readAdminPosts(String categoryName, PostStatus postStatus, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findPostsWithFilters(categoryName, postStatus, pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostMapper::toPostResponse)
                .collect(Collectors.toList());

        return PostMapper.toPagePostResponse(postResponses, postPage);
    }
}
