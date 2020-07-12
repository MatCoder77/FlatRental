package com.flatrental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerStartedMessage implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ServerStartedMessage.class);

    @Override
    public void run(String... args) {
        logger.info("\nServer started successfully!");
    }
}
