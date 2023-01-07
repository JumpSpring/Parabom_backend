package taveSpring.parabom.Controller.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
public class PostSearch {
    private String name;
    private Integer price;
    private Integer finOrIng;   // 거래완료/거래중
    private Date datePurchased;
    private Integer openOrNot;
    private String status;      // 상품상태
    private String directOrDel;
    private String category;    // 카테고리
    private String hashtag;     // 해시태그
    private String title;
    private String content;
}
