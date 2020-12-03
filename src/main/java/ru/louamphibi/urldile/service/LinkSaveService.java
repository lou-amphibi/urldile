package ru.louamphibi.urldile.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.repository.CustomerLinkRepo;
import ru.louamphibi.urldile.error.exception.UrldileInternalException;
import ru.louamphibi.urldile.service.util.UrlResponseValidator;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class LinkSaveService {

    @Autowired
    private CustomerLinkRepo customerLinkRepo;

    public CustomerLink saveCustomerLink(CustomerLink customerLink) throws IOException {

        UrlResponseValidator.checkSuccessUrlResponseCode(customerLink.getUrl());

        CustomerLink linkFromDb = customerLinkRepo.findByUrl(customerLink.getUrl());

        if (linkFromDb != null) {
            setLinkLifeTime(linkFromDb);
            customerLinkRepo.save(linkFromDb);
            return linkFromDb;
        }

        setLinkLifeTime(customerLink);
        customerLink.setShortUrl(makeShortUrl());
        customerLinkRepo.save(customerLink);

        return customerLink;
    }

    private void setLinkLifeTime(CustomerLink customerLink) {
        long linkLifeTime = 5L;
        customerLink.setDateOfCreation(LocalDateTime.now().withNano(0));
        customerLink.setDateOfDelete(customerLink.getDateOfCreation().plusDays(linkLifeTime).withNano(0));
    }

    private String makeShortUrl() {
        int attempt = 5;
        String shortUrl;
        int shortUrlLength;

        while(attempt > 0) {
            shortUrlLength = 3 + (int) (Math.random() * 3);
            shortUrl = RandomStringUtils.random(shortUrlLength, true, true);
            if (customerLinkRepo.findByShortUrl(shortUrl) == null)
                return shortUrl;
            attempt--;
        }

        throw new UrldileInternalException("failed to create short link", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
