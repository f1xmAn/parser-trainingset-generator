package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.common.LoadedPage;

import java.util.List;

public interface SourceCollectionLoader {
    List<LoadedPage> loadAll(int page, int size);
}
