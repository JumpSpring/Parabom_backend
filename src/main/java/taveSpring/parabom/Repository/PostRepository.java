package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Domain.Post;

import java.util.List;

public interface PostRepository  extends JpaRepository<Post,Long>, PostRepositoryCustom {

    /* 특정 카테고리 게시글 리스트 조회 */
    //List<Post> findAllByCategory(String postCategoryName); // member fetch join

    /* 찜한 게시글 목록 조회 */
    //List<Post> findAllListOfLiked(Long memberId); // member fetch join

    List<Post> findAllByMemberId(Long memberId);
}
