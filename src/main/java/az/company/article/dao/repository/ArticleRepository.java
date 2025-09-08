package az.company.article.dao.repository;

import az.company.article.dao.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity,Long> {
    boolean existsByTitle(String title);
}
