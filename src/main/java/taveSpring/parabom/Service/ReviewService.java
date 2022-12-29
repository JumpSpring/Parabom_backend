package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Review;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.ReviewRepository;

import static taveSpring.parabom.Controller.Dto.ReviewDto.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    public final MemberRepository memberRepository;
    public final ReviewRepository reviewRepository;

    /*후기 등록*/
    @Transactional
    public IdResponse saveReview(ReviewCreateDto dto) {
        Review review = dto.toEntity();
        review.setSenderType(dto.getSenderType());

        Member recipient = memberRepository.findById(dto.getRecipientId())
                .orElseThrow(() -> new IllegalArgumentException("후기를 받는 회원이 존재하지 않습니다."));
        Member sender = memberRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("후기를 작성한 회원이 존재하지 않습니다."));
        review.setMembers(recipient, sender);

        reviewRepository.save(review);
        return IdResponse.of(review);
    }

    /*후기 수정*/
    @Transactional
    public void updateReview(Long id, ReviewModifyDto dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("후기 정보가 없습니다."));

        review.update(dto.getItemName(), dto.getText(), dto.getStarPoint());
    }

    /*후기 삭제*/
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("후기 정보가 없습니다."));

        Member recipient = review.getRecipient();
        recipient.deleteMyReview(review);
        Member sender = review.getSender();
        sender.deleteSendReview(review);

        reviewRepository.delete(review);
    }
}
