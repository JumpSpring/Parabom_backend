package taveSpring.parabom.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
public class Member {

    @Id @Column(name = "member_id")
    @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private String address;

    //받은 리뷰 리스트
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Review> myReviews = new ArrayList<>();

    //보낸 리뷰 리스트
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Review> sendReviews = new ArrayList<>();

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
}
