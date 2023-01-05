package taveSpring.parabom.Repository;

import lombok.RequiredArgsConstructor;
import taveSpring.parabom.Domain.PostLikes;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class PostLikesRepositoryImpl {

    private final EntityManager em;

    public Optional<PostLikes> findOneByMemberAndPost(Long memberId, Long postId) {
        return em.createQuery("select pl from PostLikes pl " +
                        "where pl.member.id = :memberId and pl.post.id = :postId", PostLikes.class)
               .setParameter("memberId", memberId)
                .setParameter("postId", postId)
                .getResultList()
                .stream().findAny();
    }
}
