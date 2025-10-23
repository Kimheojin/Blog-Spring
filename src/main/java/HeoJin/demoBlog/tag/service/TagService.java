package HeoJin.demoBlog.tag.service;


import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.post.repository.PostRepository;
import HeoJin.demoBlog.tag.dto.request.TagUpdateRequestDto;
import HeoJin.demoBlog.tag.entity.PostTag;
import HeoJin.demoBlog.tag.entity.Tag;
import HeoJin.demoBlog.tag.repository.PostTagRepository;
import HeoJin.demoBlog.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;


    @Transactional
    public void updateTag(TagUpdateRequestDto tagUpdateRequestDto) {
        Long postId = tagUpdateRequestDto.getPostId();
        if (!postRepository.existsById(postId)) {
            throw new CustomNotFound("해당 post가 존재하지 않습니다.");
        }

        List<String> tagNameList = tagUpdateRequestDto.getTagNameList();

        tagNameList.forEach(tagName -> {
            updateTag(tagName, postId);
        });

    }

    private void updateTag(String tagName, Long postId){
        Optional<Tag> byTagName = tagRepository.findByTagName(tagName);
        if (byTagName.isPresent()) {
            PostTag postTag = PostTag.builder()
                    .tagId(byTagName.get().getId())
                    .postId(postId)
                    .build();

            postTagRepository.save(postTag);

        }else {
            Tag newTag = Tag.builder()
                    .tagName(tagName)
                    .build();
            tagRepository.save(newTag);
            PostTag postTag = PostTag.builder()
                    .tagId(newTag.getId())
                    .postId(postId)
                    .build();
            postTagRepository.save(postTag);

        }
    }
}