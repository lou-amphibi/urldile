package ru.louamphibi.urldile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.service.ShortUrlService;

@RestController
@RequestMapping("/api/spit")
public class ShortUrlRestController {

    @Autowired
    private ShortUrlService shortUrlService;

    @GetMapping("{shortUrl}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public CustomerLink getCustomerLinkByShortUrl(@PathVariable String shortUrl) {
        return shortUrlService.findCustomerLinkByShortUrl(shortUrl);
    }
}
