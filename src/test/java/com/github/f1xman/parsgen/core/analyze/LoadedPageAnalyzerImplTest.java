package com.github.f1xman.parsgen.core.analyze;

import com.github.f1xman.parsgen.core.analyze.model.LoadedPage;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

public class LoadedPageAnalyzerImplTest {

    static final URL PAGE_URL = url();

    @SneakyThrows
    private static URL url() {
        return new URL("https://foo.bar/");
    }

    @Test
    @SneakyThrows
    void findsStrategyByAskingEachIfTheUrlSupported() {
        AnalysisStrategy expectedStrategy = new NullStrategy();
        LoadedPageAnalyzerImpl analyzer = new LoadedPageAnalyzerImpl(List.of(expectedStrategy));

        AnalysisStrategy actualStrategy = analyzer.findStrategy(PAGE_URL);

        then(actualStrategy).isSameAs(expectedStrategy);
    }

    @Test
    void throwsAnalysisStrategyNotFoundExceptionIfSupportedStrategyDoesNotExist() {
        LoadedPageAnalyzerImpl analyzer = new LoadedPageAnalyzerImpl(List.of());

        thenThrownBy(() -> analyzer.findStrategy(PAGE_URL))
                .isInstanceOf(AnalysisStrategyNotFoundException.class)
                .hasFieldOrPropertyWithValue("host", PAGE_URL.getHost());
    }

    private static class NullStrategy implements AnalysisStrategy {
        @Override
        public PageFeatures analyze(LoadedPage page) {
            return null;
        }

        @Override
        public boolean supports(URL url) {
            return true;
        }
    }

}
