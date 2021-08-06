package com.github.f1xman.parsgen.core.analyze;

import java.net.URL;

public interface LoadedPageAnalyzer {
    AnalysisStrategy findStrategy(URL url) throws AnalysisStrategyNotFoundException;
}
