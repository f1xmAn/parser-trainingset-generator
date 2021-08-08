package com.github.f1xman.parsgen.adapter.http;

import com.github.f1xman.parsgen.core.load.HtmlPageLoader;
import com.github.f1xman.parsgen.core.load.model.HtmlPage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class HtmlPageLoaderImpl implements HtmlPageLoader {
    @Override
    public CompletableFuture<HtmlPage> load(URL url) {
        HttpRequest request = HttpRequest.newBuilder(toUri(url))
                .GET()
                .build();
        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .whenComplete((b, e) -> log(url, e))
                .thenApply(content -> HtmlPage.of(url, content));
    }

    private void log(URL url, Throwable e) {
        if (e == null) {
            log.debug("Page {} loaded successfully", url);
        } else {
            log.error("Failed to load page {}", url, e);
        }
    }

    @SneakyThrows
    private URI toUri(URL url) {
        return url.toURI();
    }
}
