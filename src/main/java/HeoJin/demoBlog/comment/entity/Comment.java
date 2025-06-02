package HeoJin.demoBlog.comment.entity;

import HeoJin.demoBlog.comment.dto.request.CommentModifyRequest;
import HeoJin.demoBlog.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @Email
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CommentStatus status = CommentStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 메소드
    public void delete(){
        this.status = CommentStatus.DELETED;
    }

    public void adminDelete() {
        this.status = CommentStatus.ADMIN_DELETED;
    }

    public boolean isDeleted() {
        return status == CommentStatus.DELETED || status == CommentStatus.ADMIN_DELETED;
    }
    public void updateComment(String content){
        this.content = content;
    }


}
