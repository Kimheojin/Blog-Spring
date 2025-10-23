package HeoJin.demoBlog.tag.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TagUpdateRequestDto {
    private Long postId;
    private List<String> tagNameList;
}
