package com.github.f1xman.parsgen.adapter.csv;

import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
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
    static final String RUN_ID = "runId";
    private Path tempDirectory;
    private String expectedCreatedCsv;
    private String expectedAppendedCsv;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        tempDirectory = Files.createTempDirectory("parsgen-output");
        ClassLoader classLoader = getClass().getClassLoader();
        URL expectedCreatedUrl = classLoader.getResource("csv/feature-value.csv");
        expectedCreatedCsv = Files.readString(Paths.get(expectedCreatedUrl.toURI()));
        URL expectedAppendedUrl = classLoader.getResource("csv/feature-value-value.csv");
        expectedAppendedCsv = Files.readString(Paths.get(expectedAppendedUrl.toURI()));
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
        PageFeatures.Feature singleFeature = PageFeatures.Feature.of(FEATURE, VALUE);
        PageFeatures pageFeatures = PageFeatures.of(List.of(singleFeature));

        URI outputCsvUri = printer.print(List.of(pageFeatures), RUN_ID);

        then(Files.readString(Path.of(outputCsvUri))).isEqualTo(expectedCreatedCsv);
    }

    @Test
    @SneakyThrows
    void appendsPageFeaturesToExistingCsvFile() {
        Path outputFile = tempDirectory.resolve("features-runId.csv");
        Files.writeString(outputFile, expectedCreatedCsv);
        CsvPageFeaturesPrinter printer = new CsvPageFeaturesPrinter(tempDirectory);
        PageFeatures.Feature singleFeature = PageFeatures.Feature.of(FEATURE, VALUE);
        PageFeatures pageFeatures = PageFeatures.of(List.of(singleFeature));

        URI outputCsvUri = printer.print(List.of(pageFeatures), RUN_ID);

        then(Files.readString(Paths.get(outputCsvUri))).isEqualTo(expectedAppendedCsv);
    }
}
