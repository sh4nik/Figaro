package com.virtusa.gto.figaro;

import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Figaro {

    private static final Logger LOGGER = LoggerFactory.getLogger(Figaro.class);

    public static void execute(final String environment) {
        LOGGER.info("Executing for environment [" + environment + "]");
        ConfigurationReader config = new ConfigurationReader();
        FileUpdater updater = new FileUpdater(environment);

        ArrayList<HashMap> files = config.getFiles();
        files.forEach((file) -> {
            updater.replaceTokens(file);
        });
        
        LOGGER.info("Execution complete!");
    }
}
