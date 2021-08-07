package com.github.f1xman.parsgen.core.load.model;

import lombok.Value;

import java.net.URL;

@Value(staticConstructor = "of")
public class HtmlPage {

    URL url;
    String content;

}
