package taveSpring.parabom.Domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_count")
    private Integer commentCount = 0;       // 받은 후기 개수
    @Column(name = "total_star_point")
    private Double totalStarPoint = 0.0;    // 받은 총 별점

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewComment> reviewComments = new ArrayList<>();

}
