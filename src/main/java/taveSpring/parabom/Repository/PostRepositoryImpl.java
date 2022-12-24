package taveSpring.parabom.Repository;

import lombok.RequiredArgsConstructor;
import taveSpring.parabom.Domain.Post;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Post> findAllByCategory(String postCategoryName) {
        return em.createQuery("select p from Post p where p.category = :category", Post.class)
                .setParameter("category", postCategoryName)
                .getResultList();
    }

    @Override
    public List<Post> findAllListOfLiked(Long memberId) {
        return null;
    }
}
