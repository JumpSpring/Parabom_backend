package taveSpring.parabom.Service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
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

import static org.junit.jupiter.api.Assertions.*;
import static taveSpring.parabom.Controller.Dto.PostDto.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostLikesService postLikesService;

    public Post beforeEach() { // post 생성하여 저장
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));
        Post post = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member.get(),"imageURL");
        return postRepository.save(post);
    }

    private Date getDate(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m-1, d);
        return new Date(cal.getTimeInMillis());
    }

    private Post getPost(String name, int price, Integer foi, Date datePurchased, Integer openOrNot,
                         String status, String directOrDel, String category, String hashtag,
                         String title, String content, Member member,String imageURL) {
        return Post.createPost(name, price, foi, datePurchased, openOrNot, status, directOrDel,
                category, hashtag, title, content, member,imageURL);
    }


    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<2;i++) {
            String path = "D:\\22-12-Parabom-Project-new";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("거래 상태 변경 테스트")
    void changeFinOrIng() {
        Post post = beforeEach();
        post.modifyFinOrIng(1);
        postService.modifyFinOrIng(post.getId(), 1);
        assertEquals(1, postService.productDetail(post.getId()).getFinOrIng());
    }

    @Test
    @DisplayName("게시물 수정 테스트")
    void modifyPostTest() throws Exception {
        Post post = beforeEach();
        Long postId = post.getId();

        PostDto.ModifyRequest modifyRequest =
                new PostDto.ModifyRequest(300000, 1, "Very Good",
                        "delivery", "기타", "인형");
        postService.postUpdate(postId, modifyRequest);

        //then
        assertEquals(300000, postService.productDetail(postId).getPrice());
        assertEquals(1, postService.productDetail(postId).getOpenOrNot());
        assertEquals("인형", postService.productDetail(postId).getHashtag());
    }


    @Test
    @DisplayName("게시물 삭제 테스트")
    void deletePostTest() {
        Post post = beforeEach();
        Member member = post.getMember();

        List<PostDto.PostDetailDto> beforeDeletePost = postService.getAllPostInfo();
        int beforePostsSize = member.getPosts().size();

        Long postId = post.getId();
        postService.postDelete(postId);

        List<PostDto.PostDetailDto> afterDeletePost = postService.getAllPostInfo();
        int afterPostsSize = member.getPosts().size();


        assertEquals(1, beforeDeletePost.size() - afterDeletePost.size());
        assertEquals(1, beforePostsSize - afterPostsSize);
    }

    @Test
    @DisplayName("게시글 상세조회 테스트")
    public void 게시글_상세조회() throws Exception {
        // given
        Post post = beforeEach();

        // when
        PostDetailDto postDetailDto = postService.productDetail(post.getId());

        // then
        assertEquals(post.getName(), postDetailDto.getName());
        assertEquals(post.getPrice(), postDetailDto.getPrice());
        assertEquals(post.getTitle(), postDetailDto.getTitle());
        assertEquals(post.getContent(), postDetailDto.getContent());
        assertEquals(post.getMember().getId(), postDetailDto.getMemberId());
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    public void 게시글_등록() throws Exception {

        // given
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));

        PostCreateDto postCreateDto = PostCreateDto.builder()
                .name("ps4").price(300000).finOrIng(0).datePurchased(getDate(2016, 1, 1))
                .date(Timestamp.valueOf(LocalDateTime.now())).openOrNot(0).status("good")
                .directOrDel("direct").category("전자제품").hashtag("게임기").title("ps4")
                .content("ps4").memberId(1L).build();

        // when
        IdResponse postId = postService.postCreate(postCreateDto);

        // then
        PostDetailDto postDetailDto = postService.productDetail(postId.getId());

        assertEquals("ps4", postDetailDto.getName());
        assertEquals(300000, postDetailDto.getPrice());
        assertEquals("게임기", postDetailDto.getHashtag());
    }

    @Test
    @DisplayName("카테고리 조회 테스트")
    public void 카테고리_조회() throws Exception {
        Post post = beforeEach();
        List<PostDetailDto> postDetailDtos = postService.getAllPostInfoByCategory("전자제품");

        assertEquals(1, postDetailDtos.size());

        PostDetailDto postDetailDto = postDetailDtos.get(0);
        assertEquals("ps5", postDetailDto.getName());
        assertEquals(500000, postDetailDto.getPrice());
        assertEquals("게임기", postDetailDto.getHashtag());
    }

    @Test
    @DisplayName("좋아요 테스트")
    public void 좋아요() throws Exception {
        Post post = beforeEach();
        PostDetailDto postDetailDto = postService.productDetail(post.getId());

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
        PostDetailDto postDetailDto = postService.productDetail(post.getId());

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
        PostDetailDto postDetailDto = postService.productDetail(post.getId());

        postLikesService.clickPostLikes(Integer.toUnsignedLong(1), postDetailDto.getId());

        List<PostDetailDto> allPostInfoLiked = postService.getAllPostInfoLiked(Integer.toUnsignedLong(1));
        PostDetailDto dto = allPostInfoLiked.get(0);

        assertEquals("ps5", dto.getName());
        assertEquals(500000, dto.getPrice());
        assertEquals("게임기", dto.getHashtag());
    }

    @Test
    @DisplayName("특정 회원이 올린 게시물 조회 테스트")
    public void 회원_게시물_목록_조회() throws Exception {
        Post post1 = beforeEach();
        Post post2 = beforeEach();
        PostDetailDto postDetailDto1 = postService.productDetail(post1.getId());
        PostDetailDto postDetailDto2 = postService.productDetail(post2.getId());

        postService.getMemberPost(postDetailDto1.getId());

        List<PostDetailDto> getMemberPost = postService.getMemberPost(Integer.toUnsignedLong(1));
        PostDetailDto dto1 = getMemberPost.get(0);
        PostDetailDto dto2 = getMemberPost.get(1);

        assertEquals("ps5", dto1.getName());
        assertEquals(500000, dto1.getPrice());
        assertEquals("게임기", dto2.getHashtag());
    }

    @Test
    @DisplayName("거래 완료 테스트")
    void dealCompleteTest() {
        Post post1 = beforeEach();
        Post post2 = beforeEach();
        Post post3 = beforeEach();

        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(2));

        postService.dealComplete(post1.getId(), member.get().getId());
        postService.dealComplete(post2.getId(), member.get().getId());
        postService.dealComplete(post3.getId(), member.get().getId());

        assertEquals(3, member.get().getBuyList().size());
    }

    @Test
    @DisplayName("구매 내역 조회 테스트")
    void getMemberBuyListTest() {
        Post post1 = beforeEach();
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(2));

        postService.dealComplete(post1.getId(), member.get().getId());

        List<PostDto.PostDetailDto> posts = postService.getMemberBuyList(member.get().getId());

        assertEquals(post1.getName(), posts.get(0).getName());
    }

    @Test
    @DisplayName("이미지 URL 추가 테스트")
    void 이미지_URL_추가() {

        // given
        Post post1 = beforeEach();
        String url = "post/images/post/1";

        // when
        postService.addImageURL(post1.getId(), url);

        // then
        assertEquals("post/images/post/1", post1.getImage());
    }

}