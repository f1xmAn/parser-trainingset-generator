package com.github.f1xman.parsgen;

import com.github.f1xman.parsgen.adapter.mongo.SourceCollectionDocumentRepository;
import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import com.github.f1xman.parsgen.core.DatasetGenerator;
import com.github.f1xman.parsgen.core.analyze.AnalysisStrategy;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockserver.model.HttpRequest.request;

@SpringBootTest(properties = "print.csv.output-directory=${java.io.tmpdir}/parsgen")
@Import(ParserTrainingsetGeneratorApplicationTests.DumbAnalysisStrategy.class)
@Testcontainers
class ParserTrainingsetGeneratorApplicationTests {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:focal"));
    static final DockerImageName MOCKSERVER_IMAGE = DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2");
    @Container
    static final MockServerContainer mockServer = new MockServerContainer(MOCKSERVER_IMAGE);
    static final String BASE_URL = "http://foo.bar";
    static final String PATH = "/baz";
    @Autowired
    SourceCollectionDocumentRepository repository;
    @Autowired
    DatasetGenerator generator;
    @Value("classpath:html/fake.html")
    Resource fakeHtmlResource;
    @Value("classpath:csv/feature-value.csv")
    Resource expectedCsvResource;
    @Value("${print.csv.output-directory}")
    Path outputDirectory;

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @SneakyThrows
    @BeforeEach
    void setUp() {
        if (Files.exists(outputDirectory)) {
            Files.walk(outputDirectory)
                    .sorted(reverseOrder())
                    .forEach(this::deletePath);
        }
        Files.createDirectory(outputDirectory);

        SourceCollectionDocument document = SourceCollectionDocument.of(
                BASE_URL,
                List.of(URI.create(mockServer.getEndpoint() + "/" + PATH).toURL())
        );
        repository.save(document);

        String html = Files.readString(fakeHtmlResource.getFile().toPath());
        MockServerClient client = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        client.when(request().withPath(PATH)).respond(HttpResponse.response().withBody(html));
    }

    @SneakyThrows
    private void deletePath(Path path) {
        Files.deleteIfExists(path);
    }

    @Test
    @SneakyThrows
    void contextLoadsAndFeaturesFileProduced() {
        String expectedCsv = Files.readString(expectedCsvResource.getFile().toPath());

        generator.generate();
        Path actualFile = Files.walk(outputDirectory)
                .filter(Files::isRegularFile)
                .findAny()
                .orElseThrow();
        String actualCsv = Files.readString(actualFile);

        then(actualCsv).isEqualTo(expectedCsv);
    }

    static class DumbAnalysisStrategy implements AnalysisStrategy {
        @Override
        public PageFeatures analyze(LoadedPage page) {
            return PageFeatures.of(List.of(PageFeatures.Feature.of("feature", "value")));
        }

        @Override
        public boolean supports(java.net.URL url) {
            return true;
        }
    }
}
