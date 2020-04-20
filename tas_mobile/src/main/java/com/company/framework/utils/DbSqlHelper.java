package com.company.framework.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DbSqlHelper {

    public static String readSqlFile(String pathFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(pathFile);
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (null != line)
        {
            stringBuilder.append(' ');
            stringBuilder.append(line);
            line = reader.readLine();
        }
        return stringBuilder.toString();
    }
}
