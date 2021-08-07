package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class SourceCollectionDocumentMapperTest {

    static final String ID = "id";
    static final String BASE_URL = "baseUrl";
    static final List<URL> URLS = List.of(url());

    @Test
    void mapsSourceCollectionDocumentToSourceCollection() {
        SourceCollection expectedSourceCollection = new SourceCollection(ID, BASE_URL, URLS);
        SourceCollectionDocumentMapper mapper = SourceCollectionDocumentMapper.INSTANCE;
        SourceCollection actualSourceCollection = mapper.map(new SourceCollectionDocument(ID, BASE_URL, URLS));

        then(actualSourceCollection).isEqualTo(expectedSourceCollection);
    }

    @SneakyThrows
    private static URL url() {
        return new URL("https://foo.bar/baz");
    }
}