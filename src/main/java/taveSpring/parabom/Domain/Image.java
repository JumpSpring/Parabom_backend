package taveSpring.parabom.Domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue
    private Long id;

    /* Post 매핑*/
    @ManyToOne
    @JoinColumn(name = "id")
    private Post post;

    /* Member 매핑*/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member_id")
    private Member member;

    private String fileName;    // 실제 로컬에 저장된 이미지 파일의 이름
    private String oriFileName; // 업로드된 이미지 파일의 초기 이름
    private String path;        // 로컬에 저장된 이미지 파일을 불러올 경로

    // 이미지 정보 저장
    public void updateImage(String oriFileName, String fileName, String path) {
        this.oriFileName = oriFileName;
        this.fileName = fileName;
        this.path = path;
    }


}
