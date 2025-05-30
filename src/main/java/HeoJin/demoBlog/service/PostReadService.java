package HeoJin.demoBlog.service;


import HeoJin.demoBlog.domain.Category;
import HeoJin.demoBlog.domain.Post;
import HeoJin.demoBlog.dto.response.PagePostResponse;
import HeoJin.demoBlog.dto.response.PostResponse;
import HeoJin.demoBlog.exception.CustomNotFound;
import HeoJin.demoBlog.repository.CategoryRepository;
import HeoJin.demoBlog.repository.PostRepository;
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
        Page<Post> postPage = postRepository.findAllPosts(pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        return new PagePostResponse(postResponses, postPage);

    }

    @Transactional(readOnly = true)
    public PagePostResponse readPagingCategoryPosts(String categoryName,
                                                    int page, int size){
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CustomNotFound("해당 카테고리 이름"));

        Pageable pageable = PageRequest.of(page, size); // 프라이머리로
        Page<Post> postPage = postRepository.findByCategoryName(category.getCategoryName(), pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());


        return new PagePostResponse(postResponses, postPage);

    }

    @Transactional(readOnly = true)
    public PostResponse getSinglePost(String postId) {
        Long id;
        try {
            id = Long.parseLong(postId);
        } catch (NumberFormatException e) {
            throw new CustomNotFound("유효하지 않은 포스트 ID");
        }

        Post post = postRepository.findPostWithMemberAndCategory(id)
                .orElseThrow(() -> new CustomNotFound("포스트"));

        return PostResponse.from(post);
    }


}
