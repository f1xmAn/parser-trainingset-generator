package com.github.f1xman.parsgen.adapter.csv;

import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import com.github.f1xman.parsgen.core.print.PageFeaturesPrinter;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvPageFeaturesPrinter implements PageFeaturesPrinter {

    private static final String FILE_NAME_TEMPLATE = "%s-%s.csv";
    private static final boolean APPEND = true;
    @Value("${print.csv.output-directory}")
    private final Path outputDirectory;
    @Value("${print.csv.output-file-prefix}")
    @Setter
    private String outputFilePrefix = "features";

    @Override
    @SneakyThrows
    public URI print(List<PageFeatures> pageFeatures, String runId) {
        Path outputFile = outputDirectory.resolve(String.format(FILE_NAME_TEMPLATE, outputFilePrefix, runId));
        @Cleanup
        FileWriter writer = new FileWriter(outputFile.toFile(), APPEND);
        PageFeatures firstEntry = pageFeatures.get(0);
        String[] featureNames = firstEntry.getNames();
        @Cleanup
        CSVPrinter printer = new CSVPrinter(writer, getFormat(featureNames, outputFile));
        pageFeatures.stream()
                .map(PageFeatures::getValues)
                .forEach(v -> printRecord(printer, v));
        return outputFile.toUri();
    }

    private CSVFormat getFormat(String[] featureNames, Path outputFile) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withSystemRecordSeparator();
        if (isEmptyOrNotExist(outputFile)) {
            return csvFormat.withHeader(featureNames);
        }
        return csvFormat;
    }

    private boolean isEmptyOrNotExist(Path outputFile) {
        return outputFile.toFile().length() == 0;
    }

    @SneakyThrows
    private void printRecord(CSVPrinter printer, String[] values) {
        printer.printRecord((Object[]) values);
    }
}
