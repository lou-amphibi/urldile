package ru.louamphibi.urldile.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Document(collection = "CustomerLink")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerLink {

    @Id
    private String id;

    @Field("Url")
    @NotNull(message = "Url field cannot be empty")
    @Pattern(regexp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)",
            message = "Input the correct link like - (http/https)://www.mylink.com")
    @Indexed(unique = true)
    private String url;


    @Field("Short_url")
    @Indexed(unique = true)
    private String shortUrl;

    @Field("Date_of_creation")
    @Indexed(expireAfter = "5d")
    private LocalDateTime dateOfCreation;

    @Field("Delete_date")
    private LocalDateTime dateOfDelete;
}
