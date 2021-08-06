package com.github.f1xman.parsgen.core.load;

import java.util.List;

public interface SourceCollectionRepository {
    List<SourceCollection> findAll(int page, int size);
}
