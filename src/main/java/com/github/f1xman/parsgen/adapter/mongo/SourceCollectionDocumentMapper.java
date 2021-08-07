package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;

public interface SourceCollectionDocumentMapper {
    SourceCollection map(SourceCollectionDocument document);
}
