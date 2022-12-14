package taveSpring.parabom.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import taveSpring.parabom.Controller.Dto.MemberDto;
import taveSpring.parabom.Controller.Response.BasicResponse;
import taveSpring.parabom.Controller.Response.CommonResponse;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Service.MemberService;
import taveSpring.parabom.Service.S3Service;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static taveSpring.parabom.Controller.Dto.MemberDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
//TODO: AWS S3 연결 후 이미지 처리
public class MemberController {
    private final MemberService memberService;
    private final S3Service s3Service;

    /*회원가입*/
    //TODO : Spring Security 적용
    @PostMapping(path = "/signUp",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<? extends BasicResponse> signUp(
           @Valid @RequestBody SaveRequest request)
    {
        return ResponseEntity.ok()
                .body(new CommonResponse<IdResponse>(memberService.signUp(request)));
    }

    /*프로필 이미지 설정 */
    @PostMapping(path = "/profile/{member-id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<? extends BasicResponse> setProfile(
            @PathVariable("member-id") Long id,
            @RequestPart(value = "file",required = false) MultipartFile profile) throws IOException {
        String UUID = "user/profile/User"+id;
        String url = s3Service.uploadFile(profile,UUID);
        memberService.modifyMemberInfo(id,new ModifyRequest(null,url,null));
        return ResponseEntity.ok().build();
    }

    /*로그인*/
    //Todo : Spring Security 적용
    @PostMapping(path = "/login")
    public ResponseEntity<? extends BasicResponse> login(
           @Valid @RequestBody LoginRequest request
    ){
        memberService.login(request);
        return ResponseEntity.ok().build();
    }

    /*아이디로 회원 단일 조회*/
    @GetMapping(path = "/{member-id}")
    public ResponseEntity<? extends BasicResponse> findMemberById(
            @PathVariable("member-id") Long id
    ){
        return ResponseEntity.ok(
                new CommonResponse<MemberInfoResponse>(
                        memberService.getMemberInfoById(id)));
    }

    /*이메일로 회원 단일 조회*/
    @GetMapping(path = "/email/{email}")
    public ResponseEntity<? extends BasicResponse> findMemberByEmail(
            @PathVariable("email") String email
    ){
        return ResponseEntity.ok(
                new CommonResponse<MemberInfoResponse>(
                        memberService.getMemberInfoByEmail(email)));
    }

    /*전체 회원 조회*/
    @GetMapping(path = "/all")
    public ResponseEntity<? extends BasicResponse> findAllMembers(){
        return ResponseEntity.ok()
                .body(new CommonResponse<List>(memberService.getAllMemberInfo()));
    }

    /*회원 정보 수정*/
    @PatchMapping(path = "/{member-id}")
    public ResponseEntity<? extends BasicResponse> modifyMemberInfo(
            @PathVariable("member-id") Long id,
            @RequestBody ModifyRequest request
    ){
        memberService.modifyMemberInfo(id, request);
        return ResponseEntity.ok().build();
    }

    /*회원 탈퇴*/
    @DeleteMapping(path = "/{member-id}")
    public ResponseEntity<? extends BasicResponse> deleteMember(
            @PathVariable("member-id") Long id
    ){
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }
}
