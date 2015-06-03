package uk.co.informaticslab.molab3dwxds;

import org.joda.time.DateTimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application starter
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        DateTimeZone.setDefault(DateTimeZone.UTC);
        SpringApplication.run(Application.class, args);
    }

}
