package HeoJin.demoBlog.post.service;


import HeoJin.demoBlog.category.entity.Category;
import HeoJin.demoBlog.category.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    // 상태 상관 X + 전체 post 조회

    @Transactional(readOnly = true)
    public PagePostResponse readAdminPagedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // entityGraph로 가져오기
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostMapper::toPostResponse)
                .collect(Collectors.toList());

        return PostMapper.toPagePostResponse(postResponses, postPage);


    }


    // 상태 상관 X + 카테고리 별 post 조회 조회
    @Transactional(readOnly = true)
    public PagePostResponse readAdminPagingCategoryPosts(String categoryName, int page, int size) {
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CustomNotFound("해당 카테고리 이름"));
        Pageable pageable = PageRequest.of(page, size); // 조건 객체
        Page<Post> postPage = postRepository.findCategoryWithFetch(category.getCategoryName(), pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostMapper::toPostResponse)
                .toList();

        return PostMapper.toPagePostResponse(postResponses, postPage);


    }


    // 상태 상관 X + 단일 포스트 조회
    @Transactional(readOnly = true)
    public PostResponse getAdminSinglePost(String postId) {
        Long id;
        try{
            id = Long.parseLong(postId);
        } catch (NumberFormatException e){
            throw new CustomNotFound("유효하지 않은 포스트 ID");
        }
        Post post = postRepository.findWithPostId(id)
                .orElseThrow(() -> new CustomNotFound("포스트"));


        return PostMapper.toPostResponse(post);
    }

    // 상태 별 게시글 조회
    @Transactional(readOnly = true)
    public PagePostResponse readAdminPagingStatusPosts(PostStatus postStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findPostWithStatusFetch(postStatus, pageable);

        List<PostResponse> postResponses = postPage.getContent()
                .stream()
                .map(PostMapper::toPostResponse)
                .collect(Collectors.toList());


        return PostMapper.toPagePostResponse(postResponses, postPage);
    }
}
