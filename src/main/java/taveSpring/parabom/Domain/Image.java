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

    @ManyToOne
    @JoinColumn(name = "id")
    private Post post; // postId -> post로 수정함

    private byte[] image;
    private String name;


}
