package com.mekongfarm.service;

/**
 * HTML Email Template Service
 * Provides beautiful HTML email templates
 */
public class HtmlEmailService {

    /**
     * Template email xác nhận đơn hàng
     */
    public static String emailDonHang(String maDH, String tenKH, double tongTien, String chiTiet) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <style>
                                body { font-family: Arial, sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }
                                .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                                .header { background: linear-gradient(135deg, #2e7d32, #4CAF50); color: white; padding: 30px; text-align: center; }
                                .header h1 { margin: 0; font-size: 24px; }
                                .logo { font-size: 40px; margin-bottom: 10px; }
                                .content { padding: 30px; }
                                .order-info { background: #f9f9f9; padding: 20px; border-radius: 8px; margin: 20px 0; }
                                .order-info h3 { margin-top: 0; color: #2e7d32; }
                                .total { font-size: 24px; color: #2e7d32; font-weight: bold; text-align: right; margin-top: 20px; }
                                .footer { background: #333; color: #999; padding: 20px; text-align: center; font-size: 12px; }
                                .btn { display: inline-block; background: #4CAF50; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 20px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <div class="logo">🌾</div>
                                    <h1>MEKONG FARM</h1>
                                    <p>Nông sản Đồng Bằng Sông Cửu Long</p>
                                </div>
                                <div class="content">
                                    <h2>Xin chào %s! 👋</h2>
                                    <p>Cảm ơn bạn đã đặt hàng tại <strong>Mekong Farm</strong>!</p>

                                    <div class="order-info">
                                        <h3>📦 Đơn hàng #%s</h3>
                                        <p>%s</p>
                                    </div>

                                    <div class="total">
                                        Tổng cộng: %,.0f VNĐ
                                    </div>

                                    <p style="margin-top: 30px;">Đơn hàng của bạn đang được xử lý. Chúng tôi sẽ liên hệ sớm để xác nhận giao hàng.</p>

                                    <a href="#" class="btn">Theo dõi đơn hàng</a>
                                </div>
                                <div class="footer">
                                    <p>© 2024 Mekong Farm - Nông sản sạch từ ĐBSCL</p>
                                    <p>Hotline: 1900-xxxx | Email: support@mekongfarm.vn</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                tenKH, maDH, chiTiet, tongTien);
    }

    /**
     * Template email cảnh báo tồn kho
     */
    public static String emailCanhBaoTonKho(String tenSP, int soLuong, int nguongCanhBao) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <style>
                                body { font-family: Arial, sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }
                                .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                                .header { background: linear-gradient(135deg, #ff5722, #ff9800); color: white; padding: 30px; text-align: center; }
                                .header h1 { margin: 0; font-size: 24px; }
                                .alert-icon { font-size: 50px; margin-bottom: 10px; }
                                .content { padding: 30px; }
                                .warning-box { background: #fff3e0; border-left: 4px solid #ff9800; padding: 20px; margin: 20px 0; }
                                .stock-info { display: flex; justify-content: space-between; background: #f5f5f5; padding: 15px; border-radius: 8px; margin: 20px 0; }
                                .stock-item { text-align: center; }
                                .stock-value { font-size: 28px; font-weight: bold; color: #f44336; }
                                .footer { background: #333; color: #999; padding: 20px; text-align: center; font-size: 12px; }
                                .btn { display: inline-block; background: #ff9800; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; margin-top: 20px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <div class="alert-icon">⚠️</div>
                                    <h1>CẢNH BÁO TỒN KHO</h1>
                                </div>
                                <div class="content">
                                    <div class="warning-box">
                                        <strong>Sản phẩm sắp hết hàng!</strong>
                                        <p style="margin: 10px 0 0 0;">Cần nhập thêm hàng sớm để tránh thiếu hàng.</p>
                                    </div>

                                    <h3>📦 %s</h3>

                                    <div class="stock-info">
                                        <div class="stock-item">
                                            <div class="stock-value">%d</div>
                                            <div>Số lượng còn</div>
                                        </div>
                                        <div class="stock-item">
                                            <div class="stock-value" style="color: #ff9800;">%d</div>
                                            <div>Ngưỡng cảnh báo</div>
                                        </div>
                                    </div>

                                    <a href="#" class="btn">Đặt hàng nhập kho</a>
                                </div>
                                <div class="footer">
                                    <p>© 2024 Mekong Farm - Hệ thống quản lý</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                tenSP, soLuong, nguongCanhBao);
    }

    /**
     * Template email chào mừng khách hàng mới
     */
    public static String emailChaoMung(String tenKH) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <style>
                                body { font-family: Arial, sans-serif; background: #f4f4f4; margin: 0; padding: 20px; }
                                .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                                .header { background: linear-gradient(135deg, #1976D2, #42A5F5); color: white; padding: 40px; text-align: center; }
                                .header h1 { margin: 0; font-size: 28px; }
                                .welcome-icon { font-size: 60px; margin-bottom: 15px; }
                                .content { padding: 30px; text-align: center; }
                                .features { display: flex; justify-content: space-around; margin: 30px 0; flex-wrap: wrap; }
                                .feature { width: 140px; padding: 15px; text-align: center; }
                                .feature-icon { font-size: 30px; margin-bottom: 10px; }
                                .footer { background: #333; color: #999; padding: 20px; text-align: center; font-size: 12px; }
                                .btn { display: inline-block; background: #1976D2; color: white; padding: 15px 40px; text-decoration: none; border-radius: 5px; margin-top: 20px; font-size: 16px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <div class="welcome-icon">🎉</div>
                                    <h1>Chào mừng đến Mekong Farm!</h1>
                                </div>
                                <div class="content">
                                    <h2>Xin chào %s! 👋</h2>
                                    <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>Mekong Farm</strong>!</p>

                                    <div class="features">
                                        <div class="feature">
                                            <div class="feature-icon">🌾</div>
                                            <div>Nông sản sạch</div>
                                        </div>
                                        <div class="feature">
                                            <div class="feature-icon">🚚</div>
                                            <div>Giao hàng nhanh</div>
                                        </div>
                                        <div class="feature">
                                            <div class="feature-icon">💯</div>
                                            <div>Chất lượng đảm bảo</div>
                                        </div>
                                    </div>

                                    <a href="#" class="btn">Bắt đầu mua sắm</a>
                                </div>
                                <div class="footer">
                                    <p>© 2024 Mekong Farm - Nông sản sạch từ ĐBSCL</p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                tenKH);
    }
}
