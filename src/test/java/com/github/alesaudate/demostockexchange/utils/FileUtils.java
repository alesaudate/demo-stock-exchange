package com.github.alesaudate.demostockexchange.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class FileUtils {

    public static String getResponseDataForRapidAPI(String file) {
        var classpathResource = new ClassPathResource(String.format("/api/responses/rapidapi/%s", file));
        try {
            return org.apache.commons.io.FileUtils.readFileToString(classpathResource.getFile(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
