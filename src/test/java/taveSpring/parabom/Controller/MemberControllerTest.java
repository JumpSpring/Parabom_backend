package taveSpring.parabom.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import taveSpring.parabom.Controller.Response.ExceptionController;
import taveSpring.parabom.Domain.Member;
import taveSpring.parabom.Service.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static taveSpring.parabom.Controller.Dto.MemberDto.MemberInfoResponse;

@SpringBootTest
//TODO : 아이디로 단일 검색 제외 나머지 Test 코드 작성
public class MemberControllerTest {

    private MockMvc mvc;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;

    List<MemberInfoResponse> members = new ArrayList<>();
    Member member1 = getMember("email1@gmail.com","password","nickname1","profile","address");
    Member member2 = getMember("email2@gmail.com","password","nickname2","profile","address");
    Member member3 = getMember("email3@gmail.com","password","nickname3","profile","address");
    Member member4 = getMember("email4@gmail.com","password","nickname4","profile","address");
    Member member5 = getMember("email5@gmail.com","password","nickname5","profile","address");

    @BeforeEach
    void setUp(@Autowired MemberController memberController){
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(ExceptionController.class)
                .build();
        //MockMembers
        members.add(MemberInfoResponse.of(member1));
        members.add(MemberInfoResponse.of(member2));
        members.add(MemberInfoResponse.of(member3));
        members.add(MemberInfoResponse.of(member4));
        members.add(MemberInfoResponse.of(member5));
    }

    public String toJsonString(Member member) throws JsonProcessingException {
        return objectMapper.writeValueAsString(member);
    }

    /*멤버 아이디로 단일 조회 */
    @Test
    @DisplayName("멤버 아이디로 단일 조회")
    void findMemberWithId() throws Exception{
        //given
        given(memberService.getMemberInfoById(any())).willReturn(MemberInfoResponse.of(member1));

        //when
        ResultActions actions = mvc.perform(get("/member/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("email1@gmail.com"));
    }

    /*멤버 아이디로 단일 조회 */
    @Test
    @DisplayName("멤버 아이디로 단일 조회 - 예외")
    void findMemberWithId_Exception() throws Exception{
        //given
        given(memberService.getMemberInfoById(any())).willThrow(new IllegalArgumentException("회원 아이디가 올바르지 않습니다."));

        //when
        ResultActions actions = mvc.perform(get("/member/1"));

        //then
        actions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("회원 아이디가 올바르지 않습니다."))
                .andExpect(jsonPath("$.errorCode").value("404"));
    }

    private Member getMember(String email, String password, String nickname, String profile, String address){
        return Member.builder()
                .email(email)
                .password(password)
                .profile(profile)
                .nickname(nickname)
                .address(address)
                .build();
    }
}
