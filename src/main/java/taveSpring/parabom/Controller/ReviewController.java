package taveSpring.parabom.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.ReviewService;

import javax.validation.Valid;

import java.util.List;

import static taveSpring.parabom.Controller.Dto.ReviewDto.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/member/{memberId}/review")
    public ResponseEntity<? extends BasicResponse> create(
            @Valid @RequestBody ReviewCreateDto dto,
            @PathVariable("memberId") Long recipientId) {
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(reviewService.saveReview(dto, recipientId)));
    }

    @PatchMapping("/member/{memberId}/review/{reviewId}")
    public ResponseEntity<? extends BasicResponse> update(
            @PathVariable Long reviewId,
            @RequestBody ReviewModifyDto dto) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/member/{memberId}/review/{reviewId}")
    public ResponseEntity<? extends BasicResponse> delete(
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    /// 코드 추가 부분 ///

    // 전체 후기 목록 조회
    @GetMapping("/member/{memberId}/getreview")
    public ResponseEntity<? extends BasicResponse> getReviewList(
            @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok().body(new CommonResponse<List>(reviewService.getReviewList(memberId)));
    }
    // 후기 상세 조회 (판매자 후기만 조회)
    @GetMapping("/member/{memberId}/getsellerreview")
    public ResponseEntity<? extends BasicResponse> getSellerReviewList(
            @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok().body(new CommonResponse<List>(reviewService.getReviewListBySeller(memberId)));
    }

    // 후기 상세 조회 (구매자 후기만 조회)
    @GetMapping("/member/{memberId}/getbuyerreview")
    public ResponseEntity<? extends BasicResponse> getBuyerReviewList(
            @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok().body(new CommonResponse<List>(reviewService.getReviewListByBuyer(memberId)));
    }

    // 별점 평균 조회
    @GetMapping("/member/{memberId}/getstarpoint")
    public ResponseEntity<? extends BasicResponse> getStarPoint(
            @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok().body(new CommonResponse<Double>(reviewService.getStarPoint(memberId)));
    }


    /// 코드 추가 부분 ///
}
