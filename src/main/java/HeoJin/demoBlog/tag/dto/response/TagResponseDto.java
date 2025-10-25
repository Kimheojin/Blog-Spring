package HeoJin.demoBlog.tag.dto.response;

import HeoJin.demoBlog.tag.entity.Tag;

public record TagResponseDto(Long id, String tagName) {
    public static TagResponseDto fromEntity(Tag tag) {
        return new TagResponseDto(tag.getId(), tag.getTagName());
    }
}