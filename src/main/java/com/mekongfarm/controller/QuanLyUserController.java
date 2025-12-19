package com.mekongfarm.controller;

import com.mekongfarm.dao.NguoiDungDAO;
import com.mekongfarm.model.NguoiDung;
import com.mekongfarm.model.VaiTro;
import com.mekongfarm.util.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class QuanLyUserController {

    @FXML
    private TextField txtTimKiem;
    @FXML
    private ComboBox<String> cboVaiTro;
    @FXML
    private TableView<NguoiDung> tableUser;
    @FXML
    private TableColumn<NguoiDung, Void> colAction;
    @FXML
    private TitledPane paneForm;
    @FXML
    private TextField txtUsername, txtHoTen;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<String> cboVaiTroForm;

    private final NguoiDungDAO dao = new NguoiDungDAO();
    private ObservableList<NguoiDung> danhSach = FXCollections.observableArrayList();
    private NguoiDung userDangSua = null;

    @FXML
    public void initialize() {
        // Load vai tro
        cboVaiTro.getItems().addAll("Tất cả", "ADMIN", "NHAN_VIEN", "KHACH_HANG");
        cboVaiTro.setValue("Tất cả");
        cboVaiTroForm.getItems().addAll("ADMIN", "NHAN_VIEN", "KHACH_HANG");
        cboVaiTroForm.setValue("NHAN_VIEN");

        // Setup action column
        setupActionColumn();

        // Load data
        lamMoi();

        // Search listener
        txtTimKiem.textProperty().addListener((obs, old, val) -> timKiem());
        cboVaiTro.setOnAction(e -> timKiem());
    }

    @FXML
    private void lamMoi() {
        danhSach.setAll(dao.layTatCa());
        tableUser.setItems(danhSach);
    }

    private void timKiem() {
        String keyword = txtTimKiem.getText().toLowerCase();
        String vaiTro = cboVaiTro.getValue();

        ObservableList<NguoiDung> filtered = FXCollections.observableArrayList();
        for (NguoiDung u : dao.layTatCa()) {
            boolean matchKeyword = keyword.isEmpty() ||
                    u.getTenDangNhap().toLowerCase().contains(keyword) ||
                    u.getHoTen().toLowerCase().contains(keyword);
            boolean matchVaiTro = "Tất cả".equals(vaiTro) || vaiTro.equals(u.getVaiTro());

            if (matchKeyword && matchVaiTro) {
                filtered.add(u);
            }
        }
        tableUser.setItems(filtered);
    }

    @FXML
    private void moFormThem() {
        userDangSua = null;
        txtUsername.clear();
        txtPassword.clear();
        txtHoTen.clear();
        txtUsername.setEditable(true);
        cboVaiTroForm.setValue("NHAN_VIEN");
        paneForm.setExpanded(true);
    }

    private void moFormSua(NguoiDung user) {
        userDangSua = user;
        txtUsername.setText(user.getTenDangNhap());
        txtUsername.setEditable(false);
        txtPassword.clear();
        txtHoTen.setText(user.getHoTen());
        cboVaiTroForm.setValue(user.getVaiTro());
        paneForm.setExpanded(true);
    }

    @FXML
    private void luuUser() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String hoTen = txtHoTen.getText().trim();
        String vaiTro = cboVaiTroForm.getValue();

        // Validate username
        if (!ValidationUtil.isNotEmpty(username)) {
            ValidationUtil.setErrorStyle(txtUsername);
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập tên đăng nhập!");
            return;
        }
        if (!ValidationUtil.hasValidLength(username, 3, 50)) {
            ValidationUtil.setErrorStyle(txtUsername);
            DialogUtil.showError("Lỗi nhập liệu", "Tên đăng nhập phải từ 3-50 ký tự!");
            return;
        }
        ValidationUtil.clearErrorStyle(txtUsername);
        
        // Validate họ tên
        if (!ValidationUtil.isNotEmpty(hoTen)) {
            ValidationUtil.setErrorStyle(txtHoTen);
            DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập họ tên!");
            return;
        }
        ValidationUtil.clearErrorStyle(txtHoTen);

        LoadingUtil.showLoading("Đang lưu người dùng...");
        
        try {
            if (userDangSua == null) {
                // Thêm mới - validate password
                if (!ValidationUtil.isNotEmpty(password)) {
                    LoadingUtil.hideLoading();
                    ValidationUtil.setErrorStyle(txtPassword);
                    DialogUtil.showError("Lỗi nhập liệu", "Vui lòng nhập mật khẩu!");
                    return;
                }
                if (!ValidationUtil.hasValidLength(password, 6, 100)) {
                    LoadingUtil.hideLoading();
                    ValidationUtil.setErrorStyle(txtPassword);
                    DialogUtil.showError("Lỗi mật khẩu",
                        "Mật khẩu phải từ 6-100 ký tự!\n" +
                        "• Nên dùng kết hợp chữ, số, ký tự đặc biệt");
                    return;
                }
                ValidationUtil.clearErrorStyle(txtPassword);
                
                NguoiDung newUser = new NguoiDung(username, password, hoTen, vaiTro);
                boolean success = dao.them(newUser);
                LoadingUtil.hideLoading();
                
                if (success) {
                    DialogUtil.showSuccess("Thành công",
                        "Đã thêm người dùng: " + hoTen + "\n" +
                        "Tên đăng nhập: " + username + "\n" +
                        "Vai trò: " + vaiTro);
                } else {
                    DialogUtil.showError("Lỗi thêm người dùng",
                        "Không thể thêm người dùng!\n" +
                        "Tên đăng nhập có thể đã tồn tại.");
                    return;
                }
            } else {
                // Cập nhật
                userDangSua.setHoTen(hoTen);
                userDangSua.setVaiTro(vaiTro);
                
                // Nếu đổi mật khẩu
                if (!password.isEmpty()) {
                    if (!ValidationUtil.hasValidLength(password, 6, 100)) {
                        LoadingUtil.hideLoading();
                        ValidationUtil.setErrorStyle(txtPassword);
                        DialogUtil.showError("Lỗi mật khẩu", "Mật khẩu mới phải từ 6-100 ký tự!");
                        return;
                    }
                    dao.doiMatKhau(userDangSua.getMaNguoiDung(), password);
                }
                
                boolean success = dao.capNhat(userDangSua);
                LoadingUtil.hideLoading();
                
                if (success) {
                    DialogUtil.showSuccess("Thành công", "Đã cập nhật người dùng: " + hoTen);
                } else {
                    DialogUtil.showError("Lỗi cập nhật", "Không thể cập nhật người dùng!");
                    return;
                }
            }

            huyForm();
            lamMoi();
        } catch (Exception e) {
            LoadingUtil.hideLoading();
            DialogUtil.showError("Lỗi", "Đã xảy ra lỗi!", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void huyForm() {
        paneForm.setExpanded(false);
        userDangSua = null;
    }

    private void xoaUser(NguoiDung user) {
        if (user == null) return;
        
        // Không cho xóa chính mình
        if (DangNhapController.nguoiDungHienTai != null && 
            user.getMaNguoiDung() == DangNhapController.nguoiDungHienTai.getMaNguoiDung()) {
            DialogUtil.showWarning("Không thể xóa",
                "Bạn không thể xóa tài khoản của chính mình!\n" +
                "Vui lòng đăng nhập bằng tài khoản khác để xóa.");
            return;
        }
        
        if (DialogUtil.confirm("Xác nhận xóa người dùng",
            "Bạn có chắc muốn xóa người dùng?\n\n" +
            "• Tên đăng nhập: " + user.getTenDangNhap() + "\n" +
            "• Họ tên: " + user.getHoTen() + "\n" +
            "• Vai trò: " + user.getVaiTro() + "\n\n" +
            "Người dùng sẽ không thể đăng nhập nữa.")) {
            
            LoadingUtil.showLoading("Đang xóa người dùng...");
            boolean success = dao.xoa(user.getMaNguoiDung());
            LoadingUtil.hideLoading();
            
            if (success) {
                DialogUtil.showSuccess("Đã xóa", "Đã xóa người dùng: " + user.getHoTen());
                lamMoi();
            } else {
                DialogUtil.showError("Lỗi xóa", "Không thể xóa người dùng!");
            }
        }
    }

    private void setupActionColumn() {
        colAction.setCellFactory(col -> new TableCell<>() {
            private final Button btnSua = new Button("✏️");
            private final Button btnXoa = new Button("🗑️");
            private final HBox box = new HBox(5, btnSua, btnXoa);
            {
                btnSua.setOnAction(e -> moFormSua(getTableView().getItems().get(getIndex())));
                btnXoa.setOnAction(e -> xoaUser(getTableView().getItems().get(getIndex())));
                btnSua.getStyleClass().add("btn-icon");
                btnXoa.getStyleClass().add("btn-icon-danger");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void thongBao(String msg) {
        DialogUtil.showSuccess("Thông báo", msg);
    }

    private boolean xacNhan(String msg) {
        return DialogUtil.confirm("Xác nhận", msg);
    }
}
