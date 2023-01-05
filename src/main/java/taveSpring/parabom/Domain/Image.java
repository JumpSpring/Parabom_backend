package taveSpring.parabom.Domain;

import lombok.*;
import taveSpring.parabom.Controller.Dto.ImageDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Post 매핑*/
    @ManyToOne(cascade = CascadeType.ALL) // 영속성 전이 추가
    @JoinColumn(name = "post_id")
    private Post post;

    /* Member 매핑*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member_id")
    private Member member;

    private String fileName;    // 실제 로컬에 저장된 이미지 파일의 이름 (초기이름이 겹칠 수 있기 때문에 새로 설정)
    private String oriFileName; // 업로드된 이미지 파일의 초기 이름
    private String path;        // 로컬에 저장된 이미지 파일을 불러올 경로

    // 이미지 정보 저장
    public void updateImage(String oriFileName, String fileName, String path) {
        this.oriFileName = oriFileName;
        this.fileName = fileName;
        this.path = path;
    }

    public static Image createImage(Post post, Member member, String fileName, String oriFileName, String path) {
        return Image.builder().post(post).member(member)
                .fileName(fileName).oriFileName(oriFileName).path(path).build();
    }


}
