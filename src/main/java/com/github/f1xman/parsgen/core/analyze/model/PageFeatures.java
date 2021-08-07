package com.github.f1xman.parsgen.core.analyze.model;

import com.github.f1xman.parsgen.core.analyze.AnalysisStrategy;
import lombok.Getter;
import lombok.Value;

import java.util.List;

import static lombok.AccessLevel.NONE;

/**
 * This is a bag of features produced by {@link AnalysisStrategy}.
 * It doesn't declare features explicitly to enable strategy evolution separate from this product.
 */
@Value(staticConstructor = "of")
public class PageFeatures {

    @Getter(NONE)
    List<Feature> features;

    public void add(Feature feature) {
        features.add(feature);
    }

    public String[] getNames() {
        return features.stream()
                .map(Feature::getName)
                .toArray(String[]::new);
    }

    public String[] getValues() {
        return features.stream()
                .map(Feature::getValue)
                .toArray(String[]::new);
    }

    @Value(staticConstructor = "of")
    public static class Feature {

        String name;
        String value;

    }
}
