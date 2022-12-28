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

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /* 게시물 상세조회*/
    // TODO : Member 객체 넣기 완료
    // TODO : 내 게시물 상세조회와 타 게시물 상세조회 분리
    @GetMapping(path = "/productDetail")
    public ResponseEntity<? extends BasicResponse> productDetail(@RequestParam(value="id") Long id) throws Exception {
        return ResponseEntity.ok(new CommonResponse<PostDto.PostDetailDto>(postService.productDetail(id)));
    }

    /*게시물 등록*/
    // TODO : 로그인 정보 받아오기 - 게시물 내용과 이미지에 member_id 적용
    @PostMapping(path = "/create")
    public ResponseEntity<? extends BasicResponse> create(@RequestPart("dto") PostDto.PostCreateDto dto,
                                                          @RequestPart("image") List<MultipartFile> imageFileList,
                                                          Model model) throws Exception {
        try { // 상품 저장 로직 호출
            postService.postCreate(dto, imageFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage : ", "게시물 등록 중 에러 발생!");
        }
        return ResponseEntity.ok().build();
    }

    /*찜한 목록에 추가
    @PostMapping(path = "/addHeart")
    public ResponseEntity<? extends BasicResponse> addHeart(@RequestBody PostDto.AddHeartDto addHeartDto,
                                                            Principal principal) throws Exception {

        String email = principal.getName();
        postService.addHeart(addHeartDto, email);
        return ResponseEntity.ok(new CommonResponse<PostDto.AddHeartDto>(postService.addHeart(addHeartDto, email)));
    }*/

}
