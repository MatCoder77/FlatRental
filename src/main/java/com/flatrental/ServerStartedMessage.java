package com.flatrental;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Component
public class ServerStartedMessage implements CommandLineRunner {

    @Override
    public void run(String... args) {
        List<Integer> ids = Arrays.asList(2, 5, 3, 8);
        System.out.println("\nServer started successfully!");
        System.out.println(MessageFormat.format("Ids: {0}", ids));
    }
}
