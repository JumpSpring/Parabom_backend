package taveSpring.parabom.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.PostLikes;

public interface PostLikesRepository extends JpaRepository<PostLikes, Long>, PostLikesRepositoryCustom {
}
