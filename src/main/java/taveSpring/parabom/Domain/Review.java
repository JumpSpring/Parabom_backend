package taveSpring.parabom.Domain;

import lombok.Builder;
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

    @Builder
    public Review(String itemName, String text, Integer starPoint, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.itemName = itemName;
        this.text = text;
        this.starPoint = starPoint;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setSenderType(String senderType) {
        this.senderType = ReviewSenderType.find(senderType);
    }

    public void setMembers(Member recipient, Member sender) {
        this.recipient = recipient;
        this.sender = sender;

        recipient.addMyReviews(this);
        sender.addSendReviews(this);
    }

//    public void update(String itemName, String text, String starPoint) {
//        this.updatedAt = LocalDateTime.now();
//    }
}
