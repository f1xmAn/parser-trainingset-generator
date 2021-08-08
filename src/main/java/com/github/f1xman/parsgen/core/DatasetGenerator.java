package com.github.f1xman.parsgen.core;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import com.github.f1xman.parsgen.core.load.PageLoader;
import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.print.PageFeaturesPrinter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
@Component
public class DatasetGenerator {

    private final PageLoader loader;
    private final LoadedPageAnalyzer analyzer;
    private final PageFeaturesPrinter printer;
    private final Clock clock;
    @Setter
    private int pageSize = 20;
    private String runId;


    public void generate() {
        runId = LocalDateTime.now(clock).toString();
        List<LoadedPage> loadedPages;
        int page = 0;
        do {
            loadedPages = loader.loadAll(page++, pageSize);
            analyzeAndPrint(loadedPages);
        } while (!loadedPages.isEmpty());
    }

    private void analyzeAndPrint(List<LoadedPage> loadedPages) {
        List<PageFeatures> featuresList = loadedPages.stream()
                .map(this::analyze)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        if (!featuresList.isEmpty()) {
            printer.print(featuresList, runId);
        }
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
