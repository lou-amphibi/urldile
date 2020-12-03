package ru.louamphibi.urldile.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.service.LinkSaveService;
import ru.louamphibi.urldile.service.ShortUrlService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class DevouringLinkRestController {

    @Autowired
    private LinkSaveService linkSaveService;

    @Autowired
    private ShortUrlService shortUrlService;

    @PostMapping("/eatLink")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public CustomerLink saveLink(@RequestBody @Valid CustomerLink customerLink) throws Exception {
        return linkSaveService.saveCustomerLink(customerLink);
    }

    @GetMapping("/eatLink/{shortUrl}")
    public void redirectToLinkByShortLink(HttpServletResponse response, @PathVariable String shortUrl) throws IOException {
        response.sendRedirect(shortUrlService.findCustomerLinkByShortUrl(shortUrl).getUrl());
    }
}
