package taveSpring.parabom.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Domain.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static taveSpring.parabom.Controller.Dto.MemberDto.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;

    Member member1 = getMember("test1@gmail.com","test1Password",
            "test1Nickname","test1Profile","test1Address");
    Member member2 = getMember("test2@gmail.com","test2Password",
            "test2Nickname","test2Profile","test2Address");
    Member member3 = getMember("test3@gmail.com","test3Password",
            "test3Nickname","test3Profile","test3Address");
    SaveRequest member_request1 = SaveRequest.of(member1);
    SaveRequest member_request2 = SaveRequest.of(member2);
    SaveRequest member_request3 = SaveRequest.of(member3);

    @Test
    void 회원가입_and_회원단일검색byID(){
        IdResponse response = memberService.signUp(member_request1);
        assertEquals(member1.getEmail(),memberService.getMemberInfoById(response.getId()).getEmail());
    }

    @Test
    void 회원가입_and_회원단일검색byEmail(){
        IdResponse response = memberService.signUp(member_request1);
        assertEquals(member1.getEmail(),memberService.getMemberInfoByEmail(member_request1.getEmail()).getEmail());
    }

    @Test
    void 모든회원검색(){
        List<MemberInfoResponse> initMembers = memberService.getAllMemberInfo();

        memberService.signUp(member_request1);
        memberService.signUp(member_request2);
        memberService.signUp(member_request3);

        List<MemberInfoResponse> addMembers = memberService.getAllMemberInfo();
        assertEquals(3, addMembers.size()- initMembers.size());
    }

    @Test
    void 회원정보수정(){
        IdResponse response = memberService.signUp(member_request1);
        ModifyRequest request = new ModifyRequest("modifyNickname",null,null);
        memberService.modifyMemberInfo(response.getId(),request);
        assertEquals("modifyNickname", memberService.getMemberInfoById(response.getId()).getNickname());
        assertEquals(member1.getAddress(), memberService.getMemberInfoById(response.getId()).getAddress());
    }

    @Test
    void 회원탈퇴(){
        IdResponse response = memberService.signUp(member_request1);
        List<MemberInfoResponse> BeforeDeleteMembers = memberService.getAllMemberInfo();
        memberService.deleteMember(response.getId());
        List<MemberInfoResponse> AfterDeleteMembers = memberService.getAllMemberInfo();
        assertEquals(1, BeforeDeleteMembers.size()-AfterDeleteMembers.size());
    }

    private Member getMember(String email, String password, String nickname, String profile, String address){
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .address(address)
                .profile(profile)
                .build();
    }
}
