package com.example.checkrepo.logging;

import com.example.checkrepo.entities.LogTask;
import com.example.checkrepo.exception.ObjectNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogGenerate {

    private static final Logger logger = LoggerFactory.getLogger(LogGenerate.class);

    @Async
    public void generatelogFiles(LogTask task, String date) throws InterruptedException {
        task.setStatus("Generating");
        Thread.sleep(30000);
        logger.info("starting generation for Id {} in thread {}", task.getTaskId(), Thread.currentThread().getName());
        try {
            if (date == null || date.isEmpty()) {
                date = LocalDate.now().toString();
            }
            String newLogFile;
            String logname = "app." + date + ".log";
            String currentTime;
            Path logFilePath = Paths.get("logs", logname);

            if (!Files.exists(logFilePath)) {
                throw new ObjectNotFoundException("error");
            } else {
                System.out.println("ok");
            }
            currentTime = date;
            newLogFile = "logs/byDate/" + date + ".log";
            System.out.println(newLogFile);

            List<String> logEntries = new ArrayList<>();
            List<String> lines = Files.readAllLines(Paths.get("logs", logname));
            for (String line : lines) {
                if (line.contains(currentTime)) {
                    logEntries.add(line);
                }
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(newLogFile));
            for (String line : logEntries) {
                writer.write(line);
                writer.newLine();
            }

            task.setFilePath(newLogFile);
            task.setStatus("completed");
            logger.info("Log file generated");

        } catch (IOException e) {
            logger.error("cannot generate for task {}", task.getTaskId());
            task.setStatus("Failed");
            logger.error("can not generate {}", e.getMessage());
        }
    }
}
