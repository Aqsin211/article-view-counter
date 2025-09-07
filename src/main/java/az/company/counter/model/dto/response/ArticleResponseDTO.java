package az.company.counter.model.dto.response;

import java.time.LocalDateTime;

public class ArticleResponseDTO {
    Long id;
    String title;
    String content;
    Long viewCount;
    LocalDateTime createdAt;
}
