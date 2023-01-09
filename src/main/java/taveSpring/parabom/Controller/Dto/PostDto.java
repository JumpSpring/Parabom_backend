package taveSpring.parabom.Controller.Dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;

import java.sql.Timestamp;
import java.util.Date;


public class PostDto {

    // DTO -> Entity 혹은 Entity -> DTO 변환
    private static ModelMapper modelMapper = new ModelMapper();

    /*게시물 상세조회*/
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostDetailDto {
        private Long memberId;
        private Long id;
        private String name;
        private int price;
        private Integer finOrIng;
        private Date datePurchased;
        private Integer openOrNot;
        private String status;
        private String directOrDel;
        private String hashtag;
        private String category;
        private String title;
        private String content;
        private Timestamp date;
        private String image;

        public static PostDetailDto of(Post post){
            return modelMapper.map(post, PostDetailDto.class);
            // entity 를 파라미터로 받아서 dto 로 반환함
        }

        public PostDetailDto(Post post) {
            this.memberId = post.getMember().getId();
            this.id = post.getId();
            this.name = post.getName();
            this.price = post.getPrice();
            this.finOrIng = post.getFinOrIng();
            this.datePurchased = post.getDatePurchased();
            this.openOrNot = post.getOpenOrNot();
            this.status = post.getStatus();
            this.openOrNot = post.getOpenOrNot();
            this.directOrDel = post.getDirectOrDel();
            this.hashtag = post.getHashtag();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.date = post.getDate();
            this.category = post.getCategory();
            this.image = post.getImage();
        }

    }

    /*게시물 등록*/
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostCreateDto {
        private Long memberId;
        private String name;
        private int price;
        private Integer finOrIng;
        private Date datePurchased;
        private Integer openOrNot;
        private String status;
        private String directOrDel;
        private String category;
        private String hashtag;
        private String title;
        private String content;
        private Timestamp date;

        public Post toEntity() {
            return Post.builder().name(name).price(price).finOrIng(finOrIng)
                    .datePurchased(datePurchased).openOrNot(openOrNot)
                    .status(status).directOrDel(directOrDel).category(category).hashtag(hashtag)
                    .title(title).content(content).date(date)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static PostDto.IdResponse of(Post post){
            return new PostDto.IdResponse(post.getId());
        }
    }


    /*게시물 수정*/
    @Getter @Setter
    @AllArgsConstructor
    public static class ModifyRequest{
        private int price;
        private Integer openOrNot;
        private String status;
        private String directOrDel;
        private String category;
        private String hashtag;


        public ModifyRequest( int price,Integer openOrNot, String status, String directOrDel,String hashtag) {
            this.price = price;
            this.openOrNot = openOrNot;
            this.status = status;
            this.directOrDel = directOrDel;
            this.hashtag = hashtag;
        }
    }

    /*게시물 거래 상태 수정*/
    @Getter @Setter
    @AllArgsConstructor
    public static class ModifyFinOrIngRequest {
        private Integer finOrIng;

        public ModifyFinOrIngRequest(Post post) {
            this.finOrIng = post.getFinOrIng();
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class DealCompleteRequest {
        private Member buyer;
    }
}
