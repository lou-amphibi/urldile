package ru.louamphibi.urldile.service.util;

import org.springframework.http.HttpStatus;
import ru.louamphibi.urldile.error.exception.UrlResponseStatusException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlResponseValidator {
    public static void checkSuccessUrlResponseCode(String userUrl) throws IOException {
        URL url = new URL(userUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.connect();

        if (checkResponseCode(openConnection.getResponseCode()))  {
            openConnection.disconnect();
            return;
        }

        openConnection.disconnect();

        throw new UrlResponseStatusException("The resource under your link is not available at the moment or does not exist",
                openConnection.getResponseCode());
    }

    private static boolean checkResponseCode(int responseCode) {
        return responseCode == HttpStatus.OK.value() ||
                responseCode == HttpStatus.FOUND.value() ||
                responseCode == HttpStatus.PERMANENT_REDIRECT.value() ||
                responseCode == HttpStatus.TEMPORARY_REDIRECT.value() ||
                responseCode == HttpStatus.MOVED_PERMANENTLY.value() ||
                responseCode == HttpStatus.SEE_OTHER.value() ||
                responseCode == HttpStatus.RESET_CONTENT.value() ||
                responseCode == HttpStatus.PARTIAL_CONTENT.value() ||
                responseCode == HttpStatus.NOT_MODIFIED.value() ||
                responseCode == HttpStatus.NON_AUTHORITATIVE_INFORMATION.value() ||
                responseCode == HttpStatus.MULTIPLE_CHOICES.value() ||
                responseCode == HttpStatus.MULTI_STATUS.value() ||
                responseCode == HttpStatus.IM_USED.value() ||
                responseCode == HttpStatus.CREATED.value() ||
                responseCode == HttpStatus.ALREADY_REPORTED.value();
    }
}
