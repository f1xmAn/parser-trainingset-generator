package com.github.f1xman.parsgen.adapter.mongo.document;

import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.net.URL;
import java.util.List;

@Document(collection = "sourceCollections")
@Value
public class SourceCollectionDocument {

    @MongoId
    String id;
    String baseUrl;
    List<URL> urls;

    public static SourceCollectionDocument of(String baseUrl, List<URL> urls) {
        return new SourceCollectionDocument(null, baseUrl, urls);
    }

}
