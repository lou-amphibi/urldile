package ru.louamphibi.urldile.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.louamphibi.urldile.entity.CustomerLink;

@Repository
public interface CustomerLinkRepo extends MongoRepository<CustomerLink, String> {

    CustomerLink findByUrl(String url);

    CustomerLink findByShortUrl(String shortUrl);
}
