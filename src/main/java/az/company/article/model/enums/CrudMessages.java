package az.company.article.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrudMessages {
    VIEWS("Views: %d"),
    VIEW_COUNT_INCREMENTED("Article view count incremented"),
    VIEW_COUNT_RESET("Article view count has been reset"),
    ARTICLE_UPDATED("Article updated"),
    ARTICLE_DELETED("Article deleted"),
    ARTICLE_CREATED("Article created with id: %d");
    private final String message;
}
