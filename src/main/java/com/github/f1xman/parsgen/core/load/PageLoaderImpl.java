package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPageImpl;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class PageLoaderImpl implements PageLoader {

    private final SourceCollectionRepository sourceCollectionRepository;
    private final HtmlPageLoader loader;

    @Override
    public List<LoadedPage> loadAll(int page, int size) {
        return getUrlStream(page, size)
                .map(this::load)
                .map(CompletableFuture::join)
                .collect(toList());
    }

    private CompletableFuture<LoadedPage> load(URL url) {
        return loader.load(url).thenApply(LoadedPageImpl::from);
    }

    private Stream<URL> getUrlStream(int page, int size) {
        List<SourceCollection> sourceCollections = sourceCollectionRepository.findAll(page, size);
        return sourceCollections.stream().flatMap(s -> s.getUrls().stream());
    }

}
