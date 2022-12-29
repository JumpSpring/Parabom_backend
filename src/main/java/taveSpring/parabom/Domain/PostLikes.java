package taveSpring.parabom.Domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_likes")
@Getter @Setter
public class PostLikes {

    @Id @GeneratedValue
    @Column(name = "post_likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 연관관계 메서드
    public void setPost(Post post) { // 좋아요 횟수 count
        this.post = post;
        post.getLikes().add(this);
    }

    // 생성 메서드
    public static PostLikes createPostLikes(Member member, Post post) {
        PostLikes postLikes = new PostLikes();
        postLikes.setMember(member);
        postLikes.setPost(post);
        return postLikes;
    }
}