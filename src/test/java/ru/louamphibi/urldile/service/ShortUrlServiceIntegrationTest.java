package ru.louamphibi.urldile.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.error.exception.CustomerLinkNotFoundException;
import ru.louamphibi.urldile.repository.CustomerLinkRepo;
import ru.louamphibi.urldile.setup.CustomerLinkTestObject;

@RunWith(SpringRunner.class)
public class ShortUrlServiceIntegrationTest {

    @TestConfiguration
    static class ShortUrlServiceTestContextConfiguration {
        @Bean
        public ShortUrlService shortUrlService() {
            return new ShortUrlService();
        }
    }

    @Autowired
    private ShortUrlService shortUrlService;

    @MockBean
    private CustomerLinkRepo customerLinkRepo;

    private CustomerLink customerLink;

    @Before
    public void setup() {
        customerLink = CustomerLinkTestObject.getCustomerLink();
    }

    @Test
    public void findCustomerLinkByShortUrlShouldThrowCustomerLinkNotFoundExceptionIfCustomerLinkNotFound() {
        Mockito.when(customerLinkRepo.findByShortUrl(customerLink.getShortUrl())).thenReturn(null);

        Exception expectedEx = Assert.assertThrows(CustomerLinkNotFoundException.class, () ->
                shortUrlService.findCustomerLinkByShortUrl(customerLink.getShortUrl()));

        Assert.assertEquals(expectedEx.getMessage(), "Long link not found for short url - "
                + customerLink.getShortUrl().trim());
    }

    @Test
    public void findCustomerLinkByShortUrlShouldThrowCustomerLinkNotFoundExceptionIfShortUrlLengthEqualsZero() {
        Mockito.when(customerLinkRepo.findByShortUrl(customerLink.getShortUrl())).thenReturn(null);

        Exception expectedEx = Assert.assertThrows(CustomerLinkNotFoundException.class, () ->
            shortUrlService.findCustomerLinkByShortUrl(" "));

        Assert.assertEquals(expectedEx.getMessage(), "Please input a short url");
    }

    @Test
    public void findCustomerLinkByShortUrlShouldReturnCorrectCustomerLinkByShortUrl()  {
        Mockito.when(customerLinkRepo.findByShortUrl(customerLink.getShortUrl())).thenReturn(customerLink);

        CustomerLink linkFromDb = shortUrlService.findCustomerLinkByShortUrl(customerLink.getShortUrl());

        Assert.assertEquals(customerLink, linkFromDb);
    }
}
