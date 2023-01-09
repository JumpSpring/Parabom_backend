package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.ImageDto;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Image;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.ImageRepository;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostLikesRepository;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final ImageRepository imageRepository;
    private final PostLikesRepository postLikesRepository;

    /*게시물 상세조회*/
    @Transactional
    public PostDto.PostDetailDto productDetail(Long id) {
        // 이미지
        List<Image> imageList = imageRepository.findByIdBy(id);
        List<ImageDto.ImageInfoDto> imageDtoList = new ArrayList<>();

        for (Image images : imageList) {
            ImageDto.ImageInfoDto dto = ImageDto.ImageInfoDto.of(images);
            imageDtoList.add(dto);
        }

        // 게시물 내용
        Post entity = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        PostDto.PostDetailDto detailDto = new PostDto.PostDetailDto(entity);
        detailDto.setImageDtoList(imageDtoList);
        Optional<Member> member = memberRepository.findByEmail(entity.getMember().getEmail());
        detailDto.setMember(member.get());
        //System.out.print("[Info][productDetail SERVICE] detailDto.getImageInfoDtos() : " + detailDto.getImageDtoList());
        //System.out.print("[Info][productDetail SERVICE] imageDtoList.get(0) : " + imageDtoList.get(0));
        return detailDto;
    }

    /*게시물 등록*/
    @Transactional
    public Long postCreate(PostDto.PostCreateDto postDto, List<MultipartFile> imageList) throws Exception {

        // 게시물 등록
        Post post = postDto.toEntity();
        postRepository.save(post);

        // 이미지 등록
        for(int i=0 ; i<imageList.size(); i++){
            Image image = new Image();
            System.out.println("[Info][productCreate SERVICE] imageList.get(0).getName() : " + imageList.get(0).getName());
            //postDto.setImage(image);
            image.setPost(post);
            postImageService.saveImage(image, imageList.get(i));
            System.out.println("[Info][productCreate SERVICE] image.getPath() : " + image.getPath());
        }

        System.out.println("[Info][productCreate SERVICE] post.getImages() : " + post.getImages());
        return post.getId();
    }


    /*게시물 전체조회*/
    public List<PostDto.PostDetailDto> getAllPostInfo() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
    }

    /*카테고리 조회*/
    public List<PostDto.PostDetailDto> getAllPostInfoByCategory(String categoryName) {
        return postRepository.findAllByCategory(categoryName).stream()
                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
    }

    /*찜한 목록 조회*/
    public List<PostDto.PostDetailDto> getAllPostInfoLiked(Long memberId) {
        return postRepository.findAllListOfLiked(memberId).stream()
                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
    }

    /*특정 회원의 게시물 리스트 조회*/
    public List<PostDto.PostDetailDto> getMemberPost(Long memberId) {
        return postRepository.findAllByMemberId(memberId).stream()
                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
    }

}
