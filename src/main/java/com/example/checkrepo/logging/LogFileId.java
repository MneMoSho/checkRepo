package com.example.checkrepo.logging;

import com.example.checkrepo.entities.LogTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogFileId {
    final Map<String, LogTask> tasks = new ConcurrentHashMap<>();
    private final LogGenerate generator;

   public LogFileId(LogGenerate generator) {
       this.generator = generator;
   }

    public String createFileTask(String date, String level) throws InterruptedException {
      String taskId = UUID.randomUUID().toString();
      System.out.println(taskId);
      LogTask task = new LogTask(taskId);
      tasks.put(taskId, task);
      generator.generatelogFiles(task, date);
        return date;
    }

    public LogTask getTaskStatus(String taskId) {
        return  tasks.get(taskId);
    }

    public String getFilePath(String taskId) {
        LogTask task = tasks.get(taskId);
        if (task != null && "completed".equals(task.getStatus())) {
            return task.getFilePath();
        }
        return null;
    }
}
