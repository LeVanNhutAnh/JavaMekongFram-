package com.mekongfarm.service;

import com.mekongfarm.dao.SanPhamDAO;
import com.mekongfarm.dao.KhachHangDAO;
import com.mekongfarm.model.SanPham;
import com.mekongfarm.model.KhachHang;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.geometry.Bounds;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search Autocomplete Service
 * Provides autocomplete functionality for search fields
 */
public class SearchAutocompleteService {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    /**
     * Thêm autocomplete cho TextField tìm sản phẩm
     */
    public void setupProductAutocomplete(TextField textField, java.util.function.Consumer<SanPham> onSelect) {
        Popup popup = new Popup();
        ListView<SanPham> listView = new ListView<>();
        listView.setPrefWidth(300);
        listView.setPrefHeight(200);
        listView.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(SanPham item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaSP() + " - " + item.getTenSanPham());
            }
        });

        listView.setOnMouseClicked(e -> {
            SanPham selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                textField.setText(selected.getTenSanPham());
                popup.hide();
                if (onSelect != null) {
                    onSelect.accept(selected);
                }
            }
        });

        popup.getContent().add(listView);
        popup.setAutoHide(true);

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.length() < 2) {
                popup.hide();
                return;
            }

            List<SanPham> results = sanPhamDAO.layTatCa().stream()
                    .filter(sp -> sp.getTenSanPham().toLowerCase().contains(newVal.toLowerCase()) ||
                            sp.getMaSP().toLowerCase().contains(newVal.toLowerCase()))
                    .limit(10)
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                popup.hide();
                return;
            }

            listView.getItems().setAll(results);

            if (!popup.isShowing()) {
                Window window = textField.getScene().getWindow();
                Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
                popup.show(window, bounds.getMinX(), bounds.getMaxY());
            }
        });

        textField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused)
                popup.hide();
        });
    }

    /**
     * Thêm autocomplete cho TextField tìm khách hàng
     */
    public void setupCustomerAutocomplete(TextField textField, java.util.function.Consumer<KhachHang> onSelect) {
        Popup popup = new Popup();
        ListView<KhachHang> listView = new ListView<>();
        listView.setPrefWidth(300);
        listView.setPrefHeight(200);
        listView.setStyle("-fx-background-color: white; -fx-border-color: #ccc;");

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(KhachHang item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null
                        : item.getMaKH() + " - " + item.getHoTen() + " - " + item.getSoDienThoai());
            }
        });

        listView.setOnMouseClicked(e -> {
            KhachHang selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                textField.setText(selected.getHoTen());
                popup.hide();
                if (onSelect != null) {
                    onSelect.accept(selected);
                }
            }
        });

        popup.getContent().add(listView);
        popup.setAutoHide(true);

        textField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.length() < 2) {
                popup.hide();
                return;
            }

            List<KhachHang> results = khachHangDAO.layTatCa().stream()
                    .filter(kh -> kh.getHoTen().toLowerCase().contains(newVal.toLowerCase()) ||
                            kh.getSoDienThoai().contains(newVal) ||
                            kh.getMaKH().toLowerCase().contains(newVal.toLowerCase()))
                    .limit(10)
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                popup.hide();
                return;
            }

            listView.getItems().setAll(results);

            if (!popup.isShowing()) {
                Window window = textField.getScene().getWindow();
                Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
                popup.show(window, bounds.getMinX(), bounds.getMaxY());
            }
        });

        textField.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused)
                popup.hide();
        });
    }
}
