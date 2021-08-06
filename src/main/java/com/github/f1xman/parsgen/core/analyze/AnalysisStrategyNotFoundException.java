package com.github.f1xman.parsgen.core.analyze;

import lombok.Getter;

public class AnalysisStrategyNotFoundException extends Exception {

    @Getter
    private final String host;

    public AnalysisStrategyNotFoundException(String host) {
        super(String.format("No analysis strategy found for host %s", host));
        this.host = host;
    }
}
