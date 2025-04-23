package com.example.checkrepo.controller;

import com.example.checkrepo.entities.LogTask;
import com.example.checkrepo.exception.FileNotFound;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.logging.LogFileId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.QueryParam;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
public class LogController {

    public LogFileId logTaskId;

    public LogController(LogFileId logFile) {
        this.logTaskId = logFile;
    }

    @PostMapping("/create")
    @Operation(summary = "order for new log file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно скачан"),
            @ApiResponse(responseCode = "404", description = "Файл лога не найден"),
    })
    public ResponseEntity<String> createLog(@Parameter(description = "Дата лога в формате YYYY-MM-DD") @RequestParam String date, @RequestParam(required = false, defaultValue = "all") String level) throws InterruptedException {
        System.out.println(date);
        String taskId = logTaskId.createFileTask(date, level);
        return new ResponseEntity<>(taskId, HttpStatus.ACCEPTED);
    }

    @GetMapping("/status/{taskId}")
    @Operation(summary = "get status of your request")
    public ResponseEntity<LogTask> getTaskStatus(@PathVariable String taskId) {
        LogTask task = logTaskId.getTaskStatus(taskId);
        if(task == null) {
            return new ResponseEntity<>(task, HttpStatus.NOT_FOUND);
        }
    return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/download/{taskId}")
    public void downloadLogFilepath(@PathVariable String taskId, HttpServletResponse response){
        LogTask checkTask = logTaskId.getTaskStatus(taskId);
        if(!checkTask.getStatus().equals("completed")) {
            System.out.println(checkTask.getStatus());
            throw new IncorrectInputException("file is creating, wait");
        } else {
            String path = logTaskId.getFilePath(taskId);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + path);
            Path filePath = Paths.get(path);
            try {
                Files.copy(filePath, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException exception) {
                exception.printStackTrace();
                throw new RuntimeException("Error while downloading the log file", exception);
            }
        }
    }

    @GetMapping("/download")
    @Operation(summary = "Скачать отчёт с логами",
            description = "Позволяет скачать файл лога за указанную дату и, если указаны, минуты.")
    @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Файл успешно скачан"),
            @ApiResponse(responseCode = "404", description = "Файл лога не найден"),
    })
    public void downloadLogFile(
            @Parameter(description = "Дата лога в формате YYYY-MM-DD") @QueryParam("date") String date,
            HttpServletResponse response) {

        if (date == null || date.isEmpty()) {
            date = LocalDate.now().toString();
        }

        String logFileName;

        logFileName = "logs/" + "app." + date + ".log";
        System.out.println(logFileName);

        Path logFilePath = Paths.get(logFileName);

        if (!Files.exists(logFilePath)) {
            throw new ObjectNotFoundException("Log file not found: " + logFileName);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Download was interrupted", e);
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + logFilePath.getFileName().toString());

        try {
            Files.copy(logFilePath, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error while downloading the log file", exception);
        }
    }

    @GetMapping()
    public List<String> newLogFile(@QueryParam("date") String date,
                                   @QueryParam("minutes") String minutes) {
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

        if (minutes == null) {
            currentTime = date;
            newLogFile = "logs/byDate/" + date + ".log";
            System.out.println(newLogFile);
        } else {
            currentTime = date + " " + minutes;
            newLogFile = "logs/byDate/" + date + "." + minutes + ".log";
            System.out.println(newLogFile);
        }

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
        return logEntries;
    }
}
