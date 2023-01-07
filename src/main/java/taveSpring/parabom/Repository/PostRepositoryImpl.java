package taveSpring.parabom.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import taveSpring.parabom.Controller.Dto.PostSearch;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Domain.QMember;
import taveSpring.parabom.Domain.QPost;

import javax.persistence.EntityManager;
import java.util.List;

import static taveSpring.parabom.Domain.QMember.*;
import static taveSpring.parabom.Domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllByCategory(String postCategoryName) {
        return em.createQuery("select p from Post p where p.category = :category", Post.class)
                .setParameter("category", postCategoryName)
                .getResultList();
    }

    @Override
    public List<Post> findAllListOfLiked(Long memberId) {
        return em.createQuery("select p from PostLikes pl " +
                "join pl.post p on pl.member.id = :memberId", Post.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<Post> searchPosts(PostSearch postSearch) {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.member, member)
                .where(getNameContains(postSearch.getName()),
                        getPriceContains(postSearch.getPrice()))
                .fetch();
    }

    private BooleanExpression getNameContains(String name) { // name이 존재하는지 확인
        return StringUtils.hasText(name) ? post.name.contains(name) : null;
    }

    private BooleanExpression getPriceContains(Integer price) {
        return price != null ? post.price.goe(price) : null;
    }
}
