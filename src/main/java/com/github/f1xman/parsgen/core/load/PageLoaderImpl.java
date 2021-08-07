package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.load.model.LoadedPageImpl;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class PageLoaderImpl implements PageLoader {

    private final SourceCollectionRepository sourceCollectionRepository;
    private final HtmlPageLoader loader;

    @Override
    public List<LoadedPage> loadAll(int page, int size) {
        List<SourceCollection> sourceCollections = sourceCollectionRepository.findAll(page, size);
        return sourceCollections.stream()
                .flatMap(s -> s.getUrls().stream())
                .map(loader::load)
                .map(LoadedPageImpl::from)
                .collect(toList());
    }
}
