package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository  extends JpaRepository<Post,Long> {

    /* Post 엔티티에 Image 저장*/
    List<Image> save(Image image);


    /* 특정 카테고리 게시글 리스트 조회 */
    List<Post> findAllByCategory(String postCategoryName); // member fetch join

//    /* 찜한 게시글 목록 조회 */
//    List<Post> findAllListOfLiked(Long memberId); // member fetch join
}
