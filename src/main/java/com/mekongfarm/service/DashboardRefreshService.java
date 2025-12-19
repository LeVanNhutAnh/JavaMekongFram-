package com.mekongfarm.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Service auto-refresh dashboard real-time
 */
public class DashboardRefreshService {

    private static Timeline timeline;
    private static final List<Runnable> refreshCallbacks = new ArrayList<>();
    private static boolean isRunning = false;
    private static int intervalSeconds = 30;

    /**
     * Bắt đầu auto-refresh
     */
    public static void start(int seconds) {
        intervalSeconds = seconds;
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(intervalSeconds), e -> {
            for (Runnable callback : refreshCallbacks) {
                try {
                    callback.run();
                } catch (Exception ex) {
                    System.err.println("Refresh error: " + ex.getMessage());
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        isRunning = true;
        System.out.println("📊 Dashboard auto-refresh started (every " + intervalSeconds + "s)");
    }

    /**
     * Dừng auto-refresh
     */
    public static void stop() {
        if (timeline != null) {
            timeline.stop();
            isRunning = false;
            System.out.println("📊 Dashboard auto-refresh stopped");
        }
    }

    /**
     * Đăng ký callback khi refresh
     */
    public static void addRefreshCallback(Runnable callback) {
        refreshCallbacks.add(callback);
    }

    /**
     * Xóa callback
     */
    public static void removeCallback(Runnable callback) {
        refreshCallbacks.remove(callback);
    }

    /**
     * Kiểm tra đang chạy không
     */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
     * Lấy interval hiện tại
     */
    public static int getInterval() {
        return intervalSeconds;
    }
}
