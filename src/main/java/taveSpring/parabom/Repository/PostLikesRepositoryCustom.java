package taveSpring.parabom.Repository;

import taveSpring.parabom.Domain.PostLikes;

import java.util.Optional;

public interface PostLikesRepositoryCustom {

    public Optional<PostLikes> findOneByMemberAndPost(Long memberId, Long postId);
}
