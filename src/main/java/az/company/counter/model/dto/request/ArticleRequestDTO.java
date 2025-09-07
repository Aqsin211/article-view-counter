package az.company.counter.model.dto.request;

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
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Title cannot exceed 20 characters")
    String title;

    @NotBlank(message = "Content cannot be blank")
    @Size(min = 60, message = "Content must be at least 60 characters long")
    String content;

}
