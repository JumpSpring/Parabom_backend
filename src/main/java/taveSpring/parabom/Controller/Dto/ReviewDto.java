package taveSpring.parabom.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import taveSpring.parabom.Domain.Review;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReviewDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewCreateDto {

        @NotNull
        private Long senderId;
        @NotBlank
        private String senderType; //"판매자" 또는 "구매자"만 저장 가능

        private String itemName;
        private String text;
        @Range(min = 1, max = 5)
        private Integer starPoint;

        public Review toEntity() {
            return Review.builder()
                    .itemName(itemName)
                    .text(text)
                    .starPoint(starPoint)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IdResponse {
        private Long id;

        public static IdResponse of(Review review) {
            return new IdResponse(review.getId());
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewModifyDto {
        private String itemName;
        private String text;
        @Range(min = 1, max = 5)
        private Integer starPoint;
    }

    /// 코드 추가 부분 ///

    // 전체 review 조회 클래스
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListDto {
        private Long id;
        private String senderType;
        private String itemName;
        private String text;
        private Integer starPoint;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ReviewListDto(Review review) {
            this.id = review.getId();
            this.senderType = String.valueOf(review.getSenderType());
            this.itemName = review.getItemName();
            this.text = review.getText();
            this.starPoint = review.getStarPoint();
            this.createdAt = review.getCreatedAt();
            this.updatedAt = review.getUpdatedAt();
        }


    }

    // test class
    // testcode 작성 위해 만든 클래스
    // 추후 필요하면 추가해주기
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewResponse {
        private Long id;
        private String itemName;
        private String text;
        private Integer starPoint;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ReviewResponse of(Review review) {
            return new ReviewResponse(review.getId(), review.getItemName(), review.getText(),
                    review.getStarPoint(), review.getCreatedAt(), review.getUpdatedAt());
        }
    }


    /// 코드 추가 부분 ///

}
