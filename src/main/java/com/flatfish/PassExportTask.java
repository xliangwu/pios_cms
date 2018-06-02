package com.flatfish;

import com.flatfish.export.TableExport;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class PassExportTask {

    public static void main(String[] args) {
        PassExportTask task = new PassExportTask();
        task.exportToFile();
    }

    public void exportToFile() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Properties properties = new Properties();
            properties.load(classLoader.getResourceAsStream("config.ini"));


            String url = properties.getProperty("database");
            String username = properties.getProperty("user");
            String password = properties.getProperty("pwd");
            String machineCode = properties.getProperty("machine_code");
            String outputSuffix = properties.getProperty("output_suffix");
            int startHour = Integer.parseInt(properties.getProperty("start_hour"));


            TableExport export = new TableExport();
            List<String> outputLines = export.exportPassAtToday(machineCode, startHour, url, username, password);

            System.out.println("Start output " + LocalDateTime.now());
            String output = outputSuffix + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + machineCode + ".txt";
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(output))));
            for (String line : outputLines) {
                out.write(line);
                out.newLine();
            }
            out.flush();
            out.close();
            System.out.println("End output :" + LocalDateTime.now() + ",file :" + output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exportToFile(LocalDate startEnd, LocalDate endDate) {
        Timestamp startTime = Timestamp.valueOf(startEnd.atTime(0, 0));
        Timestamp endTime = Timestamp.valueOf(endDate.atTime(0, 0));

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Properties properties = new Properties();
            properties.load(classLoader.getResourceAsStream("config.ini"));

            String url = properties.getProperty("database");
            String username = properties.getProperty("user");
            String password = properties.getProperty("pwd");
            String machineCode = properties.getProperty("machine_code");
            String outputSuffix = properties.getProperty("output_suffix");

            TableExport export = new TableExport();
            List<String> outputLines = export.exportPass(machineCode, startTime, endTime, url, username, password);

            System.out.println("Start output " + LocalDateTime.now());
            String output = outputSuffix + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + machineCode + ".txt";
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(output))));
            for (String line : outputLines) {
                out.write(line);
                out.newLine();
            }
            out.flush();
            out.close();
            System.out.println("End output :" + LocalDateTime.now() + ",file :" + output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
