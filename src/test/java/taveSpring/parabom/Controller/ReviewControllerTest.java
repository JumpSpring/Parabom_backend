package taveSpring.parabom.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import taveSpring.parabom.Service.ReviewService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static taveSpring.parabom.Controller.Dto.ReviewDto.IdResponse;
import static taveSpring.parabom.Controller.Dto.ReviewDto.ReviewCreateDto;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ReviewService reviewService;
    @Autowired
    private ObjectMapper objectMapper;

    public String toJsonString(ReviewCreateDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    @DisplayName("후기 등록 컨트롤러 테스트")
    void createReviewTest() throws Exception {
        ReviewCreateDto dto = createDto(2L, "판매자", "iphone13", "감사합니다!", 5);

        given(reviewService.saveReview(dto, 1L))
                .willReturn(new IdResponse(1L));

        String content = toJsonString(dto);

        mvc.perform(post("/member/1/review")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.count").exists());

        verify(reviewService).saveReview(refEq(dto), eq(1L));
    }

    public ReviewCreateDto createDto(Long senderId, String senderType, String itemName, String text, Integer starPoint) {
        return new ReviewCreateDto(senderId, senderType, itemName, text, starPoint);
    }
}
