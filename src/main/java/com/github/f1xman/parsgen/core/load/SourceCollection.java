package com.github.f1xman.parsgen.core.load;

import lombok.Value;

import java.net.URL;
import java.util.List;

@Value(staticConstructor = "of")
public class SourceCollection {

    String baseUrl;
    List<URL> urls;

}
