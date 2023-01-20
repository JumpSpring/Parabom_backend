package taveSpring.parabom.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Service.PostService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private PostService postService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    MemberRepository memberRepository;

    Post post = new Post();
    List<PostDto.PostDetailDto> posts = new ArrayList<>();
    List<PostDto.PostDetailDto> postsByCategory = new ArrayList<>();
    List<PostDto.PostDetailDto> postsByMember = new ArrayList<>();
    List<PostDto.PostDetailDto> buyList = new ArrayList<>();

    public void setUp() throws Exception {
        Optional<Member> member1 = memberRepository.findByEmail("email1@gmail.com");
        Optional<Member> member2 = memberRepository.findByEmail("email2@gmail.com");
        Optional<Member> member3 = memberRepository.findByEmail("email3@gmail.com");

        Post post1 = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member1.get(),"imageURL");
        post1.setId(1L);

        Post post2 = getPost("MacBook", 1700000, 0, getDate(2020, 1, 1),
                0, "good", "direct", "전자제품", "노트북", "MacBook",
                "MacBook", member2.get(),"imageURL");

        Post post3 = getPost("clothes", 10000, 0, getDate(2022, 8, 15),
                0, "good", "direct", "의류", "옷", "clothes",
                "clothes", member1.get(),"imageURL");

        Post post4 = getPost("book", 5000, 0, getDate(2020, 8, 15),
                0, "good", "direct", "도서", "도서", "book",
                "book", member3.get(),"imageURL");

        Post post5 = getPost("tv", 800000, 0, getDate(2021, 8, 15),
                0, "good", "direct", "전자제품", "tv", "tv",
                "tv", member2.get(),"imageURL");

        PostDto.PostDetailDto postDetailDto1 = new PostDto.PostDetailDto(post1);
        PostDto.PostDetailDto postDetailDto2 = new PostDto.PostDetailDto(post2);
        PostDto.PostDetailDto postDetailDto3 = new PostDto.PostDetailDto(post3);
        PostDto.PostDetailDto postDetailDto4 = new PostDto.PostDetailDto(post4);
        PostDto.PostDetailDto postDetailDto5 = new PostDto.PostDetailDto(post5);

        post = post1;

        posts.add(postDetailDto1); postsByCategory.add(postDetailDto1);
        posts.add(postDetailDto2); postsByCategory.add(postDetailDto2);
        posts.add(postDetailDto3);
        posts.add(postDetailDto4); postsByMember.add(postDetailDto4);
        posts.add(postDetailDto5); postsByCategory.add(postDetailDto5);

        buyList.add(postDetailDto1);
        buyList.add(postDetailDto2);
        buyList.add(postDetailDto3);
        buyList.add(postDetailDto4);
        buyList.add(postDetailDto5);
    }

    private Date getDate(int y, int m, int d) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m-1, d);
        return new Date(cal.getTimeInMillis());
    }

    private Post getPost(String name, int price, Integer foi, Date datePurchased, Integer openOrNot,
                         String status, String directOrDel, String category, String hashtag,
                         String title, String content, Member member,String image) {
        return Post.createPost(name, price, foi, datePurchased, openOrNot, status, directOrDel,
                category, hashtag, title, content, member,image);
    }

    public String toJsonString(PostDto.PostCreateDto createDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(createDto);
    }

    public String toJsonString(PostDto.ModifyRequest modifyRequest) throws JsonProcessingException {
        return objectMapper.writeValueAsString(modifyRequest);
    }

    @Test
    @DisplayName("게시물 상세조회 테스트")
    @Transactional
    void 게시물_상세조회() throws Exception {

        setUp();

        given(postService.productDetail(1L)).willReturn(
                new PostDto.PostDetailDto(post)
        );

        mvc.perform(get("/post/productDetail?id=" + 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("ps5"))
                .andExpect(jsonPath("$.data.title").value("ps5"))
                .andDo(print());

        verify(postService).productDetail(post.getId());
    }


    @Test
    @DisplayName("게시물 등록 테스트")
    @Transactional
    void 게시물_등록() throws Exception {

        // 게시물 내용
        PostDto.PostCreateDto postCreateDto = PostDto.PostCreateDto.builder().memberId(1L)
                .name("ps4").price(300000).finOrIng(0).datePurchased(getDate(2016, 1, 1))
                .date(Timestamp.valueOf(LocalDateTime.now())).openOrNot(0).status("good")
                .directOrDel("direct").category("전자제품").hashtag("게임기").title("ps4")
                .content("ps4").build();

        // given
        String content = toJsonString(postCreateDto);
        given(postService.postCreate(any())).willReturn(new PostDto.IdResponse(2L));

        //when
        ResultActions actions = mvc.perform(post("/post/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());

    }

    @Test
    @DisplayName("게시물 수정 테스트")
    @Transactional
    void 게시물_수정() throws Exception {
        setUp();

        PostDto.ModifyRequest modifyRequest =
                new PostDto.ModifyRequest("Camera", 500000, "camera", "etc",
                        new Date(2023, 01, 10), 1, "veryGood",
                        "delivery", "etc");
        String requestJson = toJsonString(modifyRequest);

        mvc.perform(patch("/post/1")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("거래상태 변경 테스트")
    @Transactional
    void 거래상태_변경() throws Exception {
        setUp();

        mvc.perform(patch("/post/productState/1?finOrIng=1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    @Transactional
    void 게시물_삭제() throws Exception {
        setUp();

        mvc.perform(delete("/post/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("거래 완료 테스트")
    @Transactional
    void 거래_완료() throws Exception {
        setUp();

        mvc.perform(patch("/post/dealComplete/1?buyerId=3"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("구매 내역 조회 테스트")
    void 구매내역_조회() throws Exception {
        setUp();

        given(postService.getMemberBuyList(1L)).willReturn(buyList);

        mvc.perform(get("/post/buylist/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].name").value("ps5"))
                .andExpect(jsonPath("$.data.[1].price").value(1700000))
                .andExpect(jsonPath("$.data.[2].directOrDel").value("direct"))
                .andExpect(jsonPath("$.data.[3].hashtag").value("도서"))
                .andExpect(jsonPath("$.data.[4].status").value("good"))
                .andDo(print());
    }

    @Test
    @DisplayName("전체 게시물 조회")
    void test1() throws Exception {
        setUp();

        given(postService.getAllPostInfo()).willReturn(posts);

        mvc.perform(get("/post/allList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value("5"))
                .andExpect(jsonPath("$.data.[0].name").value("ps5"))
                .andExpect(jsonPath("$.data.[1].name").value("MacBook"))
                .andExpect(jsonPath("$.data.[2].name").value("clothes"))
                .andExpect(jsonPath("$.data.[3].name").value("book"))
                .andExpect(jsonPath("$.data.[4].name").value("tv"))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 조회")
    void test2() throws Exception {
        setUp();

        given(postService.getAllPostInfoByCategory("전자제품")).willReturn(postsByCategory);

        mvc.perform(get("/post/category?categoryName=전자제품"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value("3"))
                .andExpect(jsonPath("$.data.[0].name").value("ps5"))
                .andExpect(jsonPath("$.data.[0].memberInfoResponse.nickname").value("nickname1"))
                .andExpect(jsonPath("$.data.[1].name").value("MacBook"))
                .andExpect(jsonPath("$.data.[1].memberInfoResponse.nickname").value("nickname2"))
                .andExpect(jsonPath("$.data.[2].name").value("tv"))
                .andExpect(jsonPath("$.data.[2].memberInfoResponse.nickname").value("nickname2"))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 회원의 게시물 리스트 조회")
    void test3() throws Exception {
        setUp();

        Optional<Member> member3 = memberRepository.findByEmail("email3@gmail.com");
        given(postService.getMemberPost(member3.get().getId())).willReturn(postsByMember);

        mvc.perform(get("/post/memberPostList?id=" + member3.get().getId()))
                .andExpect(jsonPath("$.count").value("1"))
                .andExpect(jsonPath("$.data.[0].name").value("book"))
                .andExpect(jsonPath("$.data.[0].memberInfoResponse.nickname").value("nickname3"))
                .andDo(print());
    }
}