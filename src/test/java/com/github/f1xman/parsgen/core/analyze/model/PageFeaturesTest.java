package com.github.f1xman.parsgen.core.analyze.model;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.f1xman.parsgen.core.analyze.model.PageFeatures.Feature;
import static com.github.f1xman.parsgen.core.analyze.model.PageFeatures.of;
import static org.assertj.core.api.BDDAssertions.then;

public class PageFeaturesTest {

    static final String FEATURE = "feature";
    static final String VALUE = "value";
    static final String ANOTHER_FEATURE = "another feature";
    static final String ANOTHER_VALUE = "another value";

    @Test
    void putsFeature() {
        val featuresHolder = new ArrayList<Feature>();
        PageFeatures features = of(featuresHolder);
        Feature expectedFeature = Feature.of(FEATURE, VALUE);

        features.add(expectedFeature);

        then(featuresHolder).contains(expectedFeature);
    }

    @Test
    void getsFeatureNamesInAddedOrder() {
        PageFeatures features = PageFeatures.of(List.of(
                Feature.of(FEATURE, VALUE),
                Feature.of(ANOTHER_FEATURE, ANOTHER_VALUE))
        );

        String[] actualNames = features.getNames();

        then(actualNames).containsExactly(FEATURE, ANOTHER_FEATURE);
    }

    @Test
    void getsValuesInAddedOrder() {
        PageFeatures features = PageFeatures.of(List.of(
                Feature.of(FEATURE, VALUE),
                Feature.of(ANOTHER_FEATURE, ANOTHER_VALUE))
        );

        String[] actualValues = features.getValues();

        then(actualValues).containsExactly(VALUE, ANOTHER_VALUE);
    }
}
