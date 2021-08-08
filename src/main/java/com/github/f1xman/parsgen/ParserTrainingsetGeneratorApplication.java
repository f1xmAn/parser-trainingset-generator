package com.github.f1xman.parsgen;

import com.github.f1xman.parsgen.core.DatasetGenerator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class ParserTrainingsetGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParserTrainingsetGeneratorApplication.class, args);
    }

    @Bean
    @Profile("!test")
    ApplicationRunner runner(DatasetGenerator generator) {
        return args -> generator.generate();
    }

}
