package taveSpring.parabom.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taveSpring.parabom.Controller.Dto.ReviewDto;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.ReviewService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public ResponseEntity<? extends BasicResponse> create(
            @Valid @RequestBody ReviewDto.ReviewCreateDto dto) {
        return ResponseEntity.ok()
                .body(new CommonResponse<ReviewDto.IdResponse>(reviewService.saveReview(dto)));
    }


}
