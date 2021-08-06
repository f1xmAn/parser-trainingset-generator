package com.github.f1xman.parsgen.core.common;

import com.github.f1xman.parsgen.core.analyze.PageFeatures;
import com.github.f1xman.parsgen.core.analyze.AnalysisStrategy;
import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import lombok.Value;

import java.net.URL;

@Value(staticConstructor = "of")
public class LoadedPageImpl implements LoadedPage {

    URL url;
    String content;

    @Override
    public PageFeatures analyze(LoadedPageAnalyzer analyzer) throws AnalysisStrategyNotFoundException {
        AnalysisStrategy strategy = analyzer.findStrategy(url);
        return strategy.analyze(this);
    }

}
