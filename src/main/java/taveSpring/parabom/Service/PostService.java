package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static taveSpring.parabom.Controller.Dto.PostDto.*;



@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /*게시물 상세조회*/
    public PostDto.PostDetailDto productDetail(Long id) {
        Post entity = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return new PostDto.PostDetailDto(entity);
    }

    /*게시물 등록*/
    @Transactional
    public Long postCreate(PostDto.PostCreateDto dto) {
        return postRepository.save(dto.toEntity()).getId();
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
//    public List<PostDto.PostDetailDto> getAllPostInfoLiked(Long memberId) {
//        return postRepository.findAllListOfLiked(memberId).stream()
//                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
//    }


    /*구매 상태 변경*/
    @Transactional
    public void changeFinOrIng(Long id, ModifyFinOrIngRequest request) {
        // 상태변경 기능
        Post post = postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("게시물 정보가 없습니다."));
        post.updateFinOrIng(request.getFinOrIng());
    }

    /*게시물 삭제*/
    @Transactional
    public void postDelete(Long id) {
        Post post = postRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("게시물 정보가 없습니다."));
        postRepository.delete(post);
    }

    /*게시물 수정*/
    @Transactional
    public void postChange(Long id, ModifyRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("게시물 정보가 없습니다."));
        post.update(request.getPrice(), request.getOpenOrNot(), request.getStatus(),
                request.getDirectOrDel(), request.getCategory(), request.getHashtag());
    }
}
