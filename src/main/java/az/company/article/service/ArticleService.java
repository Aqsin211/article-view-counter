package az.company.article.service;

import az.company.article.dao.entity.ArticleEntity;
import az.company.article.dao.repository.ArticleRepository;
import az.company.article.exception.AlreadyExistsException;
import az.company.article.exception.NotFoundException;
import az.company.article.model.dto.request.ArticleRequestDTO;
import az.company.article.model.dto.response.ArticleResponseDTO;
import az.company.article.model.enums.ErrorMessages;
import az.company.article.model.mapper.ArticleMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    public Long createArticle(ArticleRequestDTO dto) {
        if (articleRepository.existsByTitle(dto.getTitle())) {
            throw new AlreadyExistsException(ErrorMessages.ARTICLE_ALREADY_EXISTS.getMessage());
        }
        return articleRepository.save(articleMapper.toEntity(dto)).getId();
    }

    public ArticleResponseDTO getArticleById(Long id) {
        ArticleEntity entity = getArticleEntityById(id);
        incrementViews(id);
        return articleMapper.toResponseDTO(entity);
    }

    public List<ArticleResponseDTO> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateArticle(Long id, ArticleRequestDTO dto) {
        ArticleEntity entity = getArticleEntityById(id);

        if (!entity.getTitle().equals(dto.getTitle()) && !articleRepository.existsByTitle(dto.getTitle())) {
            entity.setTitle(dto.getTitle());
        }
        entity.setContent(dto.getContent());
        articleRepository.save(entity);
    }

    public void deleteArticleById(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new NotFoundException(format(ErrorMessages.ARTICLE_NOT_FOUND.getMessage(), id));
        }
        articleRepository.deleteById(id);
    }

    @Cacheable(value = "articleViews", key = "#articleId")
    public Long getViews(Long articleId) {
        return getArticleEntityById(articleId).getViewCount();
    }

    @CachePut(value = "articleViews", key = "#articleId")
    public Long incrementViews(Long articleId) {
        ArticleEntity entity = getArticleEntityById(articleId);
        entity.setViewCount(entity.getViewCount() + 1);
        articleRepository.save(entity);
        return entity.getViewCount();
    }

    @CacheEvict(value = "articleViews", key = "#articleId")
    public void resetViews(Long articleId) {
        ArticleEntity entity = getArticleEntityById(articleId);
        entity.setViewCount(0L);
        articleRepository.save(entity);
    }

    private ArticleEntity getArticleEntityById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                                format(ErrorMessages.ARTICLE_NOT_FOUND.getMessage(), id)
                        )
                );
    }
}
