package taveSpring.parabom.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class PostDto {

    public static Post post;

    // DTO -> Entity 혹은 Entity -> DTO 변환
    private static ModelMapper modelMapper = new ModelMapper();

    /*게시물 상세조회*/
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostDetailDto {
        //private Long memberId;

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

        // 이미지 정보를 저장하는 리스트
        private List<ImageDto.ImageInfoDto> imageDtoList = new ArrayList<>();

        public static PostDetailDto of(Post post){
            return modelMapper.map(post, PostDetailDto.class);
            // entity 를 파라미터로 받아서 dto 로 반환함
        }

        public PostDetailDto(Post entity) {
            //listDtoToListEntity(imageDtoList); // List<image entity> -> List<imageInfoDto>
            //this.memberId = entity.getMember().getId();
            //this.getMember().getNickname();
            //this.getMember().getProfile();
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

            this.getImageDtoList();
        }

    }

    /*게시물 등록*/
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

        private String title;
        private String content;
        private Timestamp date;

        private Image image;
        private List<Image> images = new ArrayList<>();

        // 이미지 정보를 저장하는 리스트
        private List<ImageDto.ImageInfoDto> imageDtoList = new ArrayList<>();

        public Post toEntity() {
            images.add(image);
           return Post.builder().name(name).price(price).finOrIng(finOrIng)
                   .datePurchased(datePurchased).openOrNot(openOrNot)
                   .status(status).directOrDel(directOrDel).category(category).hashtag(hashtag)
                   .title(title).content(content).date(date)
                   .images(images)
                   .build();
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
