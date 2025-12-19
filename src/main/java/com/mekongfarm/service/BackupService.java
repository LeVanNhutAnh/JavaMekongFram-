package com.mekongfarm.service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service sao lưu và khôi phục database
 */
public class BackupService {

    private static final String BACKUP_FOLDER = "backups";
    private static final String DB_NAME = "mekongfarm.db";

    public BackupService() {
        // Tạo folder backup nếu chưa có
        new File(BACKUP_FOLDER).mkdirs();
    }

    /**
     * Sao lưu database
     */
    public String saoLuu() throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupFileName = BACKUP_FOLDER + "/backup_" + timestamp + ".db";

        Path source = Paths.get(DB_NAME);
        Path target = Paths.get(backupFileName);

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("✓ Đã sao lưu: " + backupFileName);
        return backupFileName;
    }

    /**
     * Khôi phục database từ file backup
     */
    public void khoiPhuc(File backupFile) throws IOException {
        Path source = backupFile.toPath();
        Path target = Paths.get(DB_NAME);

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("✓ Đã khôi phục từ: " + backupFile.getName());
    }

    /**
     * Lấy danh sách các file backup
     */
    public List<File> layDanhSachBackup() {
        File folder = new File(BACKUP_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.startsWith("backup_") && name.endsWith(".db"));

        if (files == null)
            return new ArrayList<>();

        List<File> list = Arrays.asList(files);
        list.sort((a, b) -> b.getName().compareTo(a.getName())); // Mới nhất trước
        return list;
    }

    /**
     * Xóa file backup cũ (giữ lại N file mới nhất)
     */
    public void xoaBackupCu(int giuLai) throws IOException {
        List<File> backups = layDanhSachBackup();

        for (int i = giuLai; i < backups.size(); i++) {
            Files.delete(backups.get(i).toPath());
        }
    }
}
