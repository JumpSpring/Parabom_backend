package taveSpring.parabom.Controller.Dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;

import java.sql.Timestamp;
import java.util.Date;


public class PostDto {

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
    @Builder
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

        public Post toEntity() {
           return Post.builder().name(name).price(price).finOrIng(finOrIng)
                   .datePurchased(datePurchased).date(date).openOrNot(openOrNot)
                   .status(status).directOrDel(directOrDel).category(category).hashtag(hashtag)
                   .title(title).content(content).build();
        }

    }

}
