package com.github.f1xman.parsgen.core;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import com.github.f1xman.parsgen.core.analyze.PageFeatures;
import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.load.PageLoader;
import com.github.f1xman.parsgen.core.print.PageFeaturesPrinter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class DatasetGenerator {

    private final PageLoader loader;
    private final LoadedPageAnalyzer analyzer;
    private final PageFeaturesPrinter printer;
    @Setter
    private int pageSize = 20;


    public void generate() {
        List<LoadedPage> loadedPages;
        int page = 1;
        do {
            loadedPages = loader.loadAll(page++, pageSize);
            analyzeAndPrint(loadedPages);
        } while (!loadedPages.isEmpty());
    }

    private void analyzeAndPrint(List<LoadedPage> loadedPages) {
        loadedPages.stream()
                .map(this::analyze)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(printer::print);
    }

    private Optional<PageFeatures> analyze(LoadedPage p) {
        try {
            return Optional.of(p.analyze(analyzer));
        } catch (AnalysisStrategyNotFoundException e) {
            log.warn("Cannot find analysis strategy for host {}", e.getHost(), e);
            return Optional.empty();
        }
    }
}
