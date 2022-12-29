package taveSpring.parabom.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Controller.Dto.ReviewDto;
import taveSpring.parabom.Controller.Dto.ReviewDto.IdResponse;
import taveSpring.parabom.Controller.Dto.ReviewDto.ReviewCreateDto;
import taveSpring.parabom.Controller.Dto.ReviewDto.ReviewModifyDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Review;
import taveSpring.parabom.Domain.ReviewSenderType;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.ReviewRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;

    Member member1 = getMember("test1@gmail.com", "test1Password",
            "test1Nickname", "test1Profile", "test1Address");
    Member member2 = getMember("test2@gmail.com", "test2Password",
            "test2Nickname", "test2Profile", "test2Address");

    @Test
    public void 후기_등록() {
        memberRepository.save(member1);
        memberRepository.save(member2);
        ReviewCreateDto dto = createDto(member1.getId(), member2.getId(), "판매자", "iphone13", "감사합니다!", 5);

        IdResponse idResponse = reviewService.saveReview(dto);
        Review findReview = reviewRepository.findById(idResponse.getId()).orElseThrow();

        assertEquals(member1, findReview.getSender());
        assertEquals(member2, findReview.getRecipient());
        assertEquals(ReviewSenderType.SELLER, findReview.getSenderType());
        assertEquals("iphone13", findReview.getItemName());
        assertEquals("감사합니다!", findReview.getText());
        assertEquals(5, findReview.getStarPoint());
    }

    @Test
    public void 후기_수정() {
        memberRepository.save(member1);
        memberRepository.save(member2);
        ReviewCreateDto createDto = createDto(member1.getId(), member2.getId(), "판매자", "iphone13", "감사합니다!", 5);
        IdResponse idResponse = reviewService.saveReview(createDto);
        ReviewModifyDto modifyDto = new ReviewModifyDto("iphone13 mini", "감사합니다!!!", 4);

        reviewService.updateReview(idResponse.getId(), modifyDto);
        Review findReview = reviewRepository.findById(idResponse.getId()).orElseThrow();

        assertEquals("iphone13 mini", findReview.getItemName());
        assertEquals("감사합니다!!!", findReview.getText());
        assertEquals(4, findReview.getStarPoint());
        assertEquals(4, member2.getAvgStarPoint());
    }

    @Test
    public void 후기_삭제() {
        memberRepository.save(member1);
        memberRepository.save(member2);
        ReviewCreateDto dto = createDto(member1.getId(), member2.getId(), "판매자", "iphone13", "감사합니다!", 5);
        IdResponse idResponse = reviewService.saveReview(dto);
        int beforeDeleteSendReviewSize = member1.getSendReviews().size();
        int beforeDeleteMyReviewSize = member2.getMyReviews().size();

        reviewService.deleteReview(idResponse.getId());
        int afterDeleteSendReviewSize = member1.getSendReviews().size();
        int afterDeleteMyReviewSize = member2.getMyReviews().size();

        assertEquals(1, beforeDeleteSendReviewSize - afterDeleteSendReviewSize);
        assertEquals(1, beforeDeleteMyReviewSize - afterDeleteMyReviewSize);
        assertEquals(0, member2.getAvgStarPoint());
    }

    public ReviewCreateDto createDto(Long senderId, Long recipientId, String senderType, String itemName, String text, Integer starPoint) {
        return new ReviewCreateDto(senderId, recipientId, senderType, itemName, text, starPoint);
    }

    private Member getMember(String email, String password, String nickname, String profile, String address) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .address(address)
                .profile(profile)
                .build();
    }
}