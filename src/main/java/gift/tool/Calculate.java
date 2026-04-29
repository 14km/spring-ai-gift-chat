package gift.tool;

import gift.dto.test.AddDaysRequest;
import gift.dto.test.DateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class Calculate {
    /* Logger */
    private static final Logger log = LoggerFactory.getLogger(Calculate.class);

    @Tool(description = "Calculate a date after adding days from today")
    public DateResponse addDaysFromToday(AddDaysRequest request) {
        log.info("addDaysFromToday called");
        var result = LocalDate.now().plusDays(request.days());
        return new DateResponse(result.toString());
    }
}
