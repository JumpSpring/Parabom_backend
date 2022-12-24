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
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final ImageRepository imageRepository;

    /*게시물 상세조회*/
    @Transactional
    public PostDto.PostDetailDto productDetail(Long id) {

        // 이미지
        List<Image> imageList = imageRepository.findByPostId(id);
        List<ImageDto.ImageInfoDto> imageDtoList = new ArrayList<>();

        for (Image images : imageList) {
            ImageDto.ImageInfoDto dto = ImageDto.ImageInfoDto.of(images);
            imageDtoList.add(dto);
        }

        // 게시물 내용
        Post entity = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        PostDto.PostDetailDto detailDto = new PostDto.PostDetailDto(entity);
        detailDto.setImageDtoList(imageDtoList);
        System.out.print("[Info][productDetail SERVICE] detailDto.getImageInfoDtos() : " + detailDto.getImageDtoList());
        return new PostDto.PostDetailDto(entity);
    }

    /*게시물 등록*/
    @Transactional
    public Long postCreate(PostDto.PostCreateDto postDto, List<MultipartFile> imageList) throws Exception {

        // 게시물 내용 등록
        //Post post = postRepository.save(postDto.toEntity());

        // 이미지 등록
        for(int i=0 ; i<imageList.size(); i++){
            Image image = new Image();
            //image.setPost(post);
            postImageService.saveImage(image, imageList.get(i));
            System.out.println("[Info][productCreate SERVICE] image.getPath() : " + image.getPath());

            Post postE = new Post(); //new
            postE.addImages(image); //new
            postDto.setImage(image);
        }

        // 게시물 내용 등록
        Post post = postRepository.save(postDto.toEntity());

        // 이미지 엔티티에는 저장에 되는데 post 엔티티에는 저장이 안된다... 왜일까
        System.out.println("[Info][productCreate SERVICE] post.getImages() : " + post.getImages()); // [null]

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

//    /*찜한 목록 조회*/
//    @Transactional
//    public List<PostDto.PostDetailDto> getAllPostInfoLiked(Long memberId) {
//        return postRepository.findAllListOfLiked(memberId).stream()
//                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
//    }

}
