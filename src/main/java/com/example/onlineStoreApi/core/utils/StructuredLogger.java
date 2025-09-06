package com.example.onlineStoreApi.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.function.BiConsumer;

public class StructuredLogger {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void debug(String appMessage, Map<String, Object> info) {
        logWithLevel(appMessage, info, Logger::debug);
    }

    public static void info(String appMessage, Map<String, Object> info) {
        logWithLevel(appMessage, info, Logger::info);
    }

    public static void error(String appMessage, Map<String, Object> info) {
        logWithLevel(appMessage, info, Logger::error);
    }


    private static void logWithLevel(String appMessage, Map<String, Object> info, BiConsumer<Logger, String> logMethod) {
        String callerClassName = getCallerClassName();
        Logger logger = LoggerFactory.getLogger(callerClassName);

        try {
            String json = mapper.writeValueAsString(info);
            MDC.put("log_info", json);
            logMethod.accept(logger, appMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            MDC.clear();
        }
    }


    private static String getCallerClassName() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stack) {
            String className = element.getClassName();
            if (!className.equals(StructuredLogger.class.getName())
                    && !className.equals(Thread.class.getName())) {
                return className;
            }
        }
        return StructuredLogger.class.getName();
    }

}
