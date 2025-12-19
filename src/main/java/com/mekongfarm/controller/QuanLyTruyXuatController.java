package com.mekongfarm.controller;

import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.dao.TruyXuatDAO;
import com.mekongfarm.model.SanPham;
import com.mekongfarm.model.TruyXuatNguonGoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class QuanLyTruyXuatController {

    @FXML private TableView<TruyXuatNguonGoc> tableTruyXuat;
    @FXML private TableColumn<TruyXuatNguonGoc, Integer> colMa;
    @FXML private TableColumn<TruyXuatNguonGoc, String> colSanPham;
    @FXML private TableColumn<TruyXuatNguonGoc, String> colSoLo;
    @FXML private TableColumn<TruyXuatNguonGoc, LocalDate> colNgayThuHoach;
    @FXML private TableColumn<TruyXuatNguonGoc, String> colDiaChi;
    @FXML private TableColumn<TruyXuatNguonGoc, String> colNongDan;
    @FXML private TableColumn<TruyXuatNguonGoc, String> colChungNhan;
    @FXML private TableColumn<TruyXuatNguonGoc, Void> colHanhDong;

    @FXML private TextField txtTimKiem;
    @FXML private Label lblTongSo;

    private final TruyXuatDAO truyXuatDAO = new TruyXuatDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final ObservableList<TruyXuatNguonGoc> danhSach = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() {
        setupTable();
        taiDuLieu();
    }

    private void setupTable() {
        colMa.setCellValueFactory(new PropertyValueFactory<>("maTruyXuat"));
        colSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        colSoLo.setCellValueFactory(new PropertyValueFactory<>("soLo"));
        colNgayThuHoach.setCellValueFactory(new PropertyValueFactory<>("ngayThuHoach"));
        colDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChiSanXuat"));
        colNongDan.setCellValueFactory(new PropertyValueFactory<>("tenNongDan"));
        colChungNhan.setCellValueFactory(new PropertyValueFactory<>("chungNhan"));

        // Format date column
        colNgayThuHoach.setCellFactory(col -> new TableCell<TruyXuatNguonGoc, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(date));
                }
            }
        });

        // Action buttons column
        colHanhDong.setCellFactory(param -> new TableCell<>() {
            private final Button btnSua = new Button("✏️ Sửa");
            private final Button btnXoa = new Button("🗑️ Xóa");
            private final HBox pane = new HBox(5, btnSua, btnXoa);

            {
                btnSua.getStyleClass().add("btn-warning");
                btnXoa.getStyleClass().add("btn-danger");
                pane.setAlignment(Pos.CENTER);

                btnSua.setOnAction(event -> {
                    TruyXuatNguonGoc tx = getTableView().getItems().get(getIndex());
                    moFormSua(tx);
                });

                btnXoa.setOnAction(event -> {
                    TruyXuatNguonGoc tx = getTableView().getItems().get(getIndex());
                    xoaTruyXuat(tx);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableTruyXuat.setItems(danhSach);
    }

    private void taiDuLieu() {
        danhSach.clear();
        danhSach.addAll(truyXuatDAO.layTatCa());
        capNhatThongKe();
    }

    private void capNhatThongKe() {
        lblTongSo.setText(String.valueOf(danhSach.size()));
    }

    @FXML
    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            taiDuLieu();
        } else {
            danhSach.clear();
            danhSach.addAll(truyXuatDAO.timKiem(tuKhoa));
            capNhatThongKe();
        }
    }

    @FXML
    private void lamMoi() {
        txtTimKiem.clear();
        taiDuLieu();
    }

    @FXML
    private void moFormThem() {
        hienThiFormNhap(null);
    }

    private void moFormSua(TruyXuatNguonGoc tx) {
        hienThiFormNhap(tx);
    }

    private void hienThiFormNhap(TruyXuatNguonGoc txEdit) {
        Stage dialog = new Stage();
        dialog.setTitle(txEdit == null ? "Thêm Truy Xuất Mới" : "Sửa Truy Xuất");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white;");

        // Fields
        ComboBox<SanPham> cboSanPham = new ComboBox<>();
        cboSanPham.getItems().addAll(sanPhamDAO.layTatCa());
        cboSanPham.setPromptText("Chọn sản phẩm...");
        cboSanPham.setPrefWidth(250);

        TextField txtSoLo = new TextField();
        txtSoLo.setPromptText("VD: LO-ST25-001");

        DatePicker dpNgayThuHoach = new DatePicker();
        DatePicker dpNgaySanXuat = new DatePicker();
        DatePicker dpHanSuDung = new DatePicker();

        TextArea txtDiaChi = new TextArea();
        txtDiaChi.setPrefRowCount(2);
        txtDiaChi.setPromptText("Địa chỉ sản xuất...");

        TextField txtNongDan = new TextField();
        txtNongDan.setPromptText("Tên nông dân/cơ sở...");

        TextField txtChungNhan = new TextField();
        txtChungNhan.setPromptText("VD: VietGAP, GlobalGAP");

        // Fill data if editing
        if (txEdit != null) {
            cboSanPham.getItems().stream()
                .filter(sp -> sp.getMaSanPham() == txEdit.getMaSanPham())
                .findFirst()
                .ifPresent(cboSanPham::setValue);
            txtSoLo.setText(txEdit.getSoLo());
            dpNgayThuHoach.setValue(txEdit.getNgayThuHoach());
            dpNgaySanXuat.setValue(txEdit.getNgaySanXuat());
            dpHanSuDung.setValue(txEdit.getHanSuDung());
            txtDiaChi.setText(txEdit.getDiaChiSanXuat());
            txtNongDan.setText(txEdit.getTenNongDan());
            txtChungNhan.setText(txEdit.getChungNhan());
        }

        // Layout
        int row = 0;
        grid.add(new Label("Sản phẩm: *"), 0, row);
        grid.add(cboSanPham, 1, row++);

        grid.add(new Label("Số lô: *"), 0, row);
        grid.add(txtSoLo, 1, row++);

        grid.add(new Label("Ngày thu hoạch:"), 0, row);
        grid.add(dpNgayThuHoach, 1, row++);

        grid.add(new Label("Địa chỉ sản xuất:"), 0, row);
        grid.add(txtDiaChi, 1, row++);

        grid.add(new Label("Nông dân/Cơ sở:"), 0, row);
        grid.add(txtNongDan, 1, row++);

        grid.add(new Label("Chứng nhận:"), 0, row);
        grid.add(txtChungNhan, 1, row++);

        grid.add(new Label("Ngày sản xuất:"), 0, row);
        grid.add(dpNgaySanXuat, 1, row++);

        grid.add(new Label("Hạn sử dụng:"), 0, row);
        grid.add(dpHanSuDung, 1, row++);

        // Buttons
        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button btnLuu = new Button("💾 Lưu");
        Button btnHuy = new Button("❌ Hủy");

        btnLuu.getStyleClass().add("btn-primary");
        btnHuy.getStyleClass().add("btn-secondary");

        btnLuu.setOnAction(e -> {
            if (cboSanPham.getValue() == null || txtSoLo.getText().trim().isEmpty()) {
                hienThiThongBao("Lỗi", "Vui lòng nhập đầy đủ thông tin bắt buộc!", Alert.AlertType.ERROR);
                return;
            }

            TruyXuatNguonGoc tx = txEdit != null ? txEdit : new TruyXuatNguonGoc();
            tx.setMaSanPham(cboSanPham.getValue().getMaSanPham());
            tx.setSoLo(txtSoLo.getText().trim());
            tx.setNgayThuHoach(dpNgayThuHoach.getValue());
            tx.setDiaChiSanXuat(txtDiaChi.getText().trim());
            tx.setTenNongDan(txtNongDan.getText().trim());
            tx.setChungNhan(txtChungNhan.getText().trim());
            tx.setNgaySanXuat(dpNgaySanXuat.getValue());
            tx.setHanSuDung(dpHanSuDung.getValue());

            boolean success = txEdit == null ? truyXuatDAO.them(tx) : truyXuatDAO.capNhat(tx);

            if (success) {
                hienThiThongBao("Thành công", 
                    txEdit == null ? "Thêm truy xuất thành công!" : "Cập nhật thành công!", 
                    Alert.AlertType.INFORMATION);
                taiDuLieu();
                dialog.close();
            } else {
                hienThiThongBao("Lỗi", "Không thể lưu dữ liệu!", Alert.AlertType.ERROR);
            }
        });

        btnHuy.setOnAction(e -> dialog.close());
        buttons.getChildren().addAll(btnLuu, btnHuy);

        VBox root = new VBox(20, grid, buttons);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.show();
    }

    private void xoaTruyXuat(TruyXuatNguonGoc tx) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc muốn xóa truy xuất này?");
        alert.setContentText("Số lô: " + tx.getSoLo() + " - " + tx.getTenNongDan());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (truyXuatDAO.xoa(tx.getMaTruyXuat())) {
                hienThiThongBao("Thành công", "Xóa thành công!", Alert.AlertType.INFORMATION);
                taiDuLieu();
            } else {
                hienThiThongBao("Lỗi", "Không thể xóa!", Alert.AlertType.ERROR);
            }
        }
    }

    private void hienThiThongBao(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
