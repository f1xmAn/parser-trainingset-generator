package com.github.f1xman.parsgen.adapter.mongo;

import com.github.f1xman.parsgen.adapter.mongo.document.SourceCollectionDocument;
import com.github.f1xman.parsgen.core.load.SourceCollectionRepository;
import com.github.f1xman.parsgen.core.load.model.SourceCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class MongoSourceCollectionRepository implements SourceCollectionRepository {

    private final SourceCollectionDocumentRepository repository;
    private final SourceCollectionDocumentMapper mapper;

    @Override
    public List<SourceCollection> findAll(int pageNumber, int size) {
        Page<SourceCollectionDocument> page = repository.findAll(PageRequest.of(pageNumber, size));
        return page.stream()
                .map(mapper::map)
                .collect(toList());
    }
}
