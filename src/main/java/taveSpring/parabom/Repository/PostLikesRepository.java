package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Domain.PostLikes;

import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    /* 게시물과 회원으로 조회*/
    Optional<PostLikes>  findOneByMemberAndPost(Long memberId, Long id);

    /* 회원id로 조회*/
    PostLikes findByMemberId(Long memberId);
}
