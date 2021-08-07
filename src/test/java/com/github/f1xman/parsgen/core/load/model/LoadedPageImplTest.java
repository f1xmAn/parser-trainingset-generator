package com.github.f1xman.parsgen.core.load.model;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategy;
import com.github.f1xman.parsgen.core.analyze.AnalysisStrategyNotFoundException;
import com.github.f1xman.parsgen.core.analyze.LoadedPageAnalyzer;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LoadedPageImplTest {

    static final String CONTENT = "content";
    static final URL PAGE_URL = url();

    @Mock
    LoadedPageAnalyzer analyzer;
    @Mock
    AnalysisStrategy strategy;

    @SneakyThrows
    static URL url() {
        return new URL("https://foo.bar/baz");
    }

    @Test
    void analyzesContentUsingProvidedStrategy() throws AnalysisStrategyNotFoundException {
        LoadedPageImpl page = LoadedPageImpl.from(HtmlPage.of(PAGE_URL, CONTENT));
        PageFeatures expectedPageFeatures = PageFeatures.builder().build();
        given(analyzer.findStrategy(PAGE_URL)).willReturn(strategy);
        given(strategy.analyze(page)).willReturn(expectedPageFeatures);

        PageFeatures pageFeatures = page.analyze(analyzer);

        then(pageFeatures).isSameAs(expectedPageFeatures);
    }

    @Test
    @SneakyThrows
    void doesNotSuppressAnalysisStrategyNotFoundException() {
        LoadedPageImpl page = LoadedPageImpl.from(HtmlPage.of(PAGE_URL, CONTENT));
        given(analyzer.findStrategy(PAGE_URL)).willThrow(new AnalysisStrategyNotFoundException("foo.bar"));

        thenThrownBy(() -> page.analyze(analyzer))
                .isInstanceOf(AnalysisStrategyNotFoundException.class);
    }
}