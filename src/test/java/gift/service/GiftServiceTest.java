package gift.service;

import gift.dto.GiftReq;
import gift.provider.GiftChatPrompt;
import gift.provider.GiftChatProvider;
import gift.provider.GiftChatResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class GiftServiceTest {
    @Mock
    private GiftChatProvider provider;

    private GiftService giftService;

    @BeforeEach
    void setUp() {
        giftService = new GiftService(provider);
    }

    @Test
    void 정상적인_채팅() {
        // given
        String message = "친구의 생일 선물을 추천해주세요";
        given(provider.chat(any(GiftChatPrompt.class)))
                .willReturn(new GiftChatResult("친구 생일 선물을 추천해드리겠습니다.", "test-provider"));

        // when
        var res = giftService.chat(new GiftReq(message, "sessionId-1"));

        // then
        assertThat(res.requestId()).isNotBlank();
        assertThat(UUID.fromString(res.requestId())).isNotNull();
        assertThat(res.message()).isEqualTo("친구 생일 선물을 추천해드리겠습니다.");
        assertThat(res.durationMs()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void Provider_호출_실패() {
        // given
        given(provider.chat(any(GiftChatPrompt.class)))
                .willThrow(new RuntimeException("AI provider error"));

        // when & then
        assertThatThrownBy(() -> giftService.chat(new GiftReq("선물 추천", "sessionId-1")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("선물 추천에 실패했습니다")
                .hasCauseInstanceOf(RuntimeException.class);
    }
}
