package taveSpring.parabom.Controller.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import taveSpring.parabom.Domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MemberDto {

    @Getter @Setter
    public static class SaveRequest{
        @NotEmpty(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotEmpty(message = "닉네임을 입력해주세요.")
        private String nickname;
        private String profile;
        private String address;

        public Member toMember(){
            return Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .address(address)
                    .profile(profile)
                    .build();
        }
    }

    @Getter @Setter
    public static class LoginRequest{
        @NotEmpty(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter @Setter
    public static class ModifyRequest{
        private String nickname;
        private String profile;
        private String address;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdResponse{
        private Long id;
        public static IdResponse of(Member member){
            return new IdResponse(member.getId());
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberInfoResponse{
        private Long id;
        private String email;
        private String nickname;
        private String profile;
        private String address;

        public static MemberInfoResponse of(Member member){
            return new MemberInfoResponse(member.getId(), member.getEmail(),member.getNickname(),
                    member.getProfile(), member.getAddress());
        }
    }
}
