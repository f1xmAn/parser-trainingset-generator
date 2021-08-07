package com.github.f1xman.parsgen.adapter.csv;

import com.github.f1xman.parsgen.core.analyze.model.PageFeatures;
import com.github.f1xman.parsgen.core.print.PageFeaturesPrinter;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CsvPageFeaturesPrinter implements PageFeaturesPrinter {

    private final Path outputDirectory;

    @Override
    @SneakyThrows
    public URI print(List<PageFeatures> pageFeatures) {
        Path outputFile = outputDirectory.resolve(LocalDateTime.now().toString());
        @Cleanup
        FileWriter writer = new FileWriter(outputFile.toFile());
        PageFeatures firstEntry = pageFeatures.get(0);
        String[] featureNames = firstEntry.getNames();
        @Cleanup
        CSVPrinter printer = new CSVPrinter(writer,
                CSVFormat.DEFAULT
                        .withSystemRecordSeparator()
                        .withHeader(featureNames)
        );
        pageFeatures.stream()
                .map(PageFeatures::getValues)
                .forEach(v -> printRecord(printer, v));
        return outputFile.toUri();
    }

    @SneakyThrows
    private void printRecord(CSVPrinter printer, String[] values) {
        printer.printRecord((Object[]) values);
    }
}
