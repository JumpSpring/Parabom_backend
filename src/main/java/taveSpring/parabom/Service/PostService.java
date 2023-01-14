package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Controller.Dto.PostSearch;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Domain.Post;
import taveSpring.parabom.Repository.MemberRepository;
import taveSpring.parabom.Repository.PostLikesRepository;
import taveSpring.parabom.Repository.PostRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static taveSpring.parabom.Controller.Dto.PostDto.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;

    /*게시물 상세조회*/
    @Transactional
    public PostDetailDto productDetail(Long id) {
        return postRepository.findById(id)
                .map(PostDetailDto::of)
                .orElseThrow(()->new IllegalArgumentException("post 정보가 없습니다."));
    }

    /*게시물 등록*/
    @Transactional
    public IdResponse postCreate(PostCreateDto postDto){
        // 게시물 등록
        Post post = postDto.toEntity();
        postRepository.save(post);
        Member member = memberRepository.findById(postDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
        post.setMember(member);
        return new IdResponse(post.getId());
    }

    /*이미지 URL 추가*/
    @Transactional
    public void addImageURL(Long postId,String url){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("해당 post를 찾을 수 없습니다."));
        post.setImage(url);
    }


    /*게시물 전체조회*/
    public List<PostDetailDto> getAllPostInfo() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostDetailDto(post)).collect(Collectors.toList());
    }

    /*카테고리 조회*/
    public List<PostDetailDto> getAllPostInfoByCategory(String categoryName) {
        return postRepository.findAllByCategory(categoryName).stream()
                .map(post -> new PostDetailDto(post)).collect(Collectors.toList());
    }

    /*찜한 목록 조회*/
    public List<PostDetailDto> getAllPostInfoLiked(Long memberId) {
        return postRepository.findAllListOfLiked(memberId).stream()
                .map(post -> new PostDetailDto(post)).collect(Collectors.toList());
    }

    /*특정 회원의 게시물 리스트 조회*/
    public List<PostDetailDto> getMemberPost(Long memberId) {
        return postRepository.findAllByMemberId(memberId).stream()
                .map(post -> new PostDetailDto(post)).collect(Collectors.toList());
    }

    /*구매 상태 변경*/
    @Transactional
    public void modifyFinOrIng(Long postId, Integer finOrIng) {
        // 상태변경 기능
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("게시물 정보가 없습니다."));
        post.modifyFinOrIng(finOrIng);
    }

    /*거래 완료*/
    @Transactional
    public void dealComplete(Long postId, Long buyerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("게시물 정보가 없습니다."));
        Member buyer = memberRepository.findById(buyerId)
                        .orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다."));
        post.dealComplete(buyer);
        buyer.addBuyList(post);
    }

    /*게시물 삭제*/
    @Transactional
    public void postDelete(Long id) {
        Post post = postRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("게시물 정보가 없습니다."));
        Member member = post.getMember();
        member.getPosts().remove(post);
        postRepository.delete(post);
    }

    /*게시물 수정*/
    @Transactional
    public void postUpdate(Long id, PostDto.ModifyRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("게시물 정보가 없습니다."));
        // 글 수정
        post.update(request.getPrice(), request.getOpenOrNot(), request.getStatus(),
                request.getDirectOrDel(), request.getCategory(), request.getHashtag());

    }

    /*구매 내역 조회*/
    public List<PostDto.PostDetailDto> getMemberBuyList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다."));
        List<Post> posts = member.getBuyList();
        return posts.stream()
                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
    }

    public List<PostDto.PostDetailDto> getPostBySearch(PostSearch postSearch) {
        return postRepository.searchPosts(postSearch).stream()
                .map(post -> new PostDto.PostDetailDto(post)).collect(Collectors.toList());
    }
}
