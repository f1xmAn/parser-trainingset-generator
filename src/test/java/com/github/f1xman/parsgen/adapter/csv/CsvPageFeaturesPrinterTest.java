package com.github.f1xman.parsgen.adapter.csv;

import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import com.github.f1xman.parsgen.core.analyze.model.PageFeatures.Feature;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

public class CsvPageFeaturesPrinterTest {

    static final String FEATURE = "feature";
    static final String VALUE = "value";
    private Path tempDirectory;
    private String expectedCsv;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        tempDirectory = Files.createTempDirectory("parsgen-output");
        URL resource = getClass().getClassLoader().getResource("csv/feature-value.csv");
        expectedCsv = Files.readString(Paths.get(resource.toURI()));
    }

    @AfterEach
    @SneakyThrows
    void tearDown() {
        Files.walk(tempDirectory)
                .sorted(Comparator.reverseOrder())
                .forEachOrdered(this::delete);
    }

    private void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @SneakyThrows
    void printsPageFeaturesToCsvFile() {
        CsvPageFeaturesPrinter printer = new CsvPageFeaturesPrinter(tempDirectory);
        Feature singleFeature = Feature.of(FEATURE, VALUE);
        PageFeatures pageFeatures = PageFeatures.of(List.of(singleFeature));

        URI outputCsvUri = printer.print(List.of(pageFeatures));

        then(Files.readString(Path.of(outputCsvUri))).isEqualTo(expectedCsv);
    }
}
