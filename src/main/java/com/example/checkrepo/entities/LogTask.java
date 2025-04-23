package com.example.checkrepo.entities;

import lombok.Data;

@Data
public class LogTask {
    private String taskId;
    private String status;
    private String filePath;

    public LogTask(String taskId) {
        this.taskId = taskId;
        this.status = "PENDING";
    }
}
