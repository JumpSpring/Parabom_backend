package taveSpring.parabom.Controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.PostLikesService;
import taveSpring.parabom.Service.PostService;
import taveSpring.parabom.Service.S3Service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static taveSpring.parabom.Controller.Dto.PostDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostLikesService postLikesService;
    private final S3Service s3Service;

    /*게시물 상세조회 */
    @GetMapping(path = "/productDetail")
    public ResponseEntity<? extends BasicResponse> productDetail(
            @RequestParam(value="id") Long id) throws Exception {
        return ResponseEntity.ok(new CommonResponse<PostDetailDto>(postService.productDetail(id)));
    }

    /*게시물 등록*/
    @PostMapping(path = "/create")
    public ResponseEntity<? extends BasicResponse> create(
            @RequestBody PostCreateDto dto) throws IOException
    {
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(postService.postCreate(dto)));
    }

    /*게시글 이미지 설정 */
    @PostMapping(path = "/image/{post-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?extends BasicResponse> setImage(
            @PathVariable("post-id") Long id,
            @RequestPart(value = "file",required = false) MultipartFile image) throws IOException
    {
        String UUID = "post/images/post"+id;
        String url = s3Service.uploadFile(image, UUID);
        postService.addImageURL(id,url);
        return ResponseEntity.ok().build();
    }

    /*게시글 찜하기 클릭*/
    @PostMapping("/addHeart")
    public ResponseEntity<? extends BasicResponse> clickPostLike(
            @RequestParam("memberId") Long memberId,
            @RequestParam("postId") Long postId) throws Exception
    {
        postLikesService.clickPostLikes(memberId, postId);
        return ResponseEntity.ok().build();
    }

    /*찜한 목록 조회*/
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
                                                              @RequestBody PostDto.ModifyRequest request) {
        postService.postUpdate(postId, request);
        return ResponseEntity.ok().build();
    }

    /*구매 상태 변경*/
    @PatchMapping(path = "/productState/{post-id}")
    public ResponseEntity<? extends BasicResponse> changeFinOrIng(@PathVariable("post-id") Long postId,
                                                                  @RequestParam(value="finOrIng") Integer finOrIng) {
        postService.modifyFinOrIng(postId, finOrIng);
        return ResponseEntity.ok().build();
    }

    /*상품 삭제*/
    @DeleteMapping(path = "/{post-id}")
    public ResponseEntity<? extends BasicResponse> deletePost(@PathVariable("post-id") Long postId) {
        postService.postDelete(postId);
        return ResponseEntity.ok().build();
    }

    /*거래 완료*/
    @PatchMapping(path = "/dealComplete/{post-id}")
    public ResponseEntity<? extends BasicResponse> dealComplete(@PathVariable("post-id") Long postId,
                                                                @RequestParam(value="buyerId") Long buyerId) {
        postService.dealComplete(postId, buyerId);
        return ResponseEntity.ok().build();
    }

    /*구매 내역 조회*/
    @GetMapping(path="/buylist/{member-id}")
    public ResponseEntity<? extends BasicResponse> getMemberBuyList(@PathVariable("member-id") Long id) {
        postService.getMemberBuyList(id);
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(postService.getMemberBuyList(id)));
    }
}
