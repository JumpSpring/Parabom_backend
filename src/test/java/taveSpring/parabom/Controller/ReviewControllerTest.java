package taveSpring.parabom.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import taveSpring.parabom.Controller.Dto.ReviewDto;
import taveSpring.parabom.Controller.Response.ExceptionController;
import taveSpring.parabom.Service.ReviewService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static taveSpring.parabom.Controller.Dto.ReviewDto.*;
import static taveSpring.parabom.Controller.Dto.ReviewDto.IdResponse;
import static taveSpring.parabom.Controller.Dto.ReviewDto.ReviewCreateDto;

@SpringBootTest
public class ReviewControllerTest {

    private MockMvc mvc;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(@Autowired ReviewController reviewController) {
        //MockMvc
        mvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setControllerAdvice(ExceptionController.class)
                .build();
    }

    public String toJsonString(ReviewCreateDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public String toJsonStringModifyDto(ReviewModifyDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    @DisplayName("후기 등록 컨트롤러 테스트")
    void createReviewTest() throws Exception {
        ReviewCreateDto dto = createDto(2L, "판매자", "iphone13", "감사합니다!", 5);

        given(reviewService.saveReview(any(ReviewCreateDto.class), anyLong()))
                .willReturn(new IdResponse(1L));

        String content = toJsonString(dto);

        mvc.perform(post("/member/1/review")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.count").exists());

        verify(reviewService).saveReview(any(ReviewCreateDto.class), anyLong());
    }

    @Test
    @DisplayName("후기 수정 컨트롤러 테스트")
    void modifyReviewTest() throws Exception {
        ReviewModifyDto modifyDto = new ReviewModifyDto("iphone13 mini", "감사합니다!!!", 4);

        String content = toJsonStringModifyDto(modifyDto);

        mvc.perform(patch("/member/1/review/1")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("후기 삭제 컨트롤러 테스트")
    void deleteReviewTest() throws Exception {
        mvc.perform(delete("/member/1/review/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public ReviewCreateDto createDto(Long senderId, String senderType, String itemName, String text, Integer starPoint) {
        return new ReviewCreateDto(senderId, senderType, itemName, text, starPoint);
    }
}
