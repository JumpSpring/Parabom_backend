package taveSpring.parabom.Domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import taveSpring.parabom.Domain.review.Review;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private Review review;

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
