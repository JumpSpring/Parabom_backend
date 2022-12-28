package taveSpring.parabom.Domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @Column(name = "id")
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

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<Image>();

    // 연관관계 메서드
    public void addImages(Image image) {
        this.images.add(image);
        image.setPost(this);
    }

    // 게시물 수정 기능
    public void update(int price, Integer openOrNot, String status, String directOrDel, String category, String hashtag) {
        this.price = price;
        this.openOrNot = openOrNot;
        this.status = status;
        this.directOrDel = directOrDel;
        this.category = category;
        this.hashtag = hashtag;
    }

    // 게시물 거래 상태 수정 기능
    public void updateFinOrIng(Integer finOrIng) {
        // finOrIng값 업데이트
        this.finOrIng = finOrIng;
    }
}
