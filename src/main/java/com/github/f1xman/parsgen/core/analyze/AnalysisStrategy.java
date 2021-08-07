package com.github.f1xman.parsgen.core.analyze;

import com.github.f1xman.parsgen.core.load.model.LoadedPage;

public interface AnalysisStrategy {
    PageFeatures analyze(LoadedPage page);
}
