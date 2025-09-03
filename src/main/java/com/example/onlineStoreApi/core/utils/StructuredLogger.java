package com.example.onlineStoreApi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

public class StructuredLogger {

    private  final ObjectMapper mapper = new ObjectMapper();
    private  static  Logger log;


    public static  StructuredLogger getLogger(Class<?> clazz) {
           log = LoggerFactory.getLogger(clazz);
          return new StructuredLogger();
    }

    /**
     * Log an application-level structured message.
     * - app_message becomes the log message (so it maps to app_message in the pattern).
     * - log_info becomes an escaped JSON string in MDC (so it remains valid JSON in the output).
     * - message field remains empty (we log the app_message in app_message).
     */
    public void debug(String appMessage, Object info) {
        try {
            // serialize info to JSON and escape quotes so the pattern's JSON remains valid
            String json = mapper.writeValueAsString(info);
//            String escaped = json.replace("\"", "\\\""); // results in {"a":"b"} -> {\"a\":\"b\"}
            MDC.put("log_info", json);
//            MDC.put("app_message", appMessage);

            // log the app-level message — the pattern maps %msg -> app_message
            log.debug(appMessage);
            MDC.remove("log_info");
//            MDC.remove("app_message");

        } catch (JsonProcessingException e) {
            MDC.remove("log_info");
            throw new RuntimeException(e);
        } finally {
            MDC.remove("log_info");
//            MDC.remove("app_message");
        }
    }
}