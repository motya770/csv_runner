package com.company;

import com.company.service.impl.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static final String TEMP_FILE_SUFF =
            "/Users/kudelin/Desktop/work/projects/csv_runner_2/src/main/resources/small/temp";

    @Autowired
    private CsvService csvService;

    @Override
    public void run(String... args) throws Exception {
        csvService.sortMainCSVFile(
                "/Users/kudelin/Desktop/work/projects/csv_runner_2/src/main/resources/mainFile2.csv",
                "/Users/kudelin/Desktop/work/projects/csv_runner_2/src/main/resources/small/",
                4, 10);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
