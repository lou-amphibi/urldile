package ru.louamphibi.urldile.setup;

import ru.louamphibi.urldile.entity.CustomerLink;
import java.time.LocalDateTime;

public class CustomerLinkTestObject {

    public static CustomerLink getCustomerLink() {
        return  new CustomerLink(
                "id",
                "https://google.com",
                "GocM",
                LocalDateTime.now().withNano(0),
                LocalDateTime.now().plusDays(2).withNano(0)
        );
    }

    public static CustomerLink getCustomerLinkFromResponseBody() {
        CustomerLink customerLinkFromResponseBody = new CustomerLink();
        customerLinkFromResponseBody.setUrl("https://google.com");
        return customerLinkFromResponseBody;
    }
}
