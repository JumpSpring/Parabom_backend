package taveSpring.parabom.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter
@NoArgsConstructor
public class Member {

    @Id @Column(name = "member_id")
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String address;

    //UUID  = user/profile/{id}
    private String profile; //profile url

    //게시물 리스트
    @OneToMany(mappedBy="member")
    private List<Post> posts = new ArrayList<>();

    //구매 내역 리스트
    @OneToMany(mappedBy = "buyer")
    private List<Post> buyList = new ArrayList<Post>();


    //받은 리뷰 리스트
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private final List<Review> myReviews = new ArrayList<>();

    //보낸 리뷰 리스트
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private final List<Review> sendReviews = new ArrayList<>();

    //받은 리뷰들의 평균 별점
    @Column(name = "avg_star_point")
    private Double avgStarPoint = 0.0;

    @Builder
    public Member( String email, String password, String nickname, String profile, String address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.address = address;
    }

    public void update(String nickname, String profile, String address){
        if(nickname != null)
            this.nickname = nickname;
        if(profile != null)
            this.profile = profile;
        if(address != null)
            this.address = address;
    }

    /*후기 관련 비즈니스 메서드*/
    public void addMyReviews(Review review) {
        double totalStarPoint = avgStarPoint * myReviews.size();
        totalStarPoint += review.getStarPoint();

        myReviews.add(review);
        avgStarPoint = totalStarPoint / myReviews.size();
    }

    public void addSendReviews(Review review) {
        sendReviews.add(review);
    }

    public void updateStarPoint(Integer oldStarPoint, Integer newStarPoint) {
        double totalStarPoint = avgStarPoint * myReviews.size();
        totalStarPoint += (newStarPoint - oldStarPoint);
        avgStarPoint = totalStarPoint / myReviews.size();
    }

    public void deleteMyReview(Review review) {
        double totalStarPoint = avgStarPoint * myReviews.size();
        totalStarPoint -= review.getStarPoint();
        myReviews.remove(review);

        if (myReviews.size() > 0) {
            avgStarPoint = totalStarPoint / myReviews.size();
        }
        else {
            avgStarPoint = 0.0;
        }
    }

    public void deleteSendReview(Review review) {
        sendReviews.remove(review);
    }

    public void addBuyList(Post post) {
        buyList.add(post);
    }
}
