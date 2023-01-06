package taveSpring.parabom.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.jandex.PositionBasedTypeTarget;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.ImageDto;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Controller.Response.ExceptionController;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.ImageRepository;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostLikesRepository;
import taveSpring.parabom.Repository.PostRepository;
import taveSpring.parabom.Service.PostImageService;
import taveSpring.parabom.Service.PostLikesService;
import taveSpring.parabom.Service.PostService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

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
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ImageRepository imageRepository;

    List<PostDto.PostDetailDto> posts = new ArrayList<>();
    List<PostDto.PostDetailDto> postsByCategory = new ArrayList<>();
    List<PostDto.PostDetailDto> postsByMember = new ArrayList<>();

    public Post beforeEach() { // post 생성하여 저장
        Optional<Member> member = memberRepository.findById(Integer.toUnsignedLong(1));
        Post post = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member.get());

        // 이미지 추가
        Image image = getImage(post, member.get(), "fileName.jpg", "oriFileName.jpg" ,
                "D:\\22-12-Parabom-Project-new");
        List<Image> imageList = new ArrayList<>();
        imageList.add(image);
        post.setImages(imageList);
        List<ImageDto.ImageInfoDto> imageDtoList = new ArrayList<>();
        ImageDto.ImageInfoDto dto = ImageDto.ImageInfoDto.of(image);
        imageDtoList.add(dto);
        imageRepository.save(image);

        return postRepository.save(post);
    }

    public void setUp() {
        Optional<Member> member1 = memberRepository.findByEmail("email1@gmail.com");
        Optional<Member> member2 = memberRepository.findByEmail("email2@gmail.com");
        Optional<Member> member3 = memberRepository.findByEmail("email3@gmail.com");

        Post post1 = getPost("ps5", 500000, 0, getDate(2018, 8, 15),
                0, "good", "direct", "전자제품", "게임기", "ps5",
                "ps5", member1.get());

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

        PostDto.PostDetailDto postDetailDto1 = new PostDto.PostDetailDto(post1);
        PostDto.PostDetailDto postDetailDto2 = new PostDto.PostDetailDto(post2);
        PostDto.PostDetailDto postDetailDto3 = new PostDto.PostDetailDto(post3);
        PostDto.PostDetailDto postDetailDto4 = new PostDto.PostDetailDto(post4);
        PostDto.PostDetailDto postDetailDto5 = new PostDto.PostDetailDto(post5);

        posts.add(postDetailDto1); postsByCategory.add(postDetailDto1);
        posts.add(postDetailDto2); postsByCategory.add(postDetailDto2);
        posts.add(postDetailDto3);
        posts.add(postDetailDto4); postsByMember.add(postDetailDto4);
        posts.add(postDetailDto5); postsByCategory.add(postDetailDto5);
    }

    public Date getDate(int y, int m, int d) {
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

    private Image getImage(Post post, Member member, String fileName, String oriFileName, String path) {
        return Image.createImage(post, member, fileName, oriFileName, path);
    }

    public String toJsonString(Post post) throws JsonProcessingException {
        return objectMapper.writeValueAsString(post);
    }

    //@Test
    @DisplayName("게시물 상세조회 테스트")
    @Transactional
    void 게시물_상세조회() throws Exception {

        Post post = beforeEach();

        PostDto.PostDetailDto postDetailDto = postService.productDetail(post.getId());


        /*given(postService.productDetail(post.getId())).willReturn(
                new PostDto.PostDetailDto(post)
        );

        mvc.perform(
                get("post/productDetial?id=" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath(post.getMember().getNickname()).exists())
                .andExpect(jsonPath(post.getName()).exists())
                .andExpect(jsonPath(post.getTitle()).exists())
                .andExpect(jsonPath(post.getImages().get(0).getPath()).exists())
                .andDo(print());

        verify(postService).productDetail(post.getId());*/
    }

    @Test
    @DisplayName("게시물 등록 테스트")
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
        String path = "D:\\22-12-Parabom-Project-new";
        String imageName = "image.jpg";
        MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
        multipartFileList.add(multipartFile);

        List<Image> imageList = new ArrayList<>();
        for(int i=0; i<2; i++) {
            Image image = new Image();
            postCreateDto.setImage(image);
            imageList.add(image);
        }
        postCreateDto.setImages(imageList);


        // given
        String content = toJsonString(postCreateDto.toEntity());
        given(postService.postCreate(postCreateDto, multipartFileList)).willReturn(new PostDto.PostCreateDto().getId());


        // when & then
        mvc.perform(
                multipart("/post/create")
                        .file(multipartFile)
                        .file(new MockMultipartFile("dto", "", "application/json", content.getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated());


//        mvc.perform(MockMvcRequestBuilders.
//                        multipart("/post/create")
//                        .file(multipartFile)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                )
//                .andExpect(status().isCreated())
//                //.andExpect( postCreateDto.getImages().get(0).getPath()).exists()
//                //.andExpect((ResultMatcher) jsonPath(imageList.get(0).getPost().getName()).exists())
//                .andDo(print());
//
//        mvc.perform(
//                post("/post/create")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.dto").exists())
//                .andExpect(jsonPath(postCreateDto.getMember().getNickname()).exists())
//                .andExpect(jsonPath(postCreateDto.getName()).exists())
//                .andExpect(jsonPath(postCreateDto.getTitle()).exists())
//                .andDo(print());


        verify(postService).postCreate(new PostDto.PostCreateDto(), multipartFileList);

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