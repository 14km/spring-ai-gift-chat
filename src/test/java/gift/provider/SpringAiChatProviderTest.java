package gift.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class SpringAiChatProviderTest {
    @Mock
    private ChatClient chatClient;
    @Mock
    private ChatClient.Builder builder;
    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;
    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    private SpringAiChatProvider provider;

    @BeforeEach
    void setUp() {
        given(builder.build()).willReturn(chatClient);
        given(chatClient.prompt()).willReturn(requestSpec);
        given(requestSpec.system(anyString())).willReturn(requestSpec);
        given(requestSpec.user(anyString())).willReturn(requestSpec);
        given(requestSpec.call()).willReturn(responseSpec);

        provider = new SpringAiChatProvider(builder);
    }

    @Test
    void ChatClient_정상_호출() {
        // given
        given(responseSpec.content()).willReturn("친구 생일 선물을 추천해드리겠습니다.");

        // when
        GiftChatResult result = provider.chat(new GiftChatPrompt("system", "user"));

        // then
        assertThat(result.content()).isEqualTo("친구 생일 선물을 추천해드리겠습니다.");
        assertThat(result.providerName()).isEqualTo("google");
    }

    @Test
    void ChatClient_호출_실패시_예외_그대로_전파() {
        // given
        given(responseSpec.content()).willThrow(new RuntimeException("AI provider error"));

        // when & then
        assertThatThrownBy(() -> provider.chat(new GiftChatPrompt("system", "user")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("AI provider error");
    }
}
