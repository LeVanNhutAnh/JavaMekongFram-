package com.mekongfarm.service;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.nio.file.*;

/**
 * Service quản lý hình ảnh sản phẩm
 */
public class ImageService {

    private static final String IMAGE_FOLDER = "product_images";

    public ImageService() {
        // Tạo folder images nếu chưa có
        new File(IMAGE_FOLDER).mkdirs();
    }

    /**
     * Mở hộp thoại chọn ảnh
     */
    public File chonAnhTuMay(Window window) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn ảnh sản phẩm");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Hình ảnh", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("Tất cả", "*.*"));
        return fc.showOpenDialog(window);
    }

    /**
     * Lưu ảnh vào folder và trả về tên file
     */
    public String luuAnh(File sourceFile, String maSP) throws IOException {
        String extension = layPhầnMởRộng(sourceFile.getName());
        String fileName = maSP + "_" + System.currentTimeMillis() + extension;

        Path target = Paths.get(IMAGE_FOLDER, fileName);
        Files.copy(sourceFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    /**
     * Load ảnh từ tên file
     */
    public Image loadAnh(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        try {
            File file = new File(IMAGE_FOLDER, fileName);
            if (file.exists()) {
                return new Image(file.toURI().toString(), 100, 100, true, true);
            }
        } catch (Exception e) {
            System.err.println("Lỗi load ảnh: " + e.getMessage());
        }
        return null;
    }

    /**
     * Load ảnh với kích thước tùy chỉnh
     */
    public Image loadAnh(String fileName, double width, double height) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        try {
            File file = new File(IMAGE_FOLDER, fileName);
            if (file.exists()) {
                return new Image(file.toURI().toString(), width, height, true, true);
            }
        } catch (Exception e) {
            System.err.println("Lỗi load ảnh: " + e.getMessage());
        }
        return null;
    }

    /**
     * Xóa ảnh
     */
    public void xoaAnh(String fileName) {
        if (fileName == null || fileName.isEmpty())
            return;

        try {
            Files.deleteIfExists(Paths.get(IMAGE_FOLDER, fileName));
        } catch (IOException e) {
            System.err.println("Lỗi xóa ảnh: " + e.getMessage());
        }
    }

    /**
     * Lấy đường dẫn đầy đủ của ảnh
     */
    public String layDuongDan(String fileName) {
        return new File(IMAGE_FOLDER, fileName).getAbsolutePath();
    }

    private String layPhầnMởRộng(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot > 0 ? fileName.substring(dot) : ".jpg";
    }
}
