package ru.louamphibi.urldile.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.louamphibi.urldile.entity.CustomerLink;
import ru.louamphibi.urldile.error.exception.CustomerLinkNotFoundException;
import ru.louamphibi.urldile.service.LinkSaveService;
import ru.louamphibi.urldile.service.ShortUrlService;
import ru.louamphibi.urldile.setup.CustomerLinkTestObject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.louamphibi.urldile.setup.LinkToJsonStringConverter.asJsonString;

@RunWith(SpringRunner.class)
@WebMvcTest(DevouringLinkRestController.class)
public class DevouringLinkRestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LinkSaveService linkSaveService;

    @MockBean
    private ShortUrlService shortUrlService;

    private CustomerLink customerLinkFromResponseBody;

    private CustomerLink savedCustomerLink;

    @Before
    public void setup() throws IOException {
        customerLinkFromResponseBody = CustomerLinkTestObject.getCustomerLinkFromResponseBody();
        savedCustomerLink = CustomerLinkTestObject.getCustomerLink();

        given(linkSaveService.saveCustomerLink(customerLinkFromResponseBody)).willReturn(savedCustomerLink);
        given(shortUrlService.findCustomerLinkByShortUrl(savedCustomerLink.getShortUrl())).willReturn(savedCustomerLink);
    }

    @Test
    public void saveLinkShouldReturnCorrectJsonAndStatusCodeIfSuccess() throws Exception {

        mvc.perform(post("/api/eatLink/").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerLinkFromResponseBody)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(savedCustomerLink)));
    }

    @Test
    public void saveLinkShouldThrowMethodArgumentNotValidResponseAndCorrectMessageIfUrlEqualsNull() throws Exception {
        customerLinkFromResponseBody.setUrl(null);

        MvcResult result = mvc.perform(post("/api/eatLink/").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerLinkFromResponseBody)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(true, responseBody.contains(MethodArgumentNotValidException.class.getName()));
        Assert.assertEquals(true, responseBody.contains(CustomerLink.class.getDeclaredField("url")
            .getAnnotation(NotNull.class).message()));
    }

    @Test
    public void saveLinkShouldThrowMethodArgumentNotValidResponseAndCorrectMessageIfUrlNotMathWithPattern() throws Exception {
        customerLinkFromResponseBody.setUrl("incorrect url");

        MvcResult result = mvc.perform(post("/api/eatLink/").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerLinkFromResponseBody)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(true, responseBody.contains(MethodArgumentNotValidException.class.getName()));
        Assert.assertEquals(true, responseBody.contains(CustomerLink.class.getDeclaredField("url")
                .getAnnotation(Pattern.class).message()));

    }

    @Test
    public void saveLinkShouldThrowJsonParseResponseIfMalformedJsonRequest() throws Exception {
        MvcResult result = mvc.perform(post("/api/eatLink/").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerLinkFromResponseBody).replace('{', '(')))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(true, responseBody.contains(HttpMessageNotReadableException.class.getName()));
    }

    @Test
    public void redirectToLinkByShortLinkShouldRedirectToUrlInCustomerLink() throws Exception {
        mvc.perform(get("/api/eatLink/{shortUrl}",  savedCustomerLink.getShortUrl()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(savedCustomerLink.getUrl()));
    }

    @Test
    public void redirectToLinkByShortLinkShouldReturnCorrectResponseIfShortUrlNotFound() throws Exception {
        given(shortUrlService.findCustomerLinkByShortUrl(savedCustomerLink.getShortUrl()))
                .willThrow(new CustomerLinkNotFoundException("exception message", HttpStatus.NOT_FOUND));

        MvcResult result = mvc.perform(get("/api/eatLink/{shortUrl}",  savedCustomerLink.getShortUrl()))
                .andExpect(status().isNotFound())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(true, responseBody.contains(CustomerLinkNotFoundException.class.getName()));
    }
}
