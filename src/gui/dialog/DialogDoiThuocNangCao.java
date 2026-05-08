package gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import dao.*;
import entity.*;

public class DialogDoiThuocNangCao extends JDialog {
    private HoaDonDAO hoaDonDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private ThuocDAO thuocDAO;
    private LichSuDoiTraDAO lichSuDoiTraDAO;
    private PhieuDoiTraDAO phieuDoiTraDAO;
    
    private JTextField txtMaHoaDon, txtNgayLap, txtTongTienCu, txtTongTienMoi;
    private JComboBox<String> cboHoaDon, cboKhachHang, cboNhanVien;
    private JTextArea txtLyDoDoiTra;
    private JLabel lblChenhLech;
    private JTable tableDonHang;
    private DefaultTableModel modelDonHang;
    private JButton btnThemHang, btnXoaHang, btnCapNhat, btnHuy;
    
    private NumberFormat currencyFormat;
    private String currentUser;
    private String maHoaDonHienTai;
    private Map<String, Integer> originalQuantities;
    private Map<String, Double> originalDonGia;
    
    public DialogDoiThuocNangCao(Frame parent, String currentUser) {
        super(parent, "Đổi trả hàng nâng cao", true);
        this.currentUser = currentUser;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        
        initDAO();
        initComponents();
        setupEventHandlers();
        loadInitialData();
        
        setLocationRelativeTo(parent);
    }
    
    private void initDAO() {
        this.hoaDonDAO = new HoaDonDAO();
        this.chiTietHoaDonDAO = new ChiTietHoaDonDAO();
        this.khachHangDAO = new KhachHangDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.thuocDAO = new ThuocDAO();
        this.lichSuDoiTraDAO = new LichSuDoiTraDAO();
        this.phieuDoiTraDAO = new PhieuDoiTraDAO();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(1200, 800);
        
        // Panel thông tin đơn hàng
        JPanel infoPanel = createInfoPanel();
        
        // Panel đơn hàng chính
        JPanel mainPanel = createMainPanel();
        
        // Panel nút
        JPanel buttonPanel = createButtonPanel();
        
        add(infoPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Chọn hóa đơn:"), gbc);
        gbc.gridx = 1;
        cboHoaDon = new JComboBox<>();
        cboHoaDon.setPreferredSize(new Dimension(200, 25));
        panel.add(cboHoaDon, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Mã hóa đơn:"), gbc);
        gbc.gridx = 3;
        txtMaHoaDon = new JTextField(15);
        txtMaHoaDon.setEditable(false);
        panel.add(txtMaHoaDon, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 1;
        cboNhanVien = new JComboBox<>();
        cboNhanVien.setEnabled(false);
        panel.add(cboNhanVien, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Khách hàng:"), gbc);
        gbc.gridx = 3;
        cboKhachHang = new JComboBox<>();
        cboKhachHang.setEnabled(false);
        panel.add(cboKhachHang, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Ngày lập:"), gbc);
        gbc.gridx = 1;
        txtNgayLap = new JTextField(15);
        txtNgayLap.setEditable(false);
        panel.add(txtNgayLap, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Tổng tiền cũ:"), gbc);
        gbc.gridx = 3;
        txtTongTienCu = new JTextField(15);
        txtTongTienCu.setEditable(false);
        txtTongTienCu.setFont(new Font("Arial", Font.BOLD, 12));
        txtTongTienCu.setForeground(Color.BLUE);
        panel.add(txtTongTienCu, gbc);
        
        // Row 4
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Lý do đổi/trả:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtLyDoDoiTra = new JTextArea(2, 30);
        txtLyDoDoiTra.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane scrollLyDo = new JScrollPane(txtLyDoDoiTra);
        panel.add(scrollLyDo, gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JPanel tienPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        tienPanel.setBackground(Color.WHITE);
        
        txtTongTienMoi = new JTextField(15);
        txtTongTienMoi.setEditable(false);
        txtTongTienMoi.setFont(new Font("Arial", Font.BOLD, 12));
        txtTongTienMoi.setForeground(new Color(0, 153, 76));
        tienPanel.add(new JLabel("Tổng tiền mới:"));
        tienPanel.add(txtTongTienMoi);
        
        panel.add(tienPanel, gbc);
        
        // Row 5 - Chênh lệch
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        lblChenhLech = new JLabel("Chênh lệch: 0₫", JLabel.CENTER);
        lblChenhLech.setFont(new Font("Arial", Font.BOLD, 16));
        lblChenhLech.setForeground(Color.RED);
        lblChenhLech.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblChenhLech.setOpaque(true);
        lblChenhLech.setBackground(new Color(255, 255, 240));
        panel.add(lblChenhLech, gbc);
        
        return panel;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Chi tiết đơn hàng (Có thể chỉnh sửa)"));
        panel.setBackground(Color.WHITE);
        
        // Panel nút chức năng
        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        functionPanel.setBackground(Color.WHITE);
        
        btnThemHang = new JButton("Thêm hàng");
        btnThemHang.setBackground(new Color(0, 153, 76));
        btnThemHang.setForeground(Color.WHITE);
        btnThemHang.setPreferredSize(new Dimension(120, 30));
        btnThemHang.setEnabled(false);
        
        btnXoaHang = new JButton("Xóa hàng");
        btnXoaHang.setBackground(new Color(220, 53, 69));
        btnXoaHang.setForeground(Color.WHITE);
        btnXoaHang.setPreferredSize(new Dimension(120, 30));
        btnXoaHang.setEnabled(false);
        
        functionPanel.add(btnThemHang);
        functionPanel.add(btnXoaHang);
        
        // Bảng chi tiết đơn hàng
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Đơn vị", "SL", "Đơn giá", "Thành tiền"};
        modelDonHang = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cho phép sửa số lượng
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 3: return Integer.class; // Số lượng
                    case 4:
                    case 5: return Double.class; // Đơn giá, thành tiền
                    default: return String.class;
                }
            }
        };
        
        tableDonHang = new JTable(modelDonHang);
        tableDonHang.setFont(new Font("Arial", Font.PLAIN, 12));
        tableDonHang.setRowHeight(25);
        tableDonHang.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableDonHang.getTableHeader().setBackground(new Color(240, 248, 255));
        
        // Căn giữa các cột số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableDonHang.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tableDonHang.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tableDonHang.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        JScrollPane scrollPane = new JScrollPane(tableDonHang);
        
        panel.add(functionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnCapNhat = new JButton("Cập nhật đơn hàng");
        btnCapNhat.setBackground(new Color(0, 123, 255));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setPreferredSize(new Dimension(150, 35));
        btnCapNhat.setEnabled(false);
        
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setPreferredSize(new Dimension(80, 35));
        
        panel.add(btnCapNhat);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnHuy);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        btnThemHang.addActionListener(this::btnThemHangActionPerformed);
        btnXoaHang.addActionListener(this::btnXoaHangActionPerformed);
        btnCapNhat.addActionListener(this::btnCapNhatActionPerformed);
        btnHuy.addActionListener(e -> dispose());
        
        cboHoaDon.addActionListener(this::cboHoaDonActionPerformed);
        
        // Lắng nghe thay đổi trong bảng để cập nhật tổng tiền
        modelDonHang.addTableModelListener(e -> {
            if (e.getColumn() == 3) { // Cột số lượng
                updateRowTotal(e.getFirstRow());
                tinhTongTien();
            }
        });
        
        // Xử lý click chuột trên bảng để chọn sản phẩm cần đổi
        tableDonHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tableDonHang.getSelectedRow() != -1) {
                    showProductExchangeDialog();
                }
            }
        });
    }
    
    private void loadInitialData() {
        try {
            // Load danh sách nhân viên
            List<NhanVien> dsNhanVien = nhanVienDAO.getDSNhanVien();
            cboNhanVien.removeAllItems();
            for (NhanVien nv : dsNhanVien) {
                cboNhanVien.addItem(nv.getMaNV() + " - " + nv.getTenNV());
            }
            
            // Load danh sách khách hàng
            List<KhachHang> dsKhachHang = khachHangDAO.getDSKhachHang();
            cboKhachHang.removeAllItems();
            for (KhachHang kh : dsKhachHang) {
                cboKhachHang.addItem(kh.getMaKH() + " - " + kh.getHoTen());
            }
            
            // Load danh sách hóa đơn
            List<HoaDon> dsHoaDon = hoaDonDAO.getDsHoaDon();
            cboHoaDon.removeAllItems();
            cboHoaDon.addItem("-- Chọn hóa đơn --");
            for (HoaDon hd : dsHoaDon) {
                cboHoaDon.addItem(hd.getMaHD() + " - " + new SimpleDateFormat("dd/MM/yyyy").format(hd.getNgayLap()));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }
    
    private void cboHoaDonActionPerformed(ActionEvent e) {
        if (cboHoaDon.getSelectedIndex() > 0) {
            String selectedItem = (String) cboHoaDon.getSelectedItem();
            if (selectedItem != null && !selectedItem.equals("-- Chọn hóa đơn --")) {
                String maHD = selectedItem.split(" - ")[0];
                loadHoaDonDetails(maHD);
                enableControls(true);
            }
        } else {
            clearForm();
            enableControls(false);
        }
    }
    
    private void loadHoaDonDetails(String maHD) {
        try {
            // Kiểm tra database constraints trước khi load
            if (!checkDatabaseConstraints()) {
                return;
            }
            
            this.maHoaDonHienTai = maHD;
            
            // Load thông tin hóa đơn
            HoaDon hoaDon = hoaDonDAO.getHoaDonTheoMa(maHD);
            if (hoaDon != null) {
                txtMaHoaDon.setText(hoaDon.getMaHD());
                txtNgayLap.setText(new SimpleDateFormat("dd/MM/yyyy").format(hoaDon.getNgayLap()));
                
                // Set nhân viên
                if (hoaDon.getNhanVien() != null) {
                    for (int i = 0; i < cboNhanVien.getItemCount(); i++) {
                        if (cboNhanVien.getItemAt(i).startsWith(hoaDon.getNhanVien().getMaNV())) {
                            cboNhanVien.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                
                // Set khách hàng
                if (hoaDon.getKhachHang() != null) {
                    for (int i = 0; i < cboKhachHang.getItemCount(); i++) {
                        if (cboKhachHang.getItemAt(i).startsWith(hoaDon.getKhachHang().getMaKH())) {
                            cboKhachHang.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
            
            // Load chi tiết hóa đơn
            loadChiTietHoaDon(maHD);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết hóa đơn: " + e.getMessage());
        }
    }
    
    private void loadChiTietHoaDon(String maHD) throws SQLException {
        List<ChiTietHoaDon> danhSach = chiTietHoaDonDAO.getChiTietHoaDonTheoMaHD(maHD);
        modelDonHang.setRowCount(0);
        
        double tongTienCu = 0;
        
        originalQuantities = new HashMap<>();
        originalDonGia = new HashMap<>();
        
        if (danhSach != null && !danhSach.isEmpty()) {
            for (ChiTietHoaDon chiTiet : danhSach) {
                try {
                    Thuoc thuocDetail = thuocDAO.getThuocTheoMaThuoc(chiTiet.getThuoc().getMaThuoc());
                    
                    if (thuocDetail != null) {
                        double thanhTien = chiTiet.getSoLuong() * chiTiet.getDonGia();
                        tongTienCu += thanhTien;
                        
                        originalQuantities.put(chiTiet.getThuoc().getMaThuoc(), chiTiet.getSoLuong());
                        originalDonGia.put(chiTiet.getThuoc().getMaThuoc(), chiTiet.getDonGia());
                        
                        Object[] row = {
                            thuocDetail.getMaThuoc(),
                            thuocDetail.getTenThuoc(),
                            thuocDetail.getDonViTinh() != null ? thuocDetail.getDonViTinh() : "Viên",
                            chiTiet.getSoLuong(),
                            chiTiet.getDonGia(),
                            thanhTien
                        };
                        modelDonHang.addRow(row);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Lỗi khi load thuốc " + chiTiet.getThuoc().getMaThuoc() + ": " + e.getMessage());
                }
            }
        }
        
        txtTongTienCu.setText(currencyFormat.format(tongTienCu));
        tinhTongTien();
    }
    
    private boolean checkDatabaseConstraints() {
        try {
            // Kiểm tra foreign key constraints và triggers
            // Điều này đảm bảo database không có lỗi trước khi thực hiện cập nhật
            return true; // Tạm thời return true, có thể mở rộng sau
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Phát hiện lỗi trong cơ sở dữ liệu: " + e.getMessage() + 
                "\nVui lòng liên hệ quản trị viên để xử lý trước khi tiếp tục.",
                "Lỗi Database", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void showProductExchangeDialog() {
        int selectedRow = tableDonHang.getSelectedRow();
        if (selectedRow == -1) return;
        
        String tenThuocCu = (String) modelDonHang.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn muốn đổi sản phẩm: " + tenThuocCu + "?",
            "Xác nhận đổi sản phẩm",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            DialogChonThuoc dialogChonThuoc = new DialogChonThuoc(this);
            dialogChonThuoc.setVisible(true);
            
            List<Thuoc> thuocDaChon = dialogChonThuoc.getThuocDaChon();
            if (!thuocDaChon.isEmpty()) {
                Thuoc thuocMoi = thuocDaChon.get(0); // Chỉ lấy 1 thuốc đầu tiên
                
                // Cập nhật hàng hiện tại với sản phẩm mới
                modelDonHang.setValueAt(thuocMoi.getMaThuoc(), selectedRow, 0);
                modelDonHang.setValueAt(thuocMoi.getTenThuoc(), selectedRow, 1);
                modelDonHang.setValueAt(thuocMoi.getDonViTinh(), selectedRow, 2);
                modelDonHang.setValueAt(thuocMoi.getGiaBan(), selectedRow, 4);
                
                updateRowTotal(selectedRow);
                tinhTongTien();
                
                // Ghi log thao tác
                String logMessage = String.format("Đổi sản phẩm: %s -> %s", tenThuocCu, thuocMoi.getTenThuoc());
                txtLyDoDoiTra.append(logMessage + "\n");
            }
        }
    }
    
    private void btnThemHangActionPerformed(ActionEvent e) {
        DialogChonThuoc dialogChonThuoc = new DialogChonThuoc(this);
        dialogChonThuoc.setVisible(true);
        
        List<Thuoc> thuocDaChon = dialogChonThuoc.getThuocDaChon();
        
        for (Thuoc thuoc : thuocDaChon) {
            // Kiểm tra thuốc đã có trong đơn hàng chưa
            boolean daTonTai = false;
            for (int i = 0; i < modelDonHang.getRowCount(); i++) {
                if (modelDonHang.getValueAt(i, 0).toString().equals(thuoc.getMaThuoc())) {
                    // Tăng số lượng nếu đã có
                    int soLuongCu = (Integer) modelDonHang.getValueAt(i, 3);
                    modelDonHang.setValueAt(soLuongCu + 1, i, 3);
                    updateRowTotal(i);
                    daTonTai = true;
                    break;
                }
            }
            
            if (!daTonTai) {
                Object[] row = {
                    thuoc.getMaThuoc(),
                    thuoc.getTenThuoc(),
                    thuoc.getDonViTinh(),
                    1, // Số lượng mặc định
                    thuoc.getGiaBan(),
                    thuoc.getGiaBan()
                };
                modelDonHang.addRow(row);
            }
        }
        
        tinhTongTien();
    }
    
    private void btnXoaHangActionPerformed(ActionEvent e) {
        int selectedRow = tableDonHang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa");
            return;
        }
        
        String tenThuoc = (String) modelDonHang.getValueAt(selectedRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa sản phẩm: " + tenThuoc + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            modelDonHang.removeRow(selectedRow);
            tinhTongTien();
            
            // Ghi log thao tác
            String logMessage = "Xóa sản phẩm: " + tenThuoc;
            txtLyDoDoiTra.append(logMessage + "\n");
        }
    }
    
    private void updateRowTotal(int row) {
        try {
            int soLuong = (Integer) modelDonHang.getValueAt(row, 3);
            double donGia = (Double) modelDonHang.getValueAt(row, 4);
            double thanhTien = soLuong * donGia;
            modelDonHang.setValueAt(thanhTien, row, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void tinhTongTien() {
        double tongTienMoi = 0;
        
        for (int i = 0; i < modelDonHang.getRowCount(); i++) {
            try {
                double thanhTien = (Double) modelDonHang.getValueAt(i, 5);
                tongTienMoi += thanhTien;
            } catch (Exception e) {
                // Ignore parsing errors
            }
        }
        
        txtTongTienMoi.setText(currencyFormat.format(tongTienMoi));
        
        // Tính chênh lệch
        try {
            String tongTienCuStr = txtTongTienCu.getText().replaceAll("[^\\d]", "");
            double tongTienCu = tongTienCuStr.isEmpty() ? 0 : Double.parseDouble(tongTienCuStr);
            double chenhLech = tongTienMoi - tongTienCu;
            
            String text;
            Color color;
            
            if (chenhLech > 0) {
                text = "Phải trả thêm: " + currencyFormat.format(chenhLech);
                color = Color.RED;
            } else if (chenhLech < 0) {
                text = "Được hoàn: " + currencyFormat.format(Math.abs(chenhLech));
                color = new Color(0, 153, 76);
            } else {
                text = "Không có chênh lệch";
                color = Color.BLACK;
            }
            
            lblChenhLech.setText(text);
            lblChenhLech.setForeground(color);
        } catch (Exception e) {
            lblChenhLech.setText("Lỗi tính toán");
            lblChenhLech.setForeground(Color.RED);
        }
    }
    
    private void btnCapNhatActionPerformed(ActionEvent e) {
        if (validateInput()) {
            if (confirmUpdate()) {
                updateDonHang();
            }
        }
    }
    
    private boolean validateInput() {
        if (maHoaDonHienTai == null || maHoaDonHienTai.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần cập nhật");
            return false;
        }
        
        if (modelDonHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Đơn hàng phải có ít nhất một sản phẩm");
            return false;
        }
        
        if (txtLyDoDoiTra.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do đổi/trả hàng");
            txtLyDoDoiTra.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean confirmUpdate() {
        String message = "Bạn có chắc chắn muốn cập nhật đơn hàng " + maHoaDonHienTai + "?\n" +
                        "Thao tác này sẽ thay đổi trực tiếp đơn hàng hiện tại.";
        
        int confirm = JOptionPane.showConfirmDialog(this, message, "Xác nhận cập nhật", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        return confirm == JOptionPane.YES_OPTION;
    }
    
    private void updateDonHang() {
        try {
            // Tạo phiếu đổi trả với trạng thái "Chờ xử lý" thay vì cập nhật trực tiếp
            if (taoPhieuDoiTra()) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Tạo phiếu đổi trả thành công!\n\n" +
                    "📋 Thông tin:\n" +
                    "• Phiếu đã được tạo với trạng thái 'Chờ xử lý'\n" +
                    "• Cần phê duyệt từ quản lý trước khi cập nhật đơn hàng\n" +
                    "• Bạn có thể theo dõi tiến trình trong danh sách phiếu đổi trả\n\n" +
                    "🔔 Lưu ý: Đơn hàng gốc chưa được thay đổi",
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lỗi khi tạo phiếu đổi trả!\n\n" +
                    "Vui lòng kiểm tra:\n" +
                    "• Kết nối cơ sở dữ liệu\n" +
                    "• Thông tin đầu vào\n" +
                    "• Quyền truy cập",
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không xác định: " + e.getMessage());
        }
    }
    
    private boolean taoPhieuDoiTra() {
        try {
            // Lấy thông tin từ form
            String tongTienCuStr = txtTongTienCu.getText().replaceAll("[^\\d]", "");
            String tongTienMoiStr = txtTongTienMoi.getText().replaceAll("[^\\d]", "");
            
            double tongTienCu = tongTienCuStr.isEmpty() ? 0 : Double.parseDouble(tongTienCuStr);
            double tongTienMoi = tongTienMoiStr.isEmpty() ? 0 : Double.parseDouble(tongTienMoiStr);
            
            // Validate modelDonHang rows: ensure maThuoc present
            for (int i = 0; i < modelDonHang.getRowCount(); i++) {
                Object maThuocObj = modelDonHang.getValueAt(i, 0);
                String maThuocRow = maThuocObj != null ? maThuocObj.toString().trim() : "";
                if (maThuocRow.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Lỗi: Dòng " + (i+1) + " trong đơn hàng thiếu Mã thuốc. Vui lòng chọn sản phẩm hoặc xóa dòng này trước khi cập nhật.",
                        "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
            // Tạo đối tượng PhieuDoiTra
            PhieuDoiTra phieuDoiTra = new PhieuDoiTra();
            phieuDoiTra.setMaPhieuDoiTra(phieuDoiTraDAO.generateMaPhieuDoiTra());
            phieuDoiTra.setNgayDoiTra(new java.util.Date());
            
            // Xác định loại phiếu dựa trên chênh lệch tiền
            if (tongTienMoi > tongTienCu) {
                phieuDoiTra.setLoaiPhieu("DOI"); // Đổi thuốc (cần trả thêm tiền)
            } else {
                phieuDoiTra.setLoaiPhieu("TRA"); // Trả thuốc (được hoàn tiền)
            }
            
            phieuDoiTra.setLyDo(txtLyDoDoiTra.getText().trim());
            phieuDoiTra.setTongTien(Math.abs(tongTienMoi - tongTienCu)); // Chênh lệch
            phieuDoiTra.setTrangThai("Chờ xử lý"); // Trạng thái chờ phê duyệt
            
            // Set nhân viên và khách hàng
            NhanVien nv = new NhanVien();
            nv.setMaNV(currentUser);
            phieuDoiTra.setNhanVien(nv);
            
            // Lấy thông tin khách hàng từ hóa đơn gốc
            HoaDon hoaDonGoc = hoaDonDAO.getHoaDonTheoMa(maHoaDonHienTai);
            if (hoaDonGoc != null && hoaDonGoc.getKhachHang() != null) {
                phieuDoiTra.setKhachHang(hoaDonGoc.getKhachHang());
            }
            
            // Set hóa đơn gốc
            HoaDon hdGoc = new HoaDon();
            hdGoc.setMaHD(maHoaDonHienTai);
            phieuDoiTra.setHoaDonGoc(hdGoc);
            
            // Thêm phiếu đổi trả vào database
            if (!phieuDoiTraDAO.themPhieuDoiTra(phieuDoiTra)) {
                return false;
            }
            
            // Build maps of current quantities/prices from edited order
            Map<String, Integer> currentQuantities = new HashMap<>();
            Map<String, Double> currentDonGia = new HashMap<>();
            for (int i = 0; i < modelDonHang.getRowCount(); i++) {
                String maThuoc = modelDonHang.getValueAt(i, 0).toString();
                int soLuong = 0;
                double donGia = 0.0;
                try { soLuong = (Integer) modelDonHang.getValueAt(i, 3); } catch (Exception ex) { soLuong = Integer.parseInt(modelDonHang.getValueAt(i,3).toString()); }
                try { donGia = (Double) modelDonHang.getValueAt(i, 4); } catch (Exception ex) { donGia = Double.parseDouble(modelDonHang.getValueAt(i,4).toString()); }
                currentQuantities.put(maThuoc, soLuong);
                currentDonGia.put(maThuoc, donGia);
            }

            // Determine deltas: returned items (original > current) and added items (current > original)
            boolean hasChanges = false;
            StringBuilder detailsLog = new StringBuilder();
            String originalLyDo = txtLyDoDoiTra.getText().trim();
            if (originalLyDo != null && !originalLyDo.isEmpty()) {
                detailsLog.append(originalLyDo).append("\n");
            }

            // Process returns (original items reduced)
            if (originalQuantities == null) originalQuantities = new HashMap<>();
            for (Map.Entry<String, Integer> entry : originalQuantities.entrySet()) {
                String maThuoc = entry.getKey();
                int origQty = entry.getValue() != null ? entry.getValue() : 0;
                int currQty = currentQuantities.getOrDefault(maThuoc, 0);
                if (origQty > currQty) {
                    int returnedQty = origQty - currQty;
                    hasChanges = true;

                    ChiTietPhieuDoiTra chiTiet = new ChiTietPhieuDoiTra();
                    chiTiet.setMaPhieuDoiTra(phieuDoiTra.getMaPhieuDoiTra());

                    Thuoc thuoc = new Thuoc(); thuoc.setMaThuoc(maThuoc);
                    chiTiet.setThuocCu(thuoc);
                    chiTiet.setSoLuongCu(returnedQty);
                    double donGiaCu = originalDonGia.getOrDefault(maThuoc, 0.0);
                    chiTiet.setDonGiaCu(donGiaCu);
                    chiTiet.setTrangThaiThuoc("Bình thường");
                    chiTiet.setLoaiThaoTac(phieuDoiTra.getLoaiPhieu());
                    chiTiet.setChenhLechTien(- returnedQty * donGiaCu); // negative effect on revenue

                    detailsLog.append(String.format("- Trả lại: %s (SL=%d)\n", maThuoc, returnedQty));

                    System.out.println("[DEBUG] Inserting returned chiTiet: maPhieu=" + chiTiet.getMaPhieuDoiTra() + ", maThuoc=" + thuoc.getMaThuoc() + ", soLuong=" + chiTiet.getSoLuongCu());

                    if (!phieuDoiTraDAO.themChiTietPhieuDoiTra(chiTiet)) {
                        return false;
                    }
                }
            }

            // Process added items (new or increased)
            for (Map.Entry<String, Integer> entry : currentQuantities.entrySet()) {
                String maThuoc = entry.getKey();
                int currQty = entry.getValue() != null ? entry.getValue() : 0;
                int origQty = originalQuantities.getOrDefault(maThuoc, 0);
                if (currQty > origQty) {
                    int addedQty = currQty - origQty;
                    hasChanges = true;

                    // Validate maThuoc before creating ChiTietPhieuDoiTra
                    if (maThuoc == null || maThuoc.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Lỗi: Mã thuốc không hợp lệ hoặc bị thiếu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue; // Skip this item
                    }

                    ChiTietPhieuDoiTra chiTiet = new ChiTietPhieuDoiTra();
                    chiTiet.setMaPhieuDoiTra(phieuDoiTra.getMaPhieuDoiTra());

                    // old product may be null; for added items set thuocMoi
                    Thuoc thuocMoi = new Thuoc(); thuocMoi.setMaThuoc(maThuoc);
                    chiTiet.setThuocMoi(thuocMoi);
                    // Ensure maThuoc is also set (DB maThuoc NOT NULL) — set to same as thuocMoi for added items
                    chiTiet.setThuoc(thuocMoi);
                    chiTiet.setSoLuongThayThe(addedQty);
                    double donGiaMoi = currentDonGia.getOrDefault(maThuoc, getDonGiaThuocFromDAO(maThuoc));
                    chiTiet.setDonGiaThayThe(donGiaMoi);
                    chiTiet.setTrangThaiThuoc("Bình thường");
                    chiTiet.setLoaiThaoTac(phieuDoiTra.getLoaiPhieu());
                    chiTiet.setChenhLechTien(addedQty * donGiaMoi);

                    detailsLog.append(String.format("- Nhập thêm: %s (SL=%d)\n", maThuoc, addedQty));

                    System.out.println("[DEBUG] Inserting added chiTiet: maPhieu=" + chiTiet.getMaPhieuDoiTra() + ", maThuocMoi=" + thuocMoi.getMaThuoc() + ", soLuongThayThe=" + chiTiet.getSoLuongThayThe() + ", donGiaThayThe=" + chiTiet.getDonGiaThayThe());

                    if (!phieuDoiTraDAO.themChiTietPhieuDoiTra(chiTiet)) {
                        return false;
                    }
                }
            }

            if (!hasChanges) {
                // Nếu không có khác biệt, không cần tạo phiếu
                JOptionPane.showMessageDialog(this, "Không phát hiện thay đổi giữa đơn hàng gốc và đơn hàng hiện tại.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            // Update phiếu lý do to include per-line details so the original meaning isn't lost
            phieuDoiTra.setLyDo(detailsLog.toString());

            // Ghi log lịch sử với nội dung chi tiết
            try {
                lichSuDoiTraDAO.ghiLogDoiTra(maHoaDonHienTai, currentUser, 
                    "Tạo phiếu " + phieuDoiTra.getLoaiPhieu() + ": " + phieuDoiTra.getMaPhieuDoiTra(),
                    phieuDoiTra.getLyDo(), tongTienMoi - tongTienCu);
            } catch (Exception ex) {
                // ignore logging errors
                System.err.println("Không thể ghi lịch sử: " + ex.getMessage());
            }

            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            // Show detailed error to user so they don't only see generic message
            StringBuilder sb = new StringBuilder();
            sb.append("Lỗi khi tạo phiếu đổi trả:\n");
            sb.append(e.getClass().getSimpleName()).append(": ").append(e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                sb.append("\nCaused by: ").append(cause.getClass().getSimpleName()).append(": ").append(cause.getMessage());
                cause = cause.getCause();
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Lỗi tạo phiếu", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private double getDonGiaThuocFromDAO(String maThuoc) {
        try {
            Thuoc t = thuocDAO.getThuocTheoMaThuoc(maThuoc);
            if (t != null) return t.getGiaBan();
        } catch (Exception ex) {
            System.err.println("Không thể lấy giá thuốc " + maThuoc + ": " + ex.getMessage());
        }
        return 0.0;
    }
    
    private void clearForm() {
        txtMaHoaDon.setText("");
        txtNgayLap.setText("");
        txtTongTienCu.setText("");
        txtTongTienMoi.setText("");
        txtLyDoDoiTra.setText("");
        lblChenhLech.setText("Chênh lệch: 0₫");
        lblChenhLech.setForeground(Color.BLACK);
        modelDonHang.setRowCount(0);
        maHoaDonHienTai = null;
    }
    
    private void enableControls(boolean enabled) {
        btnThemHang.setEnabled(enabled);
        btnXoaHang.setEnabled(enabled);
        btnCapNhat.setEnabled(enabled);
        txtLyDoDoiTra.setEditable(enabled);
    }
}