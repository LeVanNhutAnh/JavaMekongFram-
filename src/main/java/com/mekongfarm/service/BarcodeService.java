package com.mekongfarm.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.model.SanPham;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Barcode Scanner Service - Quét mã vạch sản phẩm
 */
public class BarcodeService {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    /**
     * Quét barcode từ file ảnh
     */
    public String scanFromFile(File imageFile) {
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            return decodeBarcode(bufferedImage);
        } catch (Exception e) {
            System.err.println("Lỗi đọc ảnh: " + e.getMessage());
            return null;
        }
    }

    /**
     * Quét barcode từ JavaFX Image
     */
    public String scanFromImage(Image fxImage) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
            return decodeBarcode(bufferedImage);
        } catch (Exception e) {
            System.err.println("Lỗi quét barcode: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decode barcode từ BufferedImage
     */
    private String decodeBarcode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.err.println("Không tìm thấy barcode trong ảnh");
            return null;
        }
    }

    /**
     * Tìm sản phẩm theo barcode
     */
    public SanPham timSanPhamTheoBarcode(String barcode) {
        // Giả sử barcode là mã sản phẩm
        for (SanPham sp : sanPhamDAO.layTatCa()) {
            if (sp.getMaSP().equals(barcode)) {
                return sp;
            }
        }
        return null;
    }

    /**
     * Tạo barcode cho sản phẩm
     */
    public Image taoBarcode(String maSP) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(maSP, BarcodeFormat.CODE_128, 200, 80);

            BufferedImage image = new BufferedImage(200, 80, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 80; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
                }
            }

            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            System.err.println("Lỗi tạo barcode: " + e.getMessage());
            return null;
        }
    }
}
