package taveSpring.parabom.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.PostLikesService;
import taveSpring.parabom.Service.PostService;

import javax.persistence.Basic;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostLikesService postLikesService;

    /*게시물 상세조회*/
    // TODO : 멤버 닉네임 & 프사 받아오기
    // 내 게시물 조회와 타인 게시물 조회 따로 구현?
    @GetMapping(path = "/productDetail")
    public ResponseEntity<? extends BasicResponse> productDetail(@RequestParam(value="id") Long id) throws Exception {
        return ResponseEntity.ok(new CommonResponse<PostDto.PostDetailDto>(postService.productDetail(id)));
    }

    /*게시물 등록*/
    // TODO : 사진 등록(최대10개), 로그인 정보 받아오기
    @PostMapping(path = "/create")
    public ResponseEntity<? extends BasicResponse> create(@RequestBody PostDto.PostCreateDto dto) throws Exception {
        postService.postCreate(dto);
        return ResponseEntity.ok().build();
    }

    /*게시물 찜 목록에 추가
    @PostMapping(path = "/addHeart")
    public ResponseEntity<? extends BasicResponse> addHeart(@RequestParam(value="id") Long id) throws Exception {
        postService.addHeart(id);
        return
    }*/

    /*게시글 찜하기 클릭*/
    // TODO : 로그인한 사용자 정보 불러오기
    @PostMapping("/addHeart")
    public ResponseEntity<? extends BasicResponse> clickPostLike(
            @RequestParam("memberId") Long memberId,
            @RequestParam("postId") Long postId) throws Exception
    {
        postLikesService.clickPostLikes(memberId, postId);
        return ResponseEntity.ok().build();
    }

    /*찜한 목록 조회*/
    // TODO : 로그인한 사용자 정보 불러오기
    @GetMapping("/likeList")
    public ResponseEntity<? extends BasicResponse> allPostLiked(@RequestParam("memberId") Long memberId) throws Exception {
        return ResponseEntity.ok().body(new CommonResponse<List>(postService.getAllPostInfoLiked(memberId)));
    }

    /*전체 조회*/
    @GetMapping("/allList")
    public ResponseEntity<? extends BasicResponse> allPosts() {
        return ResponseEntity.ok().body(new CommonResponse<List>(postService.getAllPostInfo()));
    }

    /*카테고리 조회*/
    @GetMapping("/category")
    public ResponseEntity<? extends BasicResponse> findListByCategory(@RequestParam("categoryName") String categoryName) throws Exception {
        return ResponseEntity.ok().body(new CommonResponse<List>(postService.getAllPostInfoByCategory(categoryName)));
    }
}
