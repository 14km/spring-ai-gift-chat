package gift.controller;

import gift.dto.GiftReq;
import gift.dto.GiftRes;
import gift.service.GiftService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GiftController {
    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping("/api/gift-chat")
    public GiftRes giftChat(@Valid @RequestBody GiftReq request) {
        return giftService.chat(request);
    }
}
