package ru.louamphibi.urldile.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.error.exception.CustomerLinkNotFoundException;
import ru.louamphibi.urldile.service.ShortUrlService;
import ru.louamphibi.urldile.setup.CustomerLinkTestObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.louamphibi.urldile.setup.LinkToJsonStringConverter.asJsonString;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortUrlRestController.class)
public class ShortUrlRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShortUrlService shortUrlService;

    private CustomerLink customerLink;

    @Before
    public void setup() {
        customerLink = CustomerLinkTestObject.getCustomerLink();
        given(shortUrlService.findCustomerLinkByShortUrl(customerLink.getShortUrl())).willReturn(customerLink);
    }

    @Test
    public void getCustomerLinkByShortUrlShouldReturnCorrectJsonIfCustomerLinkExist() throws Exception {
        MvcResult result = mvc.perform(get("/api/spit/{shortUrl}", customerLink.getShortUrl()))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(responseBody, asJsonString(customerLink));
    }

    @Test
    public void getCustomerLinkByShortUrlShouldReturnCustomerLinkNotFoundExceptionIfCustomerLinkNotFound() throws Exception {
        given(shortUrlService.findCustomerLinkByShortUrl(customerLink.getShortUrl())).willThrow(
                new CustomerLinkNotFoundException("exception message", HttpStatus.NOT_FOUND)
        );

        MvcResult result = mvc.perform(get("/api/spit/{shortUrl}", customerLink.getShortUrl()))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(true, responseBody.contains(CustomerLinkNotFoundException.class.getName()));
    }
}
