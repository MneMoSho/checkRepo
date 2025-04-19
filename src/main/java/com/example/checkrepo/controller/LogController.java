package com.example.checkrepo.controller;

import com.example.checkrepo.exception.ErrorObject;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.exception.TypeMissMatchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.QueryParam;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    @GetMapping("/download")
    @Operation(summary = "Download review with logs",
            description = "Allows downloading files.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File succesfully downloaded"),
        @ApiResponse(responseCode = "404", description = "Log file was not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorObject.class))),
        @ApiResponse(responseCode = "400", description = "Incorrect",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TypeMissMatchException.class))),
    })
    public void downloadLogFile(
            @Parameter(description = "log date in format YYYY-MM-DD")
            @QueryParam("date") String date,
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

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + logFilePath.getFileName().toString());

        try {
            Files.copy(logFilePath, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Error while downloading the log file", exception);
        }
    }

    @GetMapping()
    @Operation(
            summary = "Get logs by date/time",
            description = "Retrieves log entries filtered by date (and optionally minutes), "
                   + "then saves the matching entries to a new log file in logs/byDate/ directory.",
            responses = {
                @ApiResponse(responseCode = "200",
                        description = "Successfully retrieved log entries"),
                @ApiResponse(responseCode = "404",
                        description = "Log file not found for the specified date",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
                @ApiResponse(responseCode = "400",
                        description = "Incorrect input of dates or minutes",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorObject.class))),
            }
    )
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
