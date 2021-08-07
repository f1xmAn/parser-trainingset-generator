package com.github.f1xman.parsgen.core;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import com.github.f1xman.parsgen.core.analyze.PageFeatures;
import com.github.f1xman.parsgen.core.common.LoadedPage;
import com.github.f1xman.parsgen.core.load.PageLoader;
import com.github.f1xman.parsgen.core.print.PageFeaturesPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DatasetGeneratorTest {

    static final int SIZE = 20;
    static final String FOO_BAR = "foo.bar";
    static final int PAGE_1 = 1;
    static final int PAGE_2 = 2;

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
        DatasetGenerator datasetGenerator = new DatasetGenerator(loader, analyzer, printer);
        given(loader.loadAll(PAGE_1, SIZE)).willReturn(List.of(loadedPageWithoutStrategy));
        given(loadedPageWithoutStrategy.analyze(analyzer)).willThrow(new AnalysisStrategyNotFoundException(FOO_BAR));
        given(loader.loadAll(PAGE_2, SIZE)).willReturn(List.of(loadedPage));
        PageFeatures pageFeatures = PageFeatures.builder().build();
        given(loadedPage.analyze(analyzer)).willReturn(pageFeatures);

        datasetGenerator.generate();

        then(printer).should().print(pageFeatures);
    }
}
