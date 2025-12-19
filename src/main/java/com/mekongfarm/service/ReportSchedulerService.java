package com.mekongfarm.service;

import java.io.File;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Report Scheduler - Tự động xuất báo cáo định kỳ
 */
public class ReportSchedulerService {

    private static ScheduledExecutorService scheduler;
    private static boolean isRunning = false;

    public enum ScheduleType {
        DAILY, WEEKLY, MONTHLY
    }

    /**
     * Bắt đầu lịch xuất báo cáo tự động
     */
    public static void start(ScheduleType type) {
        if (scheduler != null) {
            scheduler.shutdown();
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();

        long initialDelay = tinhDelayDau(type);
        long period = tinhPeriod(type);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                xuatBaoCaoTuDong();
            } catch (Exception e) {
                System.err.println("Lỗi xuất báo cáo tự động: " + e.getMessage());
            }
        }, initialDelay, period, TimeUnit.HOURS);

        isRunning = true;
        System.out.println("📅 Report scheduler started: " + type);
    }

    /**
     * Dừng scheduler
     */
    public static void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
            isRunning = false;
            System.out.println("📅 Report scheduler stopped");
        }
    }

    /**
     * Xuất báo cáo tự động
     */
    private static void xuatBaoCaoTuDong() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "BaoCao_TuDong_" + timestamp + ".pdf";

        // Tạo thư mục reports nếu chưa có
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        File outputFile = new File(reportsDir, fileName);

        try {
            // Xuất báo cáo PDF
            new PDFExportService().xuatBaoCaoThongKe(outputFile);
            System.out.println("✅ Đã xuất báo cáo tự động: " + outputFile.getPath());

            // Gửi thông báo
            NotificationService.success("Đã xuất báo cáo tự động: " + fileName);
        } catch (Exception e) {
            System.err.println("Lỗi xuất báo cáo: " + e.getMessage());
            NotificationService.error("Lỗi xuất báo cáo tự động");
        }
    }

    /**
     * Tính delay ban đầu (giờ)
     */
    private static long tinhDelayDau(ScheduleType type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = switch (type) {
            case DAILY -> now.plusDays(1).withHour(6).withMinute(0);
            case WEEKLY -> now.with(java.time.DayOfWeek.MONDAY).plusWeeks(1).withHour(6);
            case MONTHLY -> now.withDayOfMonth(1).plusMonths(1).withHour(6);
        };
        return Duration.between(now, next).toHours();
    }

    /**
     * Tính period (giờ)
     */
    private static long tinhPeriod(ScheduleType type) {
        return switch (type) {
            case DAILY -> 24;
            case WEEKLY -> 24 * 7;
            case MONTHLY -> 24 * 30;
        };
    }

    /**
     * Kiểm tra đang chạy không
     */
    public static boolean isRunning() {
        return isRunning;
    }
}
