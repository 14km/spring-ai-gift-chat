package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.GiftReq;
import gift.dto.GiftRes;
import gift.exception.GlobalExceptionHandler;
import gift.service.GiftService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GiftController.class)
@Import(GlobalExceptionHandler.class)
class GiftControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GiftService giftService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 선물_질문_요청() throws Exception {
        // given
        var req = new GiftReq("친구의 생일 선물을 추천해주세요", "sessionId-1");
        given(giftService.chat(any())).willReturn(new GiftRes("1", "친구의 선물은 이것을 추천드립니다.", 0));

        // when & then
        mockMvc.perform(post("/api/gift-chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("친구의 선물은 이것을 추천드립니다."));
    }

    @Test
    void 선물_추천_실패시_500_응답() throws Exception {
        // given
        var req = new GiftReq("친구의 생일 선물을 추천해주세요", "sessionId-1");
        given(giftService.chat(any())).willThrow(new IllegalStateException("선물 추천에 실패했습니다"));

        // when & then
        mockMvc.perform(post("/api/gift-chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}