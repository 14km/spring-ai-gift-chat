package gift.provider;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "gift.llm.provider", havingValue = "google")
public class SpringAiChatProvider implements GiftChatProvider {
    private static final String PROVIDER_NAME = "google";
    private final ChatClient client;

    public SpringAiChatProvider(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    @Override
    public GiftChatResult chat(GiftChatPrompt prompt) {
        String content = client
                .prompt()
                .system(prompt.systemPrompt())
                .user(prompt.userMessage())
                .call()
                .content();
        return new GiftChatResult(content, PROVIDER_NAME);
    }
}
