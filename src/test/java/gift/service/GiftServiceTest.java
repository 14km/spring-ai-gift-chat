package gift.service;

import gift.dto.GiftReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class GiftServiceTest {
    @Mock
    private ChatClient chatClient;
    @Mock
    private ChatClient.Builder builder;
    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;
    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    private GiftService giftService;

    @BeforeEach
    void setUp() {
        given(builder.build()).willReturn(chatClient);
        given(chatClient.prompt()).willReturn(requestSpec);
        given(requestSpec.system(anyString())).willReturn(requestSpec);
        given(requestSpec.user(anyString())).willReturn(requestSpec);
        given(requestSpec.call()).willReturn(responseSpec);

        giftService = new GiftService(builder);
    }

    @Test
    void 정상적인_채팅() {
        // give
        String message = "친구의 생일 선물을 추천해주세요";
        given(responseSpec.content()).willReturn("친구 생일 선물을 추천해드리겠습니다.");

        // when
        var res = giftService.chat(new GiftReq(message, "sessionId-1"));

        // then
        assertThat(res.requestId()).isNotBlank();
        assertThat(res.message()).isEqualTo("친구 생일 선물을 추천해드리겠습니다.");
    }
}
