package az.company.counter.service;

import az.company.counter.dao.repository.ArticleRepository;
import az.company.counter.model.dto.request.ArticleRequestDTO;
import az.company.counter.model.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    public Long createArticle(ArticleRequestDTO articleRequestDTO) {

    }
}
