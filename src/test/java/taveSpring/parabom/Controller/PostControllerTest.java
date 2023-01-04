package taveSpring.parabom.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.jandex.PositionBasedTypeTarget;
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
import taveSpring.parabom.Repository.PostRepository;
import taveSpring.parabom.Service.PostImageService;
import taveSpring.parabom.Service.PostService;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

        given(postService.productDetail(post.getId())).willReturn(
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

        verify(postService).productDetail(post.getId());
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

}