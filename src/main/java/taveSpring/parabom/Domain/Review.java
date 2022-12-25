package taveSpring.parabom.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //리뷰를 받은 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private Member recipient;

    //리뷰를 보낸 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type")
    private ReviewSenderType senderType;

    private String itemName;
    private String text;
    @Column(name = "star_point")
    private Integer starPoint;

    @Column(name = "created_at")
    private LocalDateTime createdAt;    //생성일자
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;    //마지막 수정일자

}
