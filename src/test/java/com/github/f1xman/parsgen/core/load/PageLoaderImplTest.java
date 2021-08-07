package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.HtmlPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPageImpl;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PageLoaderImplTest {

    static final int PAGE = 1;
    static final int SIZE = 1;
    static final String BASE_URL = "baseUrl";
    static final URL HTTPS_FOO_BAR_BAZ = url();
    static final String CONTENT = "content";

    @Mock
    SourceCollectionRepository sourceCollectionRepository;
    @Mock
    HtmlPageLoader htmlPageLoader;

    @Test
    void queriesSourceCollectionsAndLoadsPages() {
        PageLoaderImpl pageLoader = new PageLoaderImpl(sourceCollectionRepository, htmlPageLoader);
        List<SourceCollection> sourceCollections = List.of(SourceCollection.of(BASE_URL, List.of(HTTPS_FOO_BAR_BAZ)));
        given(sourceCollectionRepository.findAll(PAGE, SIZE)).willReturn(sourceCollections);
        HtmlPage htmlPage = HtmlPage.of(HTTPS_FOO_BAR_BAZ, CONTENT);
        given(htmlPageLoader.load(HTTPS_FOO_BAR_BAZ)).willReturn(CompletableFuture.completedFuture(htmlPage));
        List<LoadedPage> expectedLoadedPages = List.of(LoadedPageImpl.from(htmlPage));

        List<LoadedPage> actualLoadedPages = pageLoader.loadAll(PAGE, SIZE);

        then(actualLoadedPages).isEqualTo(expectedLoadedPages);
    }

    @SneakyThrows
    private static URL url() {
        return new URL("https://foo.bar/baz");
    }
}
