package com.mekongfarm.util;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * Application Logger - Proper logging thay thế System.out/err
 * Ghi log vào file và console
 */
public class AppLogger {
    
    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static Logger appLogger;
    
    static {
        try {
            // Tạo thư mục logs nếu chưa có
            Path logPath = Paths.get(LOG_DIR);
            if (!Files.exists(logPath)) {
                Files.createDirectories(logPath);
            }
            
            // Tạo logger
            appLogger = Logger.getLogger("MekongFarm");
            appLogger.setLevel(Level.ALL);
            appLogger.setUseParentHandlers(false);
            
            // Console Handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleLogFormatter());
            appLogger.addHandler(consoleHandler);
            
            // File Handler - log chi tiết
            String logFileName = LOG_DIR + "/app_" + LocalDateTime.now().format(DATE_FORMAT) + ".log";
            FileHandler fileHandler = new FileHandler(logFileName, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new DetailedLogFormatter());
            appLogger.addHandler(fileHandler);
            
            // Error File Handler - chỉ log lỗi
            String errorFileName = LOG_DIR + "/error_" + LocalDateTime.now().format(DATE_FORMAT) + ".log";
            FileHandler errorHandler = new FileHandler(errorFileName, true);
            errorHandler.setLevel(Level.WARNING);
            errorHandler.setFormatter(new DetailedLogFormatter());
            appLogger.addHandler(errorHandler);
            
        } catch (IOException e) {
            System.err.println("Không thể khởi tạo logger: " + e.getMessage());
        }
    }
    
    /**
     * Get logger instance
     */
    public static Logger getLogger() {
        return appLogger;
    }
    
    /**
     * Log info
     */
    public static void info(String message) {
        appLogger.info(message);
    }
    
    /**
     * Log warning
     */
    public static void warning(String message) {
        appLogger.warning(message);
    }
    
    /**
     * Log error
     */
    public static void error(String message, Throwable throwable) {
        appLogger.log(Level.SEVERE, message, throwable);
    }
    
    /**
     * Log error without throwable
     */
    public static void error(String message) {
        appLogger.severe(message);
    }
    
    /**
     * Log debug (fine)
     */
    public static void debug(String message) {
        appLogger.fine(message);
    }
    
    /**
     * Simple formatter for console
     */
    static class SimpleLogFormatter extends Formatter {
        private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        @Override
        public String format(LogRecord record) {
            String time = LocalDateTime.now().format(TIME_FORMAT);
            String level = record.getLevel().getName();
            String message = record.getMessage();
            
            String icon = switch(level) {
                case "SEVERE" -> "❌";
                case "WARNING" -> "⚠️";
                case "INFO" -> "✓";
                default -> "•";
            };
            
            return String.format("[%s] %s %s: %s%n", time, icon, level, message);
        }
    }
    
    /**
     * Detailed formatter for file
     */
    static class DetailedLogFormatter extends Formatter {
        private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        
        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(LocalDateTime.now().format(TIME_FORMAT)).append("] ");
            sb.append("[").append(record.getLevel()).append("] ");
            sb.append("[").append(record.getSourceClassName()).append(".").append(record.getSourceMethodName()).append("] ");
            sb.append(record.getMessage()).append("\n");
            
            if (record.getThrown() != null) {
                Throwable t = record.getThrown();
                sb.append("Exception: ").append(t.getClass().getName()).append(": ").append(t.getMessage()).append("\n");
                for (StackTraceElement element : t.getStackTrace()) {
                    sb.append("    at ").append(element.toString()).append("\n");
                }
            }
            
            return sb.toString();
        }
    }
}
