package taveSpring.parabom.Domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import taveSpring.parabom.Domain.Member;

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

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "comment_count")
    private Integer commentCount;
    @Column(name = "average_star_point")
    private Double averageStarPoint;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}
