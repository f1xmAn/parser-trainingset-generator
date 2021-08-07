package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SourceCollectionDocumentRepository extends MongoRepository<SourceCollectionDocument, String> {
}
