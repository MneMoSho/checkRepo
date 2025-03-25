package com.example.checkrepo.controller;

import com.example.checkrepo.exception.ObjectNotFoundException;
import jakarta.ws.rs.QueryParam;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class logController {

    @GetMapping()
    public void newLogFile(@QueryParam("date") String date, @QueryParam("minutes") String minutes) {
        String logname = "app." + date + ".log";
        Path logFilePath = Paths.get("logs", logname);

        if (!Files.exists(logFilePath)) {
            throw new ObjectNotFoundException("error");
        } else {
            System.out.println("ok");
        }
        String currentTime = date + " " + minutes;
        System.out.println(currentTime);

        String newLogFile = "logs/byDate/" + date + "." + minutes + ".log";
        Path logNewPath = Paths.get("logs", logname);

        List<String> logEntries = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get("logs", logname));
            for (String line : lines) {
                if (line.contains(currentTime)) {
                    logEntries.add(line);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newLogFile))) {
            for (String line : logEntries) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
