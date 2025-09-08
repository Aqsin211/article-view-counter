package az.company.counter.model.dto.request;

import az.company.counter.exception.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class ArticleRequestDTO {
    @NotBlank(message = ValidationMessages.TITLE_CANNOT_BE_BLANK)
    @Size(max = 50, message = ValidationMessages.TITLE_CANNOT_EXCEED_50_CHARACTERS)
    String title;

    @NotBlank(message = ValidationMessages.CONTENT_CANNOT_BE_BLANK)
    @Size(min = 60, max = 300, message = ValidationMessages.CONTENT_MUST_BE_BETWEEN_60_AND_300_CHARACTERS)
    String content;

}
