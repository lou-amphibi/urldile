package ru.louamphibi.urldile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.error.exception.CustomerLinkNotFoundException;
import ru.louamphibi.urldile.repository.CustomerLinkRepo;

@Service
public class ShortUrlService {

    @Autowired
    private CustomerLinkRepo customerLinkRepo;

    public CustomerLink findCustomerLinkByShortUrl(String shortUrl) {

        CustomerLink linkFromDb = customerLinkRepo.findByShortUrl(shortUrl.trim());

        if (linkFromDb != null) {
            return linkFromDb;
        }

        throw new CustomerLinkNotFoundException(shortUrl.trim().length() == 0 ? "Please input a short url"
                : "Long link not found for short url - " + shortUrl.trim(), HttpStatus.NOT_FOUND);
    }
}
