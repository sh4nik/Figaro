package com.virtusa.gto.figaro;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUpdater.class);
    private static final String APP_TOKEN = "@@";

    private String environment;

    private FileUpdater() {
    }

    public FileUpdater(String environmentParam) {
        LOGGER.info("Environment set to [" + environmentParam + "]");
        environment = environmentParam;
    }

    public void replaceTokens(HashMap fileEntry) {

        String filePath = (String) fileEntry.get("path");
        LOGGER.info("Analyzing file: " + filePath);
        File file = new File(filePath);

        LOGGER.info("Searching for tokens...");
        HashMap fileConfigs = (HashMap) fileEntry.get("configs");

        List<String> existingLines;
        try {
            existingLines = FileUtils.readLines(file, "UTF-8");
        } catch (IOException ex) {
            LOGGER.error("Could not find file!", ex);
            return;
        }
        List<String> processedLines = new ArrayList<>();

        boolean fileChanged = false;
        for (String existingLine : existingLines) {
            boolean tokenFound = false;
            for (Object tokenObj : fileConfigs.keySet()) {
                String token = (String) tokenObj;
                String fullToken = APP_TOKEN + token;
                if (existingLine.trim().startsWith(APP_TOKEN) && existingLine.trim().equals(fullToken)) {
                    tokenFound = true;
                    LOGGER.info("Replacing token: " + fullToken);
                    String replacementString;
                    Object replacement = (Object) fileConfigs.get(token);
                    if (replacement instanceof String) {
                        replacementString = (String) replacement;
                    } else {
                        HashMap replacements = (HashMap) replacement;
                        replacementString = (String) replacements.get(environment);
                        if (replacementString == null) {
                            LOGGER.info("Could not find replacement for specified environment!");
                            break;
                        }
                    };
                    processedLines.add(existingLine.substring(0, existingLine.indexOf(APP_TOKEN)) + replacementString);
                    fileChanged = true;
                    break;
                }
            }
            if (!tokenFound) {
                processedLines.add(existingLine);
            }
        }

        if (fileChanged) {
            LOGGER.info("Writting changes to file...");
            try {
                FileUtils.writeLines(file, processedLines);
            } catch (IOException ex) {
                LOGGER.error("Update failed!", ex);
            }
        }
    }

}
