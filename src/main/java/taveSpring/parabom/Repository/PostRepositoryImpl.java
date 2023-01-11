package taveSpring.parabom.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import taveSpring.parabom.Controller.Dto.PostSearch;
import taveSpring.parabom.Domain.Post;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.*;
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
                        getPriceContains(postSearch.getPrice()),
                        getFinOrIngContains(postSearch.getFinOrIng()),
                        getDatePurchasedContains(postSearch.getDatePurchased()),
                        getOpenOrNotContains(postSearch.getOpenOrNot()),
                        getStatusContains(postSearch.getStatus()),
                        getDirectOrDelContains(postSearch.getDirectOrDel()),
                        getCategoryContains(postSearch.getCategory()),
                        getHashtagContains(postSearch.getHashtag()),
                        getTitleContains(postSearch.getTitle()),
                        getContentContains(postSearch.getContent()))
                .fetch();
    }

    // name이 존재하면 검색
    private BooleanExpression getNameContains(String name) {
        return hasText(name) ? post.name.contains(name) : null;
    }

    // price이상의 금액으로 post 조회
    private BooleanExpression getPriceContains(Integer price) {
        return price != null ? post.price.goe(price) : null;
    }

    // 거래중 or 거래완료 검색
    private BooleanExpression getFinOrIngContains(Integer finOrIng) {
        return finOrIng != null ? post.finOrIng.eq(finOrIng) : null;
    }

    // 입력된 구매날짜 기준으로 현재까지의 구매날짜를 가진 post 검색
    private BooleanExpression getDatePurchasedContains(Date startDate) {
        return startDate != null ? post.datePurchased.goe(startDate) : null;
    }

    private BooleanExpression getOpenOrNotContains(Integer openOrNot) {
        return openOrNot != null ? post.openOrNot.eq(openOrNot) : null;
    }

    private BooleanExpression getStatusContains(String status) {
        return hasText(status) ? post.status.contains(status) : null;
    }

    private BooleanExpression getDirectOrDelContains(String directOrDel) {
        return hasText(directOrDel) ? post.directOrDel.eq(directOrDel) : null;
    }

    private BooleanExpression getCategoryContains(String category) {
        return hasText(category) ? post.category.eq(category) : null;
    }

    private BooleanExpression getHashtagContains(String hashtag) {
        return hasText(hashtag) ? post.hashtag.contains(hashtag) : null;
    }

    private BooleanExpression getTitleContains(String title) {
        return hasText(title) ? post.title.contains(title) : null;
    }

    private BooleanExpression getContentContains(String content) {
        return hasText(content) ? post.content.contains(content) : null;
    }
}
