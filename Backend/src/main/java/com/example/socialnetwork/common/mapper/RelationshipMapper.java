package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    RelationshipMapper INSTANCE = Mappers.getMapper(RelationshipMapper.class);

    RelationshipDomain toRelationshipDomain(Relationship relationship);

    List<RelationshipDomain> toRelationshipDomain(List<Relationship> relationships);
}
