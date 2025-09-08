package az.company.article.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    RESOURCE_NOT_FOUND("Resource Not Found"),
    CONFLICT("Conflict"),
    VALIDATION_FAILED("Validation Failed"),
    ARTICLE_NOT_FOUND("Article not found with id: %d"),
    ARTICLE_ALREADY_EXISTS("Article by this title already exists");
    private final String message;
}
