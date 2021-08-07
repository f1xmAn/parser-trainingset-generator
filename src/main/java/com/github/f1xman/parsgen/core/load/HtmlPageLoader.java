package com.github.f1xman.parsgen.core.load;

import com.github.f1xman.parsgen.core.load.model.HtmlPage;

import java.net.URL;

public interface HtmlPageLoader {
    HtmlPage load(URL url);
}
