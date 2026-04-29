package gift.service;

import gift.dto.test.ActorFilms;
import gift.tool.Calculate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService {

    private final String jsonSchema;
    private final ChatClient client;

    public TestService(ChatClient.Builder builder) {
        this.jsonSchema = getFormat();
        this.client = builder.build();
    }

    public String getFormat() {
        String template = """
                Your response should be in JSON format.
                Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.
                Do not include markdown code blocks in your response.
                Remove the ```json markdown from the output.
                Here is the JSON Schema instance your output must adhere to:
                ```%s```
                """;
        return String.format(template, this.jsonSchema);
    }

    public String findActor(String actor) {
        var converter = new BeanOutputConverter<>(ActorFilms.class);
        var format = converter.getFormat();
        var user = """
                Generate the filmography of 5 movies for {actor}.
                {format}
                """;

        var template = new PromptTemplate(user);
        var prompt = template.create(Map.of("actor", actor, "format", format));
        var text = client.prompt(prompt)
                .call()
                .content();

        ActorFilms convert = converter.convert(text);

        return convert.toString();
    }

    public String today(String days) {
        var template = new PromptTemplate("Tell me the date {days} from today.");
        var prompt = template.render(Map.of("days", days));

        return client.prompt(prompt)
                .tools(new Calculate())
                .call()
                .content();
    }
}
