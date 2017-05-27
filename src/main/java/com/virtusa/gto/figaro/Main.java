package com.virtusa.gto.figaro;

import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        ConfigurationReader config = new ConfigurationReader();
        FileUpdater updater = new FileUpdater("prod");

        ArrayList<HashMap> files = config.getFiles();
        files.forEach((file) -> {
            updater.replaceTokens(file);
        });
    }

}
