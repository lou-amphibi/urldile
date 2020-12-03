package ru.louamphibi.urldile.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.setup.CustomerLinkTestObject;

@RunWith(SpringRunner.class)
@DataMongoTest
public class CustomerLinkRepoIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CustomerLinkRepo customerLinkRepo;

    private CustomerLink customerLink;

    @Before
    public void setup() {
        customerLink = CustomerLinkTestObject.getCustomerLink();
        mongoTemplate.save(customerLink);
    }

    @Test
    public void findByShortUrlShouldReturnCorrectCustomerLink(){
        CustomerLink linkFromDb = customerLinkRepo.findByShortUrl(customerLink.getShortUrl());

        Assert.assertEquals(customerLink, linkFromDb);
    }

    @Test
    public void findByUrlShouldReturnCorrectCustomerLink() {
        CustomerLink linkFromDb = customerLinkRepo.findByUrl(customerLink.getUrl());

        Assert.assertEquals(customerLink, linkFromDb);
    }
}
