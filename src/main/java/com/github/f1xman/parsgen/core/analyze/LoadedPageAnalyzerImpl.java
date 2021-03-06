package com.github.f1xman.parsgen.core.analyze;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LoadedPageAnalyzerImpl implements LoadedPageAnalyzer {

    private final List<AnalysisStrategy> strategies;

    @Override
    public AnalysisStrategy findStrategy(URL url) throws AnalysisStrategyNotFoundException {
        return strategies.stream()
                .filter(s -> s.supports(url))
                .findAny()
                .orElseThrow(() -> new AnalysisStrategyNotFoundException(url.getHost()));
    }
}
