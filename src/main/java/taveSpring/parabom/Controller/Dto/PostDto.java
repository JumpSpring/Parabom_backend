package taveSpring.parabom.Controller.Dto;

import lombok.*;
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
        private Member member;

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

        // 이미지 정보를 저장하는 리스트
        private List<ImageDto.ImageInfoDto> imageDtoList = new ArrayList<>();

        public static PostDetailDto of(Post post){
            return modelMapper.map(post, PostDetailDto.class);
            // entity 를 파라미터로 받아서 dto 로 반환함
        }

        public PostDetailDto(Post post) {
            this.member = post.getMember();
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

            ImageDto.ImageInfoDto imageInfoDto = new ImageDto.ImageInfoDto();
            for(int i=0; i<imageDtoList.size(); i++) {
                imageInfoDto = imageDtoList.get(i);
            }

        }

    }

    /*게시물 등록*/
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostCreateDto {
        private Member member;

        private Long id;
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

        private Image image;
        private List<Image> images = new ArrayList<>();

        public Post toEntity() {
            images.add(image);
            return Post.builder().id(id).name(name).price(price).finOrIng(finOrIng)
                    .datePurchased(datePurchased).openOrNot(openOrNot)
                    .status(status).directOrDel(directOrDel).category(category).hashtag(hashtag)
                    .title(title).content(content).date(date)
                    .images(images)
                    .member(member)
                    .build();
        }
    }
}
