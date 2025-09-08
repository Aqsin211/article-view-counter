package az.company.article.service;

import az.company.article.dao.entity.ArticleEntity;
import az.company.article.dao.repository.ArticleRepository;
import az.company.article.exception.AlreadyExistsException;
import az.company.article.exception.NotFoundException;
import az.company.article.model.dto.request.ArticleRequestDTO;
import az.company.article.model.dto.response.ArticleResponseDTO;
import az.company.article.model.enums.ErrorMessages;
import az.company.article.model.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class ArticleService {

    @Value("${article.view.db-persist-threshold}")
    private long dbPersistThreshold;

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CacheManager cacheManager;

    public ArticleService(ArticleRepository articleRepository,
                          ArticleMapper articleMapper,
                          CacheManager cacheManager) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.cacheManager = cacheManager;
    }

    public Long createArticle(ArticleRequestDTO dto) {
        if (articleRepository.existsByTitle(dto.getTitle())) {
            throw new AlreadyExistsException(ErrorMessages.ARTICLE_ALREADY_EXISTS.getMessage());
        }
        return articleRepository.save(articleMapper.toEntity(dto)).getId();
    }

    public ArticleResponseDTO getArticleById(Long id) {
        ArticleResponseDTO response = articleMapper.toDTO(getArticleEntityById(id));
        response.setViewCount(incrementViews(id));
        return response;
    }

    public List<ArticleResponseDTO> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(entity -> {
                    ArticleResponseDTO dto = articleMapper.toDTO(entity);
                    Cache cache = cacheManager.getCache("articleViews");
                    if (cache != null) {
                        Long cachedCount = cache.get(entity.getId(), Long.class);
                        if (cachedCount != null) {
                            dto.setViewCount(cachedCount);
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void updateArticle(Long id, ArticleRequestDTO dto) {
        ArticleEntity entity = getArticleEntityById(id);

        if (!entity.getTitle().equals(dto.getTitle()) && !articleRepository.existsByTitle(dto.getTitle())) {
            entity.setTitle(dto.getTitle());
        }
        entity.setContent(dto.getContent());
        articleRepository.save(entity);

        Cache cache = cacheManager.getCache("articleViews");
        if (cache != null) {
            Long cachedCount = cache.get(id, Long.class);
            if (cachedCount != null) {
                entity.setViewCount(cachedCount);
            }
            cache.put(id, entity.getViewCount());
        }
    }

    public void deleteArticleById(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new NotFoundException(format(ErrorMessages.ARTICLE_NOT_FOUND.getMessage(), id));
        }
        articleRepository.deleteById(id);

        Cache cache = cacheManager.getCache("articleViews");
        if (cache != null) {
            cache.evict(id);
        }
    }

    public Long getViews(Long articleId) {
        Cache cache = cacheManager.getCache("articleViews");
        if (cache != null) {
            Long cachedCount = cache.get(articleId, Long.class);
            if (cachedCount != null) {
                return cachedCount;
            }
        }

        long dbCount = getArticleEntityById(articleId).getViewCount();
        if (cache != null) {
            cache.put(articleId, dbCount);
        }
        return dbCount;
    }

    public Long incrementViews(Long articleId) {
        ArticleEntity entity = getArticleEntityById(articleId);

        Cache cache = cacheManager.getCache("articleViews");
        long newCount = entity.getViewCount();

        if (cache != null) {
            Long cachedCount = cache.get(articleId, Long.class);
            newCount = (cachedCount != null) ? cachedCount + 1 : newCount + 1;
            cache.put(articleId, newCount);
        } else {
            newCount++;
        }

        if (newCount % dbPersistThreshold == 0) {
            entity.setViewCount(newCount);
            articleRepository.save(entity);
        }

        return newCount;
    }

    public void resetViews(Long articleId) {
        ArticleEntity entity = getArticleEntityById(articleId);
        entity.setViewCount(0L);
        articleRepository.save(entity);

        Cache cache = cacheManager.getCache("articleViews");
        if (cache != null) {
            cache.evict(articleId);
        }
    }

    private ArticleEntity getArticleEntityById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                                format(ErrorMessages.ARTICLE_NOT_FOUND.getMessage(), id)
                        )
                );
    }
}
