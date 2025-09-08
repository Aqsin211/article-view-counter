package az.company.article.model.mapper;

import az.company.article.dao.entity.ArticleEntity;
import az.company.article.model.dto.request.ArticleRequestDTO;
import az.company.article.model.dto.response.ArticleResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "viewCount", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    ArticleEntity toEntity(ArticleRequestDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "viewCount", source = "viewCount")
    @Mapping(target = "createdAt", source = "createdAt")
    ArticleResponseDTO toResponseDTO(ArticleEntity entity);
}
