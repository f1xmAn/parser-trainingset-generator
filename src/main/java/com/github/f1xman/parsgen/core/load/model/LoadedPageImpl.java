package com.github.f1xman.parsgen.core.load.model;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategy;
import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import com.github.f1xman.parsgen.core.analyze.model.LoadedPage;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.net.URL;

import static lombok.AccessLevel.PRIVATE;

@Value
@RequiredArgsConstructor(access = PRIVATE)
public class LoadedPageImpl implements LoadedPage {

    URL url;
    String content;

    @Override
    public PageFeatures analyze(LoadedPageAnalyzer analyzer) throws AnalysisStrategyNotFoundException {
        AnalysisStrategy strategy = analyzer.findStrategy(url);
        return strategy.analyze(this);
    }

    public static LoadedPageImpl from(HtmlPage htmlPage) {
        return new LoadedPageImpl(htmlPage.getUrl(), htmlPage.getContent());
    }
}
