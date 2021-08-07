package com.github.f1xman.parsgen.core.load.model;

import lombok.Value;

import java.net.URL;
import java.util.List;

@Value
public class SourceCollection {

    String id;
    String baseUrl;
    List<URL> urls;

    public static SourceCollection of(String baseUrl, List<URL> urls) {
        return new SourceCollection(null, baseUrl, urls);
    }
}
