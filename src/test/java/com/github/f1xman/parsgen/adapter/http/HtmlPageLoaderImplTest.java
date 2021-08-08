package com.github.f1xman.parsgen.adapter.http;

import com.github.f1xman.parsgen.core.load.model.HtmlPage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpResponse;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockserver.model.HttpRequest.request;

@Testcontainers
class HtmlPageLoaderImplTest {

    static final DockerImageName MOCKSERVER_IMAGE = DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2");
    @Container
    static final MockServerContainer mockServer = new MockServerContainer(MOCKSERVER_IMAGE);
    static final String PATH = "/foo";
    private String html;

    @BeforeEach
    void setUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("html/fake.html").getFile());
        html = Files.readString(file.toPath());
        MockServerClient client = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
        client.when(request().withPath(PATH)).respond(HttpResponse.response().withBody(html));
    }

    @Test
    void downloadsHtmlFromGivenURL() throws ExecutionException, InterruptedException {
        HtmlPageLoaderImpl loader = new HtmlPageLoaderImpl();
        URL url = url(mockServer.getHost(), mockServer.getServerPort());

        HtmlPage htmlPage = loader.load(url).get();

        then(htmlPage.getUrl()).isEqualTo(url);
        then(htmlPage.getContent()).isEqualTo(html);
    }

    @SneakyThrows
    static URL url(String host, Integer port) {
        return new URL(String.format("http://%s:%s/%s", host, port, PATH));
    }
}
