package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Controller.Dto.ReviewDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Review;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public final MemberRepository memberRepository;
    public final ReviewRepository reviewRepository;

    /*리뷰 등록*/
    @Transactional
    public ReviewDto.IdResponse saveReview(ReviewDto.ReviewCreateDto dto) {

        Review review = dto.toEntity();
        review.setSenderType(dto.getSenderType());

        Member recipient = memberRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("후기를 받는 회원이 존재하지 않습니다."));
        Member sender = memberRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("후기를 작성한 회원이 존재하지 않습니다."));
        review.setMembers(recipient, sender);

        reviewRepository.save(review);
        return ReviewDto.IdResponse.of(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("후기 정보가 없습니다."));

        Member recipient = review.getRecipient();
        recipient.deleteMyReview(review);

        reviewRepository.delete(review);
    }
}
