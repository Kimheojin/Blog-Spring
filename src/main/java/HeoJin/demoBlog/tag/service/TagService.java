package HeoJin.demoBlog.tag.service;


import HeoJin.demoBlog.global.exception.CustomNotFound;
import HeoJin.demoBlog.post.repository.PostRepository;
import HeoJin.demoBlog.tag.dto.request.DeleteTagDtoRequest;
import HeoJin.demoBlog.tag.dto.request.ListAddTagRequestDto;
import HeoJin.demoBlog.tag.dto.request.ListDeleteTagRequest;
import HeoJin.demoBlog.tag.dto.response.ListTagResponseDto;
import HeoJin.demoBlog.tag.dto.response.TagResponseDto;
import HeoJin.demoBlog.tag.entity.PostTag;
import HeoJin.demoBlog.tag.entity.Tag;
import HeoJin.demoBlog.tag.repository.PostTagRepository;
import HeoJin.demoBlog.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;


    @Transactional
    public void addTag(ListAddTagRequestDto listAddTagRequestDto) {
        Long postId = listAddTagRequestDto.postId();
        if (!postRepository.existsById(postId)) {
            throw new CustomNotFound("해당 post가 존재하지 않습니다.");
        }

        List<DeleteTagDtoRequest> deleteTagDtoRequests = listAddTagRequestDto.DtoList();

        listAddTagRequestDto.DtoList().forEach(
                deleteTagDtoRequest -> addTag(deleteTagDtoRequest.getTagName(), postId)
        );

    }

    private void addTag(String tagName, Long postId){
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

    // 태그 삭제 메소드
    @Transactional
    public void deleteTag(ListDeleteTagRequest listDeleteTagRequest) {
        long postId = listDeleteTagRequest.postId();

    }

    @Transactional(readOnly = true)
    public ListTagResponseDto getTagList() {
        List<Tag> allTags = tagRepository.findAll();

        List<TagResponseDto> tagDtos = allTags.stream()
                .map(TagResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new ListTagResponseDto(tagDtos);
    }
}