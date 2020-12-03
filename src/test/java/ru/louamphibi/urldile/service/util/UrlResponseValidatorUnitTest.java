package ru.louamphibi.urldile.service.util;

import org.junit.Test;
import ru.louamphibi.urldile.error.exception.UrlResponseStatusException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

public class UrlResponseValidatorUnitTest {

    @Test(expected = UrlResponseStatusException.class)
    public void checkSuccessUrlResponseCodeShouldThrowExceptionIfHttpResponseCodeNotValid() throws IOException {
        UrlResponseValidator.checkSuccessUrlResponseCode("http://google.com/incorrectLinkForTest");
    }

    @Test(expected = MalformedURLException.class)
    public void checkSuccessUrlResponseCodeShouldThrowEIfUrlIsMalformed() throws IOException {
        UrlResponseValidator.checkSuccessUrlResponseCode("MalformedUrl");
    }

    @Test(expected = UnknownHostException.class)
    public void heckSuccessUrlResponseCodeShouldThrowEIfHostIsUnknown() throws IOException {
        UrlResponseValidator.checkSuccessUrlResponseCode("http://thisHostIsLiterallyUnknown.com");
    }

    @Test
    public void checkSuccessUrlResponseCodeShouldDontThrowExceptionIfResponseCodeIsValid() throws IOException {
        UrlResponseValidator.checkSuccessUrlResponseCode("http://google.com");
    }
}
