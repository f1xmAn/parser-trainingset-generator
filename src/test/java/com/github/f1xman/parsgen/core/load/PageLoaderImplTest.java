package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.HtmlPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPageImpl;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class PageLoaderImplTest {

    static final int PAGE = 1;
    static final int SIZE = 1;
    static final URL SOURCE_1_URL_1 = url("https://foo.bar/baz1");
    static final URL SOURCE_1_URL_2 = url("https://foo.bar/baz2");
    static final URL SOURCE_2_URL_1 = url("https://foo.baz/bar1");
    static final List<SourceCollection> SOURCE_COLLECTIONS = List.of(
            SourceCollection.of("https://boo.bar", List.of(SOURCE_1_URL_1, SOURCE_1_URL_2)),
            SourceCollection.of("https://boo.baz", List.of(SOURCE_2_URL_1))
    );
    static final String CONTENT = "content";

    @Mock
    SourceCollectionRepository sourceCollectionRepository;
    @Mock
    HtmlPageLoader htmlPageLoader;

    @SneakyThrows
    private static URL url(String url) {
        return new URL(url);
    }

    @Test
    void queriesSourceCollectionsAndLoadsPages() {
        GentlePageLoader pageLoader = new GentlePageLoader(sourceCollectionRepository, htmlPageLoader);
        pageLoader.setPauseBetweenSlices(Duration.ZERO);
        given(sourceCollectionRepository.findAll(PAGE, SIZE)).willReturn(SOURCE_COLLECTIONS);
        HtmlPage source1Page1 = HtmlPage.of(SOURCE_1_URL_1, CONTENT);
        given(htmlPageLoader.load(SOURCE_1_URL_1)).willReturn(CompletableFuture.completedFuture(source1Page1));
        HtmlPage source1Page2 = HtmlPage.of(SOURCE_1_URL_2, CONTENT);
        given(htmlPageLoader.load(SOURCE_1_URL_2)).willReturn(CompletableFuture.completedFuture(source1Page2));
        HtmlPage source2Page1 = HtmlPage.of(SOURCE_2_URL_1, CONTENT);
        given(htmlPageLoader.load(SOURCE_2_URL_1)).willReturn(CompletableFuture.completedFuture(source2Page1));

        List<LoadedPage> actualLoadedPages = pageLoader.loadAll(PAGE, SIZE);

        then(actualLoadedPages).contains(
                LoadedPageImpl.from(source1Page1),
                LoadedPageImpl.from(source2Page1),
                LoadedPageImpl.from(source1Page2)
        );
        InOrder inOrder = inOrder(htmlPageLoader);
        inOrder.verify(htmlPageLoader).load(SOURCE_1_URL_1);
        inOrder.verify(htmlPageLoader).load(SOURCE_2_URL_1);
        inOrder.verify(htmlPageLoader).load(SOURCE_1_URL_2);
    }
}
