package taveSpring.parabom.Controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import taveSpring.parabom.Controller.Dto.ImageDto;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.PostLikesService;
import taveSpring.parabom.Service.PostService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostLikesService postLikesService;

    /* 게시물 상세조회*/
    // TODO : 로그인 정보 받아오기
    @GetMapping(path = "/productDetail")
    public ResponseEntity<? extends BasicResponse> productDetail(@RequestParam(value="id") Long id) throws Exception {
        return ResponseEntity.ok(new CommonResponse<PostDto.PostDetailDto>(postService.productDetail(id)));
    }

    /*게시물 등록*/
    // TODO : 로그인 정보 받아오기
    @PostMapping(path = "/create")
    public ResponseEntity<? extends BasicResponse> create(@RequestPart(value="dto") PostDto.PostCreateDto dto,
                                                          @RequestPart(value="image") List<MultipartFile> imageFileList,
                                                          Model model) throws Exception {
        // 상품 저장 로직 호출
        try {
            postService.postCreate(dto, imageFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage : ", "게시물 등록 중 에러 발생!");
        }
        return ResponseEntity.ok().build();
    }

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

    /*특정 회원의 게시물 리스트 조회*/
    @GetMapping("/memberPostList")
    public ResponseEntity<? extends BasicResponse> memberPostList(@RequestParam(value="memberId") Long memberId) {
        return ResponseEntity.ok().body(new CommonResponse<List>(postService.getMemberPost(memberId)));
    }


    /*게시물 수정*/
    @PatchMapping("/{post-id}")
    public ResponseEntity<? extends BasicResponse> modifyPost(@PathVariable("post-id") Long postId,
                                                              PostDto.ModifyRequest request) {
        postService.postUpdate(postId, request);
        return ResponseEntity.ok().build();
    }

    /*구매 상태 변경*/
    @PatchMapping(path = "/productState/{post-id}")
    public ResponseEntity<? extends BasicResponse> changeFinOrIng(@PathVariable("post-id") Long postId,
                                                                  PostDto.ModifyFinOrIngRequest request) {
        postService.modifyFinOrIng(postId, request);
        return ResponseEntity.ok().build();
    }

    /*상품 삭제*/
    @DeleteMapping(path = "/{post-id}")
    public ResponseEntity<? extends BasicResponse> deletePost(@PathVariable("post-id") Long postId) {
        postService.postDelete(postId);
        return ResponseEntity.ok().build();
    }

    /*거래 완료*/
    @PatchMapping(path = "{post-id}/dealComplete")
    public ResponseEntity<? extends BasicResponse> dealComplete(@PathVariable("post-id") Long postId,
                                                                PostDto.DealCompleteRequest request) {
        postService.dealComplete(postId, request);
        return ResponseEntity.ok().build();
    }

    /*구매 내역 조회*/
    @GetMapping(path="/{member-id}/buylist")
    public ResponseEntity<? extends BasicResponse> getMemberBuyList(
            @PathVariable("member-id") Long id
    ) {
        postService.getMemberBuyList(id);
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(postService.getMemberBuyList(id)));
    }
}
