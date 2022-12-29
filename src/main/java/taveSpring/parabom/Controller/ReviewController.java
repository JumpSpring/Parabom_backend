package taveSpring.parabom.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.ReviewService;

import javax.validation.Valid;

import static taveSpring.parabom.Controller.Dto.ReviewDto.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<? extends BasicResponse> create(
            @Valid @RequestBody ReviewCreateDto dto) {
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(reviewService.saveReview(dto)));
    }

    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<? extends BasicResponse> update(
            @PathVariable Long reviewId,
            @RequestBody ReviewModifyDto dto) {
        reviewService.updateReview(reviewId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<? extends BasicResponse> delete(
            @PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }
}
