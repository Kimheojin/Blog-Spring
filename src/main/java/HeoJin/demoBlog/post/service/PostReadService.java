package HeoJin.demoBlog.post.service;


import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.category.repository.CategoryRepository;
import HeoJin.demoBlog.post.dto.response.PagePostResponse;
import HeoJin.demoBlog.post.entity.Post;
import HeoJin.demoBlog.post.repository.PostRepository;
import HeoJin.demoBlog.post.dto.response.PostResponse;
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
public class PostReadService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public PagePostResponse readPagedPosts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findPublishedPostsWithFetch(pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostMapper::toPostResponse)
                .collect(Collectors.toList());

        return PostMapper.toPagePostResponse(postResponses, postPage);

    }

    @Transactional(readOnly = true)
    public PagePostResponse readPagingCategoryPosts(String categoryName,
                                                    int page, int size){
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CustomNotFound("해당 카테고리 이름"));

        Pageable pageable = PageRequest.of(page, size); // 프라이머리로
        Page<Post> postPage = postRepository.findPublishedCategoryWithFetch(category.getCategoryName(), pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostMapper::toPostResponse)
                .collect(Collectors.toList());


        return PostMapper.toPagePostResponse(postResponses, postPage);
    }

    @Transactional(readOnly = true)
    public PostResponse getSinglePost(String postId) {
        Long id;
        try {
            id = Long.parseLong(postId);
        } catch (NumberFormatException e) {
            throw new CustomNotFound("유효하지 않은 포스트 ID");
        }

        Post post = postRepository.findPublishedWithPostId(id)
                .orElseThrow(() -> new CustomNotFound("포스트"));

        return PostMapper.toPostResponse(post);
    }


}
