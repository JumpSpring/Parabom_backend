package taveSpring.parabom.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.PostDto;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Service.PostService;

import javax.persistence.Basic;

import static taveSpring.parabom.Controller.Dto.PostDto.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /*게시물 상세조회*/
    // TODO : 멤버 닉네임 & 프사 받아오기
    // 내 게시물 조회와 타인 게시물 조회 따로 구현?
    @GetMapping(path = "/productDetail")
    public ResponseEntity<? extends BasicResponse> productDetail(@RequestParam(value="id") Long id) throws Exception {
        return ResponseEntity.ok(new CommonResponse<PostDto.PostDetailDto>(postService.productDetail(id)));
    }

    /*게시물 등록*/
    // TODO : 사진 등록, 로그인 정보 받아오기
    @PostMapping(path = "/create")
    public ResponseEntity<? extends BasicResponse> create(@RequestPart("dto") PostDto.PostCreateDto dto,
                                                          @RequestPart("image") List<MultipartFile> imageFileList,
                                                          Model model) throws Exception {
        try { // 상품 저장 로직 호출
            //postService.postCreate(dto, imageFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage : ", "게시물 등록 중 에러 발생!");
        }
        return ResponseEntity.ok().build();
    }



    /*게시물 찜 목록에 추가
    @PostMapping(path = "/addHeart/{id}}")
    public ResponseEntity<? extends BasicResponse> addHeart(@RequestParam(name = "id") Long id) throws Exception {
        postService.addHeart(id);
        return ResponseEntity.ok(new CommonResponse<PostDto.AddHeartDto>(postService.addHeart(id)));
    }*/

    /*구매 상태 변경*/
    @PatchMapping(path = "/productState/{post-id}")
    public ResponseEntity<? extends BasicResponse> changeFinOrIng(@PathVariable("post-id") Long id,
                                                                  ModifyFinOrIngRequest request, Model model) {

        try { // 구매 상태 변경 로직 호출
            postService.changeFinOrIng(id, request);
        }
        catch (Exception e){
            model.addAttribute("errorMessage : ", "구매 상태 변경 중 오류 발생");
        }
        return ResponseEntity.ok().build();
    }

    /*상품 삭제*/
    @DeleteMapping(path = "post/{post-id}")
    public ResponseEntity<? extends BasicResponse> deletePost(@PathVariable("post-id") Long id) {
        postService.postDelete(id);
        return ResponseEntity.ok().build();
    }

    /*게시물 수정*/
    @PatchMapping(path = "post/{post-id}")
    public ResponseEntity<? extends BasicResponse> modifyPost(@PathVariable("post-id") Long id,
                                                              ModifyRequest request) {
        postService.postChange(id, request);
        return ResponseEntity.ok().build();
    }
}
