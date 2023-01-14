package taveSpring.parabom.Controller.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
public class PostSearch {
    private String name; // 상품 이름
    private Integer price; // 상품 최소 가격
    private Integer finOrIng; // 거래완료/거래중
    private Date datePurchased; // 구매 시기
    private Integer openOrNot; // 미개봉/개봉
    private String status; // 상품상태
    private String directOrDel; // 직거래/택배
    private String category; // 카테고리
    private String hashtag; // 해시태그
    private String title; // 게시글 제목
    private String content; // 게시글 내용
}
