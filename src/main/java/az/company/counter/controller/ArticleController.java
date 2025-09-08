package az.company.counter.controller;

import az.company.counter.model.dto.request.ArticleRequestDTO;
import az.company.counter.model.dto.response.ArticleResponseDTO;
import az.company.counter.model.enums.CrudMessages;
import az.company.counter.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public String create(@Valid @RequestBody ArticleRequestDTO articleRequestDTO) {
        Long id = articleService.createArticle(articleRequestDTO);
        return format(CrudMessages.ARTICLE_CREATED.getMessage(), id);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> getAll() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getArticleById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @Valid @RequestBody ArticleRequestDTO articleRequestDTO) {
        articleService.updateArticle(id, articleRequestDTO);
        return ResponseEntity.ok(CrudMessages.ARTICLE_UPDATED.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        articleService.deleteArticleById(id);
        return ResponseEntity.ok(CrudMessages.ARTICLE_DELETED.getMessage());
    }

    @GetMapping("/{id}/views")
    public ResponseEntity<String> getViews(@PathVariable Long id) {
        Long viewCount = articleService.getViews(id);
        return ResponseEntity.ok(format(CrudMessages.VIEWS.getMessage(), viewCount));
    }

    @PostMapping("/{id}/reset")
    public ResponseEntity<String> reset(@PathVariable Long id) {
        articleService.resetViews(id);
        return ResponseEntity.ok(CrudMessages.VIEW_COUNT_RESET.getMessage());
    }

    @PostMapping("/{id}/increment")
    public ResponseEntity<String> increment(@PathVariable Long id) {
        articleService.incrementViews(id);
        return ResponseEntity.ok(CrudMessages.VIEW_COUNT_INCREMENTED.getMessage());
    }

}
