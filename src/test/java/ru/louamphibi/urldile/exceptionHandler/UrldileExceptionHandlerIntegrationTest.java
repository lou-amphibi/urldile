package ru.louamphibi.urldile.exceptionHandler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import ru.louamphibi.urldile.error.UrldileExceptionHandler;
import ru.louamphibi.urldile.rest.ShortUrlRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortUrlRestController.class)
public class UrldileExceptionHandlerIntegrationTest {

    private MockMvc mvc;

    @MockBean
    private ShortUrlRestController shortUrlRestController;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(shortUrlRestController)
                .setControllerAdvice(new UrldileExceptionHandler())
                .build();
    }

    @Test
    public void requestOnNotAllowedMethodShouldReturnCorrectResponseWith() throws Exception {

        MvcResult result = mvc.perform(post("/api/spit/noMapping"))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Assert.assertEquals(true, responseBody.contains(HttpRequestMethodNotSupportedException.class.getName()));
    }

    @Test
    public void requestWithRuntimeExceptionShouldReturnCorrectDefaultResponseEntity() throws Exception {
        String testShortUrl = "test";

        Mockito.when(shortUrlRestController.getCustomerLinkByShortUrl(testShortUrl))
                .thenThrow(new RuntimeException(testShortUrl));

        MvcResult result = mvc.perform(get("/api/spit/{shortUrl}", testShortUrl))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Exception expectedEx = result.getResolvedException();

        Assert.assertEquals(true, responseBody.contains(RuntimeException.class.getName())
            && responseBody.contains(expectedEx.getMessage()));
    }
}
