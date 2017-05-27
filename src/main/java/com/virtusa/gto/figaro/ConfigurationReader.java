package com.virtusa.gto.figaro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationReader.class);
    private static final String CONFIG_FILE_NAME = "figaro.json";

    private ArrayList<HashMap> files = new ArrayList<>();

    public ConfigurationReader() {

        LOGGER.info("Searching for configuration file...");
        File configFile = new File(CONFIG_FILE_NAME);
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info("Loading configurations...");
        Map<String, Object> config;
        try {
            config = mapper.readValue(configFile,
                    new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException ex) {
            LOGGER.error("Failed to load configurations!", ex);
            return;
        }
        files = (ArrayList<HashMap>) config.get("files");
        LOGGER.info("The following files will be updated:");
        print();

    }

    public ArrayList<HashMap> getFiles() {
        return files;
    }

    private void print() {
        for (HashMap file : files) {
            LOGGER.info("  >> " + (String) file.get("path"));
            HashMap fileConfigs = (HashMap) file.get("configs");
            StringBuilder fileConfigsString = new StringBuilder();
            fileConfigsString.append("  [");
            StringJoiner joiner = new StringJoiner(", ");
            for (Object fileConfig : fileConfigs.keySet()) {
                joiner.add((String) fileConfig);
            }
            fileConfigsString.append(joiner.toString());
            fileConfigsString.append("]");
            LOGGER.info(fileConfigsString.toString());
        }
    }

}
