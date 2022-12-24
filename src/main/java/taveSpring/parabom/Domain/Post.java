package taveSpring.parabom.Domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.ImageDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT 추가 new
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<Image>();

    // 연관관계 메서드
    public void addImages(Image image) {
        this.images.add(image);
        image.setPost(this);
    }



}
