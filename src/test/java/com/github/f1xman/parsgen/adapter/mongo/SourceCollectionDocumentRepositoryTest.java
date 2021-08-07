package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@Testcontainers
@DataMongoTest
public class SourceCollectionDocumentRepositoryTest {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:focal"));

    @Autowired
    SourceCollectionDocumentRepository repository;

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void findsSourceCollectionDocuments() {
        SourceCollectionDocument expected = repository.save(SourceCollectionDocument.of("baseUrl", List.of(url())));

        Page<SourceCollectionDocument> page = repository.findAll(PageRequest.of(0, 1));

        then(page.getContent()).isEqualTo(List.of(expected));
    }

    @SneakyThrows
    private URL url() {
        return new URL("https://foo.bar/baz");
    }
}
