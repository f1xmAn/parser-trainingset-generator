package com.github.f1xman.parsgen.core;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import com.github.f1xman.parsgen.core.load.PageLoader;
import com.github.f1xman.parsgen.core.load.model.LoadedPage;
import com.github.f1xman.parsgen.core.print.PageFeaturesPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DatasetGeneratorTest {

    static final Clock CLOCK = Clock.fixed(Instant.parse("2007-12-03T10:15:30.00Z"), ZoneId.of("UTC"));
    static final int SIZE = 20;
    static final String FOO_BAR = "foo.bar";
    static final int PAGE_0 = 0;
    static final int PAGE_1 = 1;

    @Mock
    PageLoader loader;
    @Mock
    LoadedPageAnalyzer analyzer;
    @Mock
    LoadedPage loadedPage;
    @Mock
    LoadedPage loadedPageWithoutStrategy;
    @Mock
    PageFeaturesPrinter printer;

    @Test
    void orchestratesDatasetGeneratorUntilAllSourcesProcessed() throws AnalysisStrategyNotFoundException {
        DatasetGenerator datasetGenerator = new DatasetGenerator(loader, analyzer, printer, CLOCK);
        given(loader.loadAll(PAGE_0, SIZE)).willReturn(List.of(loadedPageWithoutStrategy));
        given(loadedPageWithoutStrategy.analyze(analyzer)).willThrow(new AnalysisStrategyNotFoundException(FOO_BAR));
        given(loader.loadAll(PAGE_1, SIZE)).willReturn(List.of(loadedPage));
        PageFeatures pageFeatures = PageFeatures.of(List.of());
        given(loadedPage.analyze(analyzer)).willReturn(pageFeatures);

        datasetGenerator.generate();

        then(printer).should().print(List.of(pageFeatures), "2007-12-03T10:15:30");
    }

    @Test
    void doesNotInvokePrintIfDatasetNotGenerated() {
        DatasetGenerator datasetGenerator = new DatasetGenerator(loader, analyzer, printer, CLOCK);
        given(loader.loadAll(PAGE_0, SIZE)).willReturn(List.of());

        datasetGenerator.generate();

        then(printer).shouldHaveNoInteractions();
    }
}
