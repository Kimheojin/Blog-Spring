package HeoJin.demoBlog.tag.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponseDto {

    private String tagName;
    private long postCount;
}
