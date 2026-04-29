package gift.controller;

import gift.service.TestService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {
    private final ChatClient client;
    private final TestService testService;

    public JokeController(ChatClient.Builder builder,  TestService actorService) {
        this.client = builder.build();
        this.testService = actorService;
    }

    @GetMapping("/joke")
    public String joke(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return client
                .prompt()
                .system("당신은 시인 입니다.")
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/actor")
    public String findActor(@RequestParam(value = "message") String message) {
        return testService.findActor(message);
    }

    @GetMapping("/today")
    public String today(@RequestParam(value = "day") String day) {
        return testService.today(day);
    }

}
