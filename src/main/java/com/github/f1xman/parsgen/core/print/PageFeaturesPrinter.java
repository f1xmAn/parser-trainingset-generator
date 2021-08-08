package com.github.f1xman.parsgen.core.print;

import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;

import java.net.URI;
import java.util.List;

public interface PageFeaturesPrinter {
    URI print(List<PageFeatures> pageFeatures, String runId);
}
