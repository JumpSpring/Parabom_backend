package taveSpring.parabom.Service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Domain.*;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostRepository;

import java.util.*;
import java.util.Optional;
=======
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Domain.PostLikes;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

>>>>>>> origin/master
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
<<<<<<< HEAD
public class PostServiceTest {
=======
class PostServiceTest {
>>>>>>> origin/master

    @Autowired PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
<<<<<<< HEAD


    public Post beforeEach() { // post 생성하여 저장
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));
        Post post = getPost("ps5", 500000, FinOrIng.fin, getDate(2018, 8, 15),
=======
    @Autowired
    PostLikesService postLikesService;

    public Post beforeEach() { // post 생성하여 저장
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));
        Post post = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
>>>>>>> origin/master
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member.get());
        return postRepository.save(post);
    }

    private Date getDate(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m-1, d);
        return new Date(cal.getTimeInMillis());
    }

<<<<<<< HEAD
    private Post getPost(String name, int price, FinOrIng foi, Date datePurchased, Integer openOrNot,
=======
    private Post getPost(String name, int price, Integer foi, Date datePurchased, Integer openOrNot,
>>>>>>> origin/master
                         String status, String directOrDel, String category, String hashtag,
                         String title, String content, Member member) {
        return Post.createPost(name, price, foi, datePurchased, openOrNot, status, directOrDel,
                category, hashtag, title, content, member);
    }
<<<<<<< HEAD
    @Test
    @DisplayName("구매 상태 변경하기")
    void changeFinOrIng() {
        Post post = beforeEach();
        post.modifyFinOrIng(FinOrIng.ing);
        assertEquals(post.getFinOrIng(), FinOrIng.ing);
    }

    @Test
    void 게시물수정() {
        Post post = beforeEach();
    }

    @Test
    void 게시물삭제() {
        Post post = beforeEach();
    }

}
=======

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

    @Test
    @DisplayName("카테고리 조회 테스트")
    public void 카테고리_조회() throws Exception {
        Post post = beforeEach();
        List<PostDto.PostDetailDto> postDetailDtos = postService.getAllPostInfoByCategory("전자제품");

        assertEquals(1, postDetailDtos.size());

        PostDto.PostDetailDto postDetailDto = postDetailDtos.get(0);
        assertEquals("ps5", postDetailDto.getName());
        assertEquals(500000, postDetailDto.getPrice());
        assertEquals("게임기", postDetailDto.getHashtag());
    }

    @Test
    @DisplayName("좋아요 테스트")
    public void 좋아요() throws Exception {
        Post post = beforeEach();
        PostDto.PostDetailDto postDetailDto = postService.productDetail(post.getId());

        postLikesService.clickPostLikes(Integer.toUnsignedLong(1), postDetailDto.getId());

        Optional<PostLikes> postlikes = postLikesService.findOne(Integer.toUnsignedLong(1), post.getId());
        assertNotNull(postlikes.get());

        Optional<Post> findPost = postRepository.findById(postDetailDto.getId());
        assertEquals(1, findPost.get().getLikes().size());
    }

    @Test
    @DisplayName("좋아요 취소 테스트")
    public void 좋아요_취소() throws Exception {
        Post post = beforeEach();
        PostDto.PostDetailDto postDetailDto = postService.productDetail(post.getId());

        postLikesService.clickPostLikes(Integer.toUnsignedLong(1), postDetailDto.getId());
        postLikesService.clickPostLikes(Integer.toUnsignedLong(1), postDetailDto.getId()); // 한번 더 클릭

        Optional<PostLikes> postlikes = postLikesService.findOne(Integer.toUnsignedLong(1), post.getId());
        assertFalse(postlikes.isPresent());

        Optional<Post> findPost = postRepository.findById(postDetailDto.getId());
        assertEquals(0, findPost.get().getLikes().size());
    }

    @Test
    @DisplayName("내 찜 목록 조회 테스트")
    public void 찜한_목록_조회() throws Exception {
        Post post = beforeEach();
        PostDto.PostDetailDto postDetailDto = postService.productDetail(post.getId());

        postLikesService.clickPostLikes(Integer.toUnsignedLong(1), postDetailDto.getId());

        List<PostDto.PostDetailDto> allPostInfoLiked = postService.getAllPostInfoLiked(Integer.toUnsignedLong(1));
        PostDto.PostDetailDto dto = allPostInfoLiked.get(0);

        assertEquals("ps5", dto.getName());
        assertEquals(500000, dto.getPrice());
        assertEquals("게임기", dto.getHashtag());
    }
}
>>>>>>> origin/master
