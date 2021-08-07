package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SourceCollectionDocumentMapper {
    SourceCollectionDocumentMapper INSTANCE = Mappers.getMapper(SourceCollectionDocumentMapper.class);

    SourceCollection map(SourceCollectionDocument document);
}
