package gui.form;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import dao.PhieuDoiTraDAO;
import dao.NhanVienDAO;
import entity.PhieuDoiTra;
import gui.dialog.DialogDoiThuocNangCao;
import entity.NhanVien;

public class formQuanLyDoiTraThuoc extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private PhieuDoiTraDAO phieuDoiTraDAO;
    private NhanVienDAO nvDAO;
    
    // Components
    private JButton btnThem, btnXemChiTiet, btnCapNhatTrangThai, btnBaoCao;
    private JComboBox<String> cboLoaiPhieu, cboTrangThai, cboNhanVien;
    private JTextField txtTimKiem;
    private JLabel lblTongPhieu, lblTongTien, lblThongKe;
    
    private String currentUser = "NV00001"; // Tạm thời hardcode
    private NumberFormat currencyFormat;
    
    public formQuanLyDoiTraThuoc() {
        this.phieuDoiTraDAO = new PhieuDoiTraDAO();
        this.nvDAO = new NhanVienDAO();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        
        initComponents();
        setupEventListeners();
        // Chỉ load data sau khi tất cả components đã được khởi tạo
        SwingUtilities.invokeLater(() -> {
            loadData();
            updateThongKe();
        });
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Panel tiêu đề và bộ lọc
        JPanel topPanel = createTopPanel();
        
        // Panel chức năng
        JPanel functionPanel = createFunctionPanel();
        
        // Panel bảng dữ liệu
        JPanel tablePanel = createTablePanel();
        
        // Panel thống kê
        JPanel statsPanel = createStatsPanel();
        
        add(topPanel, BorderLayout.NORTH);
        add(functionPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐỔI - TRẢ THUỐC");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        titlePanel.add(lblTitle);
        
        // Panel bộ lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);
        
        // Loại phiếu
        filterPanel.add(new JLabel("Loại:"));
        cboLoaiPhieu = new JComboBox<>(new String[]{"Tất cả", "Đổi thuốc", "Trả thuốc"});
        // Tạm thời không thêm action listener để tránh gọi loadData() khi tableModel chưa được tạo
        filterPanel.add(cboLoaiPhieu);
        
        // Trạng thái
        filterPanel.add(new JLabel("Trạng thái:"));
        cboTrangThai = new JComboBox<>(new String[]{"Tất cả", "Chờ xử lý", "Đã xử lý", "Từ chối"});
        filterPanel.add(cboTrangThai);
        
        // Nhân viên
        filterPanel.add(new JLabel("Nhân viên:"));
        cboNhanVien = new JComboBox<>();
        loadNhanVienComboBox();
        filterPanel.add(cboNhanVien);
        
        // Tìm kiếm
        filterPanel.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(15);
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loadData();
                }
            }
        });
        filterPanel.add(txtTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.addActionListener(e -> loadData());
        filterPanel.add(btnTimKiem);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(filterPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventListeners() {
        // Thêm action listeners sau khi tất cả components đã được tạo
        cboLoaiPhieu.addActionListener(e -> loadData());
        cboTrangThai.addActionListener(e -> loadData());
        cboNhanVien.addActionListener(e -> loadData());
    }
    
    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Chức năng"));
        panel.setPreferredSize(new Dimension(180, 0));
        
        // Tạo phiếu đổi
        btnThem = new JButton("Tạo phiếu đổi/trả");
        btnThem.setBackground(new Color(46, 204, 113));
        btnThem.setForeground(Color.WHITE);
        btnThem.setFont(new Font("Arial", Font.BOLD, 12));
        btnThem.setMaximumSize(new Dimension(160, 40));
        btnThem.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnThem.addActionListener(this::btnThemActionPerformed);
        
        // Xem chi tiết
        btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setBackground(new Color(52, 152, 219));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFont(new Font("Arial", Font.BOLD, 12));
        btnXemChiTiet.setMaximumSize(new Dimension(160, 40));
        btnXemChiTiet.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnXemChiTiet.addActionListener(this::btnXemChiTietActionPerformed);
        
        // Cập nhật trạng thái
        btnCapNhatTrangThai = new JButton("Cập nhật trạng thái");
        btnCapNhatTrangThai.setBackground(new Color(230, 126, 34));
        btnCapNhatTrangThai.setForeground(Color.WHITE);
        btnCapNhatTrangThai.setFont(new Font("Arial", Font.BOLD, 12));
        btnCapNhatTrangThai.setMaximumSize(new Dimension(160, 40));
        btnCapNhatTrangThai.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCapNhatTrangThai.addActionListener(this::btnCapNhatTrangThaiActionPerformed);
        
        // Báo cáo
        btnBaoCao = new JButton("Báo cáo thống kê");
        btnBaoCao.setBackground(new Color(155, 89, 182));
        btnBaoCao.setForeground(Color.WHITE);
        btnBaoCao.setFont(new Font("Arial", Font.BOLD, 12));
        btnBaoCao.setMaximumSize(new Dimension(160, 40));
        btnBaoCao.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBaoCao.addActionListener(this::btnBaoCaoActionPerformed);
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnThem);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnXemChiTiet);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnCapNhatTrangThai);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnBaoCao);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // Bảng dữ liệu
        String[] columnNames = {
            "Mã phiếu", "Loại", "Ngày", "Nhân viên", "Khách hàng", 
            "Hóa đơn gốc", "Lý do", "Tổng tiền", "Trạng thái"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(236, 240, 241));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(189, 195, 199));
        
        // Custom renderer cho cột loại phiếu
        table.getColumnModel().getColumn(1).setCellRenderer(new LoaiPhieuRenderer());
        // Custom renderer cho cột trạng thái
        table.getColumnModel().getColumn(8).setCellRenderer(new TrangThaiRenderer());
        // Custom renderer cho cột tiền
        table.getColumnModel().getColumn(7).setCellRenderer(new TienRenderer());
        
        // Set width cho các cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã phiếu
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Loại
        table.getColumnModel().getColumn(2).setPreferredWidth(90);  // Ngày
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Nhân viên
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Khách hàng
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Hóa đơn gốc
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // Lý do
        table.getColumnModel().getColumn(7).setPreferredWidth(100); // Tổng tiền
        table.getColumnModel().getColumn(8).setPreferredWidth(100); // Trạng thái
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        lblTongPhieu = new JLabel("Tổng phiếu: 0");
        lblTongPhieu.setForeground(Color.WHITE);
        lblTongPhieu.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblTongTien = new JLabel("Tổng tiền: 0₫");
        lblTongTien.setForeground(Color.WHITE);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblThongKe = new JLabel("Đã xử lý: 0 | Chờ xử lý: 0");
        lblThongKe.setForeground(Color.WHITE);
        lblThongKe.setFont(new Font("Arial", Font.BOLD, 12));
        
        panel.add(lblTongPhieu);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(lblTongTien);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(lblThongKe);
        
        return panel;
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            
            // Sử dụng hệ thống PhieuDoiTra hiện có
            try {
                List<PhieuDoiTra> danhSach = getDanhSachTheoBoLoc();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                
                for (PhieuDoiTra phieu : danhSach) {
                    Object[] row = {
                        phieu.getMaPhieuDoiTra(),
                        phieu.getLoaiPhieu().equals("DOI") ? "Đổi thuốc" : "Trả thuốc",
                        sdf.format(phieu.getNgayDoiTra()),
                        phieu.getNhanVien() != null ? phieu.getNhanVien().getTenNV() : "",
                        phieu.getKhachHang() != null ? phieu.getKhachHang().getHoTen() : "",
                        phieu.getHoaDonGoc() != null ? phieu.getHoaDonGoc().getMaHD() : "",
                        phieu.getLyDo(),
                        phieu.getTongTien(),
                        phieu.getTrangThai()
                    };
                    tableModel.addRow(row);
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            }
            
            updateThongKe();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }
    
    private List<PhieuDoiTra> getDanhSachTheoBoLoc() throws SQLException {
        // Lấy tất cả phiếu hoặc theo loại
        String loaiPhieuSelected = (String) cboLoaiPhieu.getSelectedItem();
        List<PhieuDoiTra> danhSach;
        
        if ("Tất cả".equals(loaiPhieuSelected)) {
            danhSach = phieuDoiTraDAO.getDanhSachPhieuDoiTra();
        } else {
            String loai = loaiPhieuSelected.equals("Đổi thuốc") ? "DOI" : "TRA";
            danhSach = phieuDoiTraDAO.getDanhSachPhieuDoiTraTheoLoai(loai);
        }
        
        // Lọc theo trạng thái, nhân viên, tìm kiếm
        return danhSach.stream()
            .filter(this::filterByTrangThai)
            .filter(this::filterByNhanVien)
            .filter(this::filterByTimKiem)
            .collect(java.util.stream.Collectors.toList());
    }
    
    private boolean filterByTrangThai(PhieuDoiTra phieu) {
        String trangThaiSelected = (String) cboTrangThai.getSelectedItem();
        return "Tất cả".equals(trangThaiSelected) || phieu.getTrangThai().equals(trangThaiSelected);
    }
    
    private boolean filterByNhanVien(PhieuDoiTra phieu) {
        String nvSelected = (String) cboNhanVien.getSelectedItem();
        if ("Tất cả".equals(nvSelected) || nvSelected == null) {
            return true;
        }
        String maNV = nvSelected.split(" - ")[0];
        return phieu.getNhanVien() != null && phieu.getNhanVien().getMaNV().equals(maNV);
    }
    
    private boolean filterByTimKiem(PhieuDoiTra phieu) {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            return true;
        }
        return phieu.getMaPhieuDoiTra().toLowerCase().contains(keyword) ||
               (phieu.getHoaDonGoc() != null && phieu.getHoaDonGoc().getMaHD().toLowerCase().contains(keyword)) ||
               phieu.getLyDo().toLowerCase().contains(keyword);
    }
    
    private void updateThongKe() {
        try {
            int tongPhieu = tableModel.getRowCount();
            double tongTien = 0;
            int daXuLy = 0;
            int choXuLy = 0;
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                double tien = (Double) tableModel.getValueAt(i, 7);
                tongTien += tien;
                
                String trangThai = (String) tableModel.getValueAt(i, 8);
                if ("Đã xử lý".equals(trangThai)) {
                    daXuLy++;
                } else if ("Chờ xử lý".equals(trangThai)) {
                    choXuLy++;
                }
            }
            
            lblTongPhieu.setText("Tổng phiếu: " + tongPhieu);
            lblTongTien.setText("Tổng tiền: " + currencyFormat.format(tongTien));
            lblThongKe.setText("Đã xử lý: " + daXuLy + " | Chờ xử lý: " + choXuLy);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadNhanVienComboBox() {
        try {
            cboNhanVien.removeAllItems();
            cboNhanVien.addItem("Tất cả");
            
            List<NhanVien> dsNhanVien = nvDAO.getDSNhanVien();
            for (NhanVien nv : dsNhanVien) {
                cboNhanVien.addItem(nv.getMaNV() + " - " + nv.getTenNV());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Event handlers
    private void btnThemActionPerformed(ActionEvent e) {
        // Chỉ sử dụng chế độ nâng cao (cập nhật trực tiếp đơn hàng)
        String[] options = {"Đổi trả hàng nâng cao", "Hủy"};
        int choice = JOptionPane.showOptionDialog(this,
                "Hệ thống đã được nâng cấp!\n\n" +
                "✅ Chức năng 'Đổi trả hàng nâng cao':\n" +
                "• Cập nhật trực tiếp đơn hàng hiện tại\n" +
                "• Không tạo phiếu đổi trả riêng biệt\n" +
                "• Quy trình đơn giản và nhanh chóng\n\n" +
                "Bạn có muốn tiếp tục không?",
                "Đổi trả hàng",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
        
        try {
            if (choice == 0) { // Đổi trả nâng cao
                DialogDoiThuocNangCao dialog = new DialogDoiThuocNangCao(
                    (Frame) SwingUtilities.getWindowAncestor(this), 
                    currentUser
                );
                dialog.setVisible(true);
                // Sau khi dialog đóng, làm mới dữ liệu
                loadData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở dialog: " + ex.getMessage());
        }
    }
    
    private void btnXemChiTietActionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "📋 THÔNG BÁO HỆ THỐNG MỚI\n\n" +
                "Hệ thống đã được nâng cấp lên phiên bản đơn giản hóa!\n\n" +
                "✅ Thay đổi chính:\n" +
                "• Không còn tạo phiếu đổi trả riêng\n" +
                "• Cập nhật trực tiếp trên đơn hàng gốc\n" +
                "• Ghi log thay đổi tự động\n\n" +
                "🔍 Để xem lịch sử thay đổi:\n" +
                "• Sử dụng chức năng 'Đổi trả hàng nâng cao'\n" +
                "• Kiểm tra log trong hệ thống\n\n" +
                "📞 Hỗ trợ: Liên hệ quản trị viên nếu cần thiết",
                "Hệ thống mới",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void btnCapNhatTrangThaiActionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu để xử lý");
            return;
        }
        
        String maPhieu = (String) tableModel.getValueAt(selectedRow, 0);
        String loaiPhieu = (String) tableModel.getValueAt(selectedRow, 1);
        String trangThaiHienTai = (String) tableModel.getValueAt(selectedRow, 8);
        
        // Chỉ cho phép xử lý phiếu ở trạng thái "Chờ xử lý"
        if (!"Chờ xử lý".equals(trangThaiHienTai)) {
            JOptionPane.showMessageDialog(this, 
                "Chỉ có thể xử lý phiếu ở trạng thái 'Chờ xử lý'!\n" +
                "Trạng thái hiện tại: " + trangThaiHienTai);
            return;
        }
        
        // Hiển thị các lựa chọn xử lý
        String[] options;
        if ("Đổi thuốc".equals(loaiPhieu)) {
            options = new String[]{"✅ Duyệt và xử lý đổi thuốc", "❌ Từ chối phiếu", "🚫 Hủy"};
        } else {
            options = new String[]{"✅ Duyệt và xử lý trả thuốc", "❌ Từ chối phiếu", "🚫 Hủy"};
        }
        
        int choice = JOptionPane.showOptionDialog(this,
                "🔔 XỬ LÝ PHIẾU ĐỔI TRẢ\n\n" +
                "Mã phiếu: " + maPhieu + "\n" +
                "Loại: " + loaiPhieu + "\n" +
                "Trạng thái: " + trangThaiHienTai + "\n\n" +
                "Chọn hành động:",
                "Xử lý phiếu " + loaiPhieu.toLowerCase(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        
        try {
            if (choice == 0) { // Duyệt và xử lý
                xuLyPheDuyetPhieu(maPhieu, loaiPhieu);
            } else if (choice == 1) { // Từ chối
                xuLyTuChoiPhieu(maPhieu);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xử lý phiếu: " + ex.getMessage());
        }
    }
    
    private void xuLyPheDuyetPhieu(String maPhieu, String loaiPhieu) throws Exception {
        // Hiển thị thông báo xác nhận
        String confirmMsg = "🔍 XÁC NHẬN PHÊ DUYỆT\n\n" +
                           "Bạn có chắc chắn muốn phê duyệt phiếu này?\n\n" +
                           "⚠️ Hành động này sẽ:\n";
        
        if ("Đổi thuốc".equals(loaiPhieu)) {
            confirmMsg += "• Xuất thuốc cũ khỏi kho\n" +
                         "• Nhập thuốc mới vào kho\n" +
                         "• Cập nhật doanh thu nhân viên (chênh lệch)\n";
        } else {
            confirmMsg += "• Nhập lại thuốc trả vào kho\n" +
                         "• Giảm doanh thu nhân viên\n";
        }
        
        confirmMsg += "• Ghi log lịch sử thay đổi\n" +
                     "• KHÔNG THỂ HOÀN TÁC!\n\n" +
                     "Tiếp tục?";
        
        int confirm = JOptionPane.showConfirmDialog(this, confirmMsg, 
            "Xác nhận phê duyệt", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = false;
            
            if ("Đổi thuốc".equals(loaiPhieu)) {
                success = phieuDoiTraDAO.xuLyDoiThuoc(maPhieu, currentUser);
            } else {
                success = phieuDoiTraDAO.xuLyTraThuoc(maPhieu, currentUser);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Xử lý " + loaiPhieu.toLowerCase() + " thành công!\n\n" +
                    "• Kho hàng đã được cập nhật\n" +
                    "• Doanh thu nhân viên đã được điều chỉnh\n" +
                    "• Lịch sử thay đổi đã được ghi nhận\n" +
                    "• Trạng thái phiếu: Đã xử lý",
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadData(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lỗi khi xử lý " + loaiPhieu.toLowerCase() + "!\n\n" +
                    "Có thể do:\n" +
                    "• Không đủ tồn kho thuốc\n" +
                    "• Lỗi kết nối database\n" +
                    "• Dữ liệu không hợp lệ",
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void xuLyTuChoiPhieu(String maPhieu) throws Exception {
        String lyDoTuChoi = JOptionPane.showInputDialog(this,
                "Nhập lý do từ chối phiếu:",
                "Từ chối phiếu",
                JOptionPane.QUESTION_MESSAGE);
        
        if (lyDoTuChoi != null && !lyDoTuChoi.trim().isEmpty()) {
            if (phieuDoiTraDAO.capNhatTrangThai(maPhieu, "Từ chối")) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Đã từ chối phiếu thành công!\n" +
                    "Lý do: " + lyDoTuChoi,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
                loadData(); // Refresh data
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Lỗi khi từ chối phiếu!",
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void btnBaoCaoActionPerformed(ActionEvent e) {
        try {
            // Hiển thị dialog báo cáo thống kê đổi trả
            showBaoCaoThongKe();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi mở báo cáo: " + ex.getMessage());
        }
    }
    
    private void showBaoCaoThongKe() {
        // Tạo dialog báo cáo thống kê
        JDialog dialogBaoCao = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                           "Báo cáo thống kê đổi trả", true);
        dialogBaoCao.setSize(800, 600);
        dialogBaoCao.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Tiêu đề
        JLabel titleLabel = new JLabel("📊 BÁO CÁO THỐNG KÊ ĐỔI TRẢ THUỐC", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Nội dung báo cáo
        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        
        try {
            // Thống kê tổng quan
            int tongPhieu = tableModel.getRowCount();
            double tongTien = 0;
            int phieuDoi = 0;
            int phieuTra = 0;
            int daXuLy = 0;
            int choXuLy = 0;
            int tuChoi = 0;
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                double tien = (Double) tableModel.getValueAt(i, 7);
                tongTien += tien;
                
                String loai = (String) tableModel.getValueAt(i, 1);
                if ("Đổi thuốc".equals(loai)) {
                    phieuDoi++;
                } else {
                    phieuTra++;
                }
                
                String trangThai = (String) tableModel.getValueAt(i, 8);
                switch (trangThai) {
                    case "Đã xử lý": daXuLy++; break;
                    case "Chờ xử lý": choXuLy++; break;
                    case "Từ chối": tuChoi++; break;
                }
            }
            
            // Panel thống kê tổng quan
            JPanel thongKePanel = new JPanel(new GridLayout(2, 3, 10, 10));
            thongKePanel.setBorder(BorderFactory.createTitledBorder("Thống kê tổng quan"));
            
            thongKePanel.add(createStatCard("Tổng phiếu", String.valueOf(tongPhieu), new Color(52, 152, 219)));
            thongKePanel.add(createStatCard("Phiếu đổi", String.valueOf(phieuDoi), new Color(46, 204, 113)));
            thongKePanel.add(createStatCard("Phiếu trả", String.valueOf(phieuTra), new Color(231, 76, 60)));
            thongKePanel.add(createStatCard("Đã xử lý", String.valueOf(daXuLy), new Color(39, 174, 96)));
            thongKePanel.add(createStatCard("Chờ xử lý", String.valueOf(choXuLy), new Color(241, 196, 15)));
            thongKePanel.add(createStatCard("Từ chối", String.valueOf(tuChoi), new Color(192, 57, 43)));
            
            contentPanel.add(thongKePanel);
            
            // Panel tổng tiền
            JPanel tienPanel = new JPanel(new FlowLayout());
            tienPanel.setBorder(BorderFactory.createTitledBorder("Tổng giá trị"));
            
            JLabel lblTongTienBaoCao = new JLabel("Tổng giá trị các phiếu: " + currencyFormat.format(tongTien));
            lblTongTienBaoCao.setFont(new Font("Arial", Font.BOLD, 16));
            lblTongTienBaoCao.setForeground(new Color(41, 128, 185));
            tienPanel.add(lblTongTienBaoCao);
            
            contentPanel.add(tienPanel);
            
            // Panel thống kê chi tiết từ PhieuDoiTraDAO
            if (phieuDoiTraDAO != null) {
                try {
                    JPanel chiTietPanel = new JPanel(new BorderLayout());
                    chiTietPanel.setBorder(BorderFactory.createTitledBorder("Thống kê chi tiết (Năm hiện tại)"));
                    
                    // Lấy thống kê theo tháng
                    List<PhieuDoiTraDAO.ThongKeDoiTra> thongKeThang = 
                        phieuDoiTraDAO.thongKeDoiTraTheoThang(java.time.LocalDate.now().getYear());
                    
                    if (!thongKeThang.isEmpty()) {
                        String[] columnNames = {"Tháng", "Loại phiếu", "Số lượng", "Tổng tiền"};
                        DefaultTableModel thongKeModel = new DefaultTableModel(columnNames, 0);
                        
                        for (PhieuDoiTraDAO.ThongKeDoiTra tk : thongKeThang) {
                            Object[] row = {
                                "Tháng " + tk.getThang(),
                                tk.getLoaiPhieu().equals("DOI") ? "Đổi thuốc" : "Trả thuốc",
                                tk.getSoLuongPhieu(),
                                currencyFormat.format(tk.getTongTien())
                            };
                            thongKeModel.addRow(row);
                        }
                        
                        JTable thongKeTable = new JTable(thongKeModel);
                        thongKeTable.setFont(new Font("Arial", Font.PLAIN, 12));
                        JScrollPane scrollPane = new JScrollPane(thongKeTable);
                        scrollPane.setPreferredSize(new Dimension(0, 150));
                        chiTietPanel.add(scrollPane, BorderLayout.CENTER);
                        
                        contentPanel.add(chiTietPanel);
                    }
                } catch (Exception ex) {
                    // Nếu không lấy được thống kê chi tiết, bỏ qua
                    System.err.println("Không thể lấy thống kê chi tiết: " + ex.getMessage());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Lỗi khi tạo báo cáo: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            contentPanel.add(errorLabel);
        }
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Nút đóng
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnDong = new JButton("Đóng");
        btnDong.setPreferredSize(new Dimension(100, 30));
        btnDong.addActionListener(e -> dialogBaoCao.dispose());
        buttonPanel.add(btnDong);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialogBaoCao.add(mainPanel);
        dialogBaoCao.setVisible(true);
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(color);
        
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 20));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    // Custom renderers
    private class LoaiPhieuRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String loai = (String) value;
            if ("Đổi thuốc".equals(loai)) {
                setForeground(new Color(46, 204, 113));
            } else if ("Trả thuốc".equals(loai)) {
                setForeground(new Color(231, 76, 60));
            }
            
            setHorizontalAlignment(CENTER);
            setFont(new Font("Arial", Font.BOLD, 12));
            
            return this;
        }
    }
    
    private class TrangThaiRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String trangThai = (String) value;
            switch (trangThai) {
                case "Đã xử lý":
                    setForeground(new Color(46, 204, 113));
                    break;
                case "Chờ xử lý":
                    setForeground(new Color(241, 196, 15));
                    break;
                case "Từ chối":
                    setForeground(new Color(231, 76, 60));
                    break;
            }
            
            setHorizontalAlignment(CENTER);
            setFont(new Font("Arial", Font.BOLD, 12));
            
            return this;
        }
    }
    
    private class TienRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Double) {
                setText(currencyFormat.format((Double) value));
            }
            
            setHorizontalAlignment(RIGHT);
            setFont(new Font("Arial", Font.BOLD, 12));
            setForeground(new Color(231, 76, 60));
            
            return this;
        }
    }
    
    // Public method để refresh data từ bên ngoài
    public void refreshData() {
        loadData();
    }
}