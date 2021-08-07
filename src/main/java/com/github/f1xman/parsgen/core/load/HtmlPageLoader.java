package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.HtmlPage;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public interface HtmlPageLoader {
    CompletableFuture<HtmlPage> load(URL url);
}
