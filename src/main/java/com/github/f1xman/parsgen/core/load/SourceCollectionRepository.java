package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.SourceCollection;

import java.util.List;

public interface SourceCollectionRepository {
    List<SourceCollection> findAll(int page, int size);
}
