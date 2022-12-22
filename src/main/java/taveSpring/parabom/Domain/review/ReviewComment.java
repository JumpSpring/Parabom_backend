package taveSpring.parabom.Domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import taveSpring.parabom.Domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ReviewComment {

    @Id
    @Column(name = "review_comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member reviewer;
    @Enumerated(EnumType.STRING)
    private ReviewerType reviewerType;

    private String itemName;
    private String text;
    @Column(name = "star_point")
    private Integer starPoint;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public void update(String itemName, String text, Integer starPoint) {
        if (itemName != null) {
            this.itemName = itemName;
        }
        if (text != null) {
            this.text = text;
        }
        if (starPoint != null) {
            this.starPoint = starPoint;
        }

        this.updatedDate = LocalDateTime.now();
    }

}
