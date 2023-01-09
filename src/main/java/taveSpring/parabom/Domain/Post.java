package taveSpring.parabom.Domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue
    private Long id;
    private String name;
    private int price;
    private Integer finOrIng;   // 거래완료/거래중
    private Date datePurchased;
    private Integer openOrNot;
    private String status;      // 상품상태
    private String directOrDel;
    private String category;    // 카테고리
    private String hashtag;     // 해시태그

    private String title;
    private String content;
    @CreationTimestamp
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //UUID = post/images/{post-id}
    private String image;

    @OneToMany(mappedBy = "post")
    private Set<PostLikes> likes = new HashSet<PostLikes>();

    // 생성 메서드
    public static Post createPost(String name, int price, Integer foi, Date datePurchased, Integer openOrNot,
                                  String status, String directOrDel, String category, String hashtag,
                                  String title, String content, Member member ,String image) {
        return Post.builder()
                .name(name)
                .price(price)
                .finOrIng(foi)
                .datePurchased(datePurchased)
                .openOrNot(openOrNot)
                .status(status)
                .directOrDel(directOrDel)
                .category(category)
                .hashtag(hashtag)
                .title(title)
                .content(content)
                .member(member)
                .image(image)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .likes(new HashSet<>())
                .build();
    }
}
