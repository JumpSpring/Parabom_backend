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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.ImageDto;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Service.PostService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.junit.jupiter.api.Assertions.*;

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
    List<ImageDto.ImageInfoDto> imageInfoDtoList = new ArrayList<>();
    List<PostDto.PostDetailDto> posts = new ArrayList<>();
    List<PostDto.PostDetailDto> postsByCategory = new ArrayList<>();
    List<PostDto.PostDetailDto> postsByMember = new ArrayList<>();

    public void setUp() throws Exception {
        Optional<Member> member1 = memberRepository.findByEmail("email1@gmail.com");
        Optional<Member> member2 = memberRepository.findByEmail("email2@gmail.com");
        Optional<Member> member3 = memberRepository.findByEmail("email3@gmail.com");

        Post post1 = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member1.get());
        post1.setId(1L);

        Post post2 = getPost("MacBook", 1700000, 0, getDate(2020, 1, 1),
                0, "good", "direct", "전자제품", "노트북", "MacBook",
                "MacBook", member2.get());

        Post post3 = getPost("clothes", 10000, 0, getDate(2022, 8, 15),
                0, "good", "direct", "의류", "옷", "clothes",
                "clothes", member1.get());

        Post post4 = getPost("book", 5000, 0, getDate(2020, 8, 15),
                0, "good", "direct", "도서", "도서", "book",
                "book", member3.get());

        Post post5 = getPost("tv", 800000, 0, getDate(2021, 8, 15),
                0, "good", "direct", "전자제품", "tv", "tv",
                "tv", member2.get());

        // 이미지
        ImageDto.ImageInfoDto imageInfoDto = new ImageDto.ImageInfoDto(1L, "D:\\22-12-Parabom-Project-new",
                "fileName.jpg", "oriFileName.jpg");
        imageInfoDtoList.add(imageInfoDto);

        PostDto.PostDetailDto postDetailDto1 = new PostDto.PostDetailDto(post1);
        postDetailDto1.setImageDtoList(imageInfoDtoList);
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

    public String toJsonString(PostDto.PostCreateDto createDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(createDto);
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
                .andExpect(jsonPath("$.data.member.nickname").value("nickname1"))
                .andExpect(jsonPath("$.data.title").value("ps5"))
                .andExpect(jsonPath("$.data.imageDtoList").exists())
                .andDo(print());

        verify(postService).productDetail(post.getId());
    }


    @Test
    @DisplayName("게시물 등록 테스트")
    @Transactional
    void 게시물_등록() throws Exception {

        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));

        // 게시물 내용
        PostDto.PostCreateDto postCreateDto = PostDto.PostCreateDto.builder().id(123L)
                .name("ps4").price(300000).finOrIng(0).datePurchased(getDate(2016, 1, 1))
                .date(Timestamp.valueOf(LocalDateTime.now())).openOrNot(0).status("good")
                .directOrDel("direct").category("전자제품").hashtag("게임기").title("ps4")
                .content("ps4").member(member.get()).build();

        // 이미지
        List<MultipartFile> multipartFileList = new ArrayList<>();
        MockMultipartFile multipartFile =
                    new MockMultipartFile("multipartFile", "image.jpg", "image/jpg", new byte[]{1, 2, 3, 4});
        multipartFileList.add(multipartFile);

        // given
        String content = toJsonString(postCreateDto);
        given(postService.postCreate(any(), anyList())).willReturn(new PostDto.PostCreateDto().getId());

        // when & then
        mvc.perform(
                multipart("/post/create")
                        .file("image", multipartFile.getBytes())
                        .file(new MockMultipartFile("dto", "", "application/json", content.getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(postService).postCreate(any(), anyList());

    }

}