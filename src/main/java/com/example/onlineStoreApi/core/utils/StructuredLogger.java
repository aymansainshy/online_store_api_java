package com.example.onlineStoreApi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

public class StructuredLogger {
    private static final Logger log = LoggerFactory.getLogger(StructuredLogger.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Log an application-level structured message.
     * - app_message becomes the log message (so it maps to app_message in the pattern).
     * - log_info becomes an escaped JSON string in MDC (so it remains valid JSON in the output).
     * - message field remains empty (we log the app_message in app_message).
     */
    public static void debug(String appMessage, String message) {
        try {
            // serialize info to JSON and escape quotes so the pattern's JSON remains valid
//            String json = mapper.writeValueAsString(info);
//            String escaped = json.replace("\"", "\\\""); // results in {"a":"b"} -> {\"a\":\"b\"}
//            MDC.put("log_info", json);
            MDC.put("app_message", appMessage);

            // log the app-level message — the pattern maps %msg -> app_message
            log.debug(message);
//            MDC.remove("log_info");
            MDC.remove("app_message");


        } finally {
//            MDC.remove("log_info");
            MDC.remove("app_message");
        }
    }
}