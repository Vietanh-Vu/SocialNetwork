package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.infrastructure.entity.Suggestion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SuggestionMapper {
    SuggestionMapper INSTANCE = Mappers.getMapper(SuggestionMapper.class);

    SuggestionDomain toSuggestionDomain(Suggestion suggestion);
}
