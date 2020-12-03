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
import ru.louamphibi.urldile.error.exception.UrldileInternalException;
import ru.louamphibi.urldile.repository.CustomerLinkRepo;
import ru.louamphibi.urldile.setup.CustomerLinkTestObject;
import java.io.IOException;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
public class LinkSaveServiceIntegrationTest {

    @TestConfiguration
    static class LinkSaveServiceTestContextConfiguration {
        @Bean
        public LinkSaveService linkSaveService() {
            return new LinkSaveService();
        }
    }

    @Autowired
    private LinkSaveService linkSaveService;

    @MockBean
    private CustomerLinkRepo customerLinkRepo;

    private CustomerLink customerLink;

    private CustomerLink customerLinkFromResponseBody;

    @Before
    public void setup() {
        customerLink = CustomerLinkTestObject.getCustomerLink();
        customerLinkFromResponseBody = CustomerLinkTestObject.getCustomerLinkFromResponseBody();
    }

    @Test
    public void saveCustomerLinkShouldReturnSameObjectWithUpdatedTimeIfUrlAlreadyExist() throws IOException {
        Mockito.when(customerLinkRepo.findByUrl(customerLinkFromResponseBody.getUrl()))
                .thenReturn(customerLink);

        //For update time testing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LocalDateTime dateOfCreationBeforeUpdate = customerLink.getDateOfCreation();
        LocalDateTime dateOfDeleteBeforeUpdate = customerLink.getDateOfDelete();

        CustomerLink customerLinkFromLinkSaveService = linkSaveService.saveCustomerLink(customerLinkFromResponseBody);

        LocalDateTime dateOfCreationAfterUpdate = customerLink.getDateOfCreation();
        LocalDateTime dateOfDeleteAfterUpdate = customerLink.getDateOfDelete();

        Assert.assertEquals(customerLink, customerLinkFromLinkSaveService);
        Assert.assertEquals(true,
                dateOfCreationBeforeUpdate.isBefore(dateOfCreationAfterUpdate));
        Assert.assertEquals(true,
                dateOfDeleteBeforeUpdate.isBefore(dateOfDeleteAfterUpdate));
    }

    @Test(expected = UrldileInternalException.class)
    public void saveCustomerLinkShouldThrowUrldileInternalExceptionIfCreationOfShortUrlIsFailed() throws IOException {
        Mockito.when(customerLinkRepo.findByUrl(customerLinkFromResponseBody.getUrl())).thenReturn(null);
        Mockito.when(customerLinkRepo.findByShortUrl(Mockito.argThat( arg -> arg != null && (arg.length() >= 3
            && arg.length() <= 5)))).thenReturn(customerLink);

        linkSaveService.saveCustomerLink(customerLinkFromResponseBody);
    }

    @Test
    public void saveCustomerLinkShouldReturnCorrectCustomerLinkObjectAtSaveLink() throws IOException {
        Mockito.when(customerLinkRepo.findByUrl(customerLinkFromResponseBody.getUrl())).thenReturn(null);
        Mockito.when(customerLinkRepo.findByShortUrl(Mockito.argThat( arg -> arg != null && (arg.length() >= 3
                && arg.length() <= 5)))).thenReturn(null);

        CustomerLink savedLink = linkSaveService.saveCustomerLink(customerLinkFromResponseBody);

        Assert.assertEquals(true, savedLink.getUrl() != null);
        Assert.assertEquals(true, savedLink.getShortUrl() != null
                && savedLink.getShortUrl().length() >= 3 && savedLink.getShortUrl().length() <= 5);
        Assert.assertEquals(true, savedLink.getDateOfCreation() != null);
        Assert.assertEquals(true, savedLink.getDateOfDelete() != null);
        Assert.assertEquals(true, savedLink.getDateOfCreation().isBefore(savedLink.getDateOfDelete()));
    }
}
