package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.analyze.model.LoadedPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPageImpl;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;

/**
 * Loads html pages from different sources in parallel and from the same source sequentially.
 * A pause can be configured for sequential calls. It's 10 seconds by default.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class GentlePageLoader implements PageLoader {

    private final SourceCollectionRepository sourceCollectionRepository;
    private final HtmlPageLoader loader;
    @Setter
    private Duration pauseBetweenSlices = Duration.ofSeconds(10);

    @Override
    public List<LoadedPage> loadAll(int page, int size) {
        return getUrlStreamSlices(page, size)
                .flatMap(this::loadSlice)
                .collect(toList());
    }

    /**
     * Loads url streams after a pause. Pause introduced to slower calls between slices.
     *
     * @param urlStream stream of {@link URL} to download
     * @return Stream of successfully loaded {@link LoadedPage}
     */
    @SneakyThrows
    private Stream<LoadedPage> loadSlice(Stream<URL> urlStream) {
        TimeUnit.SECONDS.sleep(pauseBetweenSlices.getSeconds());
        return urlStream
                .map(this::load)
                .map(CompletableFuture::join);
    }

    private CompletableFuture<LoadedPage> load(URL url) {
        return loader.load(url).thenApply(LoadedPageImpl::from);
    }

    private Stream<Stream<URL>> getUrlStreamSlices(int page, int size) {
        List<SourceCollection> sourceCollections = sourceCollectionRepository.findAll(page, size);
        Integer numberOfSlices = estimateNumberOfSlices(sourceCollections);
        return IntStream.range(0, numberOfSlices)
                .mapToObj(i -> getSlice(sourceCollections, i));
    }

    private Integer estimateNumberOfSlices(List<SourceCollection> sourceCollections) {
        return sourceCollections.stream()
                .map(s -> s.getUrls().size())
                .max(naturalOrder())
                .orElse(0);
    }

    private Stream<URL> getSlice(List<SourceCollection> sourceCollections, int index) {
        return sourceCollections.stream()
                .map(SourceCollection::getUrls)
                .filter(urls -> urls.size() > index)
                .map(urls -> urls.get(index));
    }

}
