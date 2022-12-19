package taveSpring.parabom.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taveSpring.parabom.Controller.Dto.MemberDto;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Repository.MemberRepository;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.stream.Collectors;

import static taveSpring.parabom.Controller.Dto.MemberDto.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /*회원가입*/
    @Transactional
    public IdResponse signUp(SaveRequest request){
        if(memberRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("이미 가입되어 있는 이메일입니다.");
        Member member = request.toMember();
        memberRepository.save(member);
        return IdResponse.of(member);
    }

    /*로그인*/
    @Transactional
    public void login(LoginRequest request){
        Member member = memberRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 정보가 없습니다."));
        if(!request.getPassword().equals(member.getPassword()))
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }

    /*아이디로 단일 회원 조회*/
    public MemberInfoResponse getMemberInfoById(Long memberId){
        return memberRepository.findById(memberId)
                .map(MemberInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("회원 아이디가 올바르지 않습니다."));
    }

    /*이메일로 단일 회원 조회*/
    public MemberInfoResponse getMemberInfoByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(MemberInfoResponse::of)
                .orElseThrow(()-> new IllegalArgumentException("회원 이메일이 올바르지 않습니다."));
    }

    /*모든 회원 조회*/
    public List<MemberInfoResponse> getAllMemberInfo(){
        return memberRepository.findAll().stream()
                .map(MemberInfoResponse::of)
                .collect(Collectors.toList());
    }

    /*회원 정보 수정 */
    @Transactional
    public void modifyMemberInfo(Long id, ModifyRequest request){
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다."));
        member.update(request.getNickname(),request.getProfile(),request.getAddress());
    }

    /*회원 탈퇴*/
    @Transactional
    public void deleteMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다."));
        memberRepository.delete(member);
    }

}
