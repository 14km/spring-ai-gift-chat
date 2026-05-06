package gift.provider;

import org.springaicommunity.claude.agent.sdk.ClaudeClient;
import org.springaicommunity.claude.agent.sdk.ClaudeSyncClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@ConditionalOnProperty(name = "gift.llm.provider", havingValue = "claude-code")
public class ClaudeCodeChatProvider implements GiftChatProvider {
    private static final String PROVIDER_NAME = "claude-code";
    private final Path workingDirectory;

    public ClaudeCodeChatProvider() {
        try {
            this.workingDirectory = Files.createTempDirectory("gift-claude-");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create working directory for claude-code SDK", e);
        }
    }

    @Override
    public GiftChatResult chat(GiftChatPrompt prompt) {
        try (ClaudeSyncClient client = ClaudeClient.sync()
                .workingDirectory(workingDirectory)
                .systemPrompt(prompt.systemPrompt())
                .build()) {
            client.connect();
            String content = client.queryText(prompt.userMessage());
            return new GiftChatResult(content, PROVIDER_NAME);
        }
    }
}
