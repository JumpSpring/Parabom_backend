package taveSpring.parabom.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.modelmapper.ModelMapper;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PostDto {

    // DTO -> Entity 혹은 Entity -> DTO 변환
    private static ModelMapper modelMapper = new ModelMapper();

    /*게시물 상세조회*/
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostDetailDto {
        //private String member;
        //private byte[] images;

        private Long id;
        private String name;
        private int price;
        private Integer finOrIng;
        private Date datePurchased;
        private Integer openOrNot;
        private String status;
        private String directOrDel;
        private String hashtag;

        private String title;
        private String content;
        private Timestamp date;

        public PostDetailDto(Post entity) {
            //this.member = entity.getMember().getNickname();
            //this.member = entity.getMember().getProfile();
            //this.images = entity.getImages().getImage();

            this.id = entity.getId();
            this.name = entity.getName();
            this.price = entity.getPrice();
            this.finOrIng = entity.getFinOrIng();
            this.datePurchased = entity.getDatePurchased();
            this.openOrNot = entity.getOpenOrNot();
            this.status = entity.getStatus();
            this.openOrNot = entity.getOpenOrNot();
            this.directOrDel = entity.getDirectOrDel();
            this.hashtag = entity.getHashtag();
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.date = entity.getDate();
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostCreateDto {
        private String name;
        private int price;
        private Integer finOrIng;
        private Date datePurchased;
        private Integer openOrNot;
        private String status;
        private String directOrDel;
        private String category;
        private String hashtag;
        //private Image images;

        private String title;
        private String content;
        private Timestamp date;

        // 상품의 이미지 아이디를 저장하는 리스트
        // 상품 등록 전에는 이미지가 없으니까 비어있음
        // 수정할 때 이미지 아이디 저장해둘 용도
        private List<Long> imageId = new ArrayList<>();

        public Post createPost(){
            return modelMapper.map(this, Post.class);
            // this_entity 를 dto로 반환
        }

        public static PostCreateDto of(Post post){
            return modelMapper.map(post, PostCreateDto.class);
            // entity 를 파라미터로 받아서 dto 로 반환
        }

        public Post toEntity() {
           return Post.builder().name(name).price(price).finOrIng(finOrIng)
                   .datePurchased(datePurchased).date(date).openOrNot(openOrNot)
                   .status(status).directOrDel(directOrDel).category(category).hashtag(hashtag)
                   .title(title).content(content).date(date).build();
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
    }

    /*게시물 거래 상태 수정*/
    @Getter @Setter
    @AllArgsConstructor
    public static class ModifyFinOrIngRequest {
        private Integer finOrIng;
    }


    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostImageDto {
        private Long id;
        private String path;
        private String name;
        private byte[] caption;

        public static PostImageDto of(Image image) {
            return modelMapper.map(image,PostImageDto.class);
            // entity 를 파라미터로 받아서 dto 로 반환함
        }

    }

    /*게시물 찜 목록에 추가*/
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddHeartDto {
        private Long id;
        private String memberId;
    }

}
