package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.common.LoadedPage;

import java.net.URL;

public interface PageLoader {
    LoadedPage load(URL url);
}
