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
}
