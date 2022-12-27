package taveSpring.parabom.Service;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    public Post beforeEach() { // post 생성하여 저장
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));
        Post post = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member.get());
        return postRepository.save(post);
    }

    private Date getDate(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m-1, d);
        return new Date(cal.getTimeInMillis());
    }

    private Post getPost(String name, int price, Integer foi, Date datePurchased, Integer openOrNot,
                         String status, String directOrDel, String category, String hashtag,
                         String title, String content, Member member) {
        return Post.createPost(name, price, foi, datePurchased, openOrNot, status, directOrDel,
                category, hashtag, title, content, member);
    }

    @Test
    @DisplayName("게시글 상세조회 테스트")
    public void 게시글_상세조회() throws Exception {
        Post post = beforeEach();

        PostDto.PostDetailDto postDetailDto = postService.productDetail(post.getId());

        assertEquals(post.getName(), postDetailDto.getName());
        assertEquals(post.getPrice(), postDetailDto.getPrice());
        assertEquals(post.getTitle(), postDetailDto.getTitle());
        assertEquals(post.getContent(), postDetailDto.getContent());
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    public void 게시글_등록() throws Exception {
        PostDto.PostCreateDto postCreateDto = PostDto.PostCreateDto.builder()
                .name("ps4").price(300000).finOrIng(0).datePurchased(getDate(2016, 1, 1))
                .date(Timestamp.valueOf(LocalDateTime.now())).openOrNot(0).status("good")
                .directOrDel("direct").category("전자제품").hashtag("게임기").title("ps4")
                .content("ps4").build();

        Long postId = postService.postCreate(postCreateDto);

        PostDto.PostDetailDto postDetailDto = postService.productDetail(postId);
        assertEquals("ps4", postDetailDto.getName());
        assertEquals(300000, postDetailDto.getPrice());
        assertEquals("게임기", postDetailDto.getHashtag());
    }
}