package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MongoSourceCollectionRepositoryTest {

    static final int PAGE = 0;
    static final int SIZE = 1;
    static final String BASE_URL = "baseUrl";
    static final List<URL> URLS = List.of();
    @Mock
    SourceCollectionDocumentRepository documentRepository;
    @Mock
    SourceCollectionDocumentMapper mapper;

    @Test
    void findsSourceCollectionsViaDocumentRepository() {
        MongoSourceCollectionRepository repository = new MongoSourceCollectionRepository(documentRepository, mapper);
        SourceCollectionDocument document = SourceCollectionDocument.of(BASE_URL, URLS);
        given(documentRepository.findAll(PageRequest.of(PAGE, SIZE))).willReturn(new PageImpl<>(List.of(document)));
        SourceCollection expectedSourceCollection = SourceCollection.of(BASE_URL, URLS);
        given(mapper.map(document)).willReturn(expectedSourceCollection);

        List<SourceCollection> actualSourceCollections = repository.findAll(PAGE, SIZE);

        then(actualSourceCollections).isEqualTo(List.of(expectedSourceCollection));
    }
}
