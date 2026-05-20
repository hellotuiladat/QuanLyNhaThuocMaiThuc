package gui.form;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.HoaDonDAO;
import entity.HoaDon;
import gui.dialog.DialogChiTietHoaDon;

public class FormTimKiemHoaDon extends JPanel 
{
  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMaHD;
    private JTextField txtMaNV;
    private JTextField txtMaKH;
    private JTextField txtMaThue;
    private JTextField txtMaKM;
    private JTextField txtMaPDT;
    private JTextField txtNgayLap;
    
   
    private JButton btnTimKiem;
    private JButton btnLamMoi;
    private JButton btnXuatExcel;
    private JButton btnXemChiTiet;
    
 
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JLabel lblKetQua;
    
    
    private HoaDonDAO hoaDonDAO;
    
   
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    
    private Font labelFont = new Font("Roboto", Font.PLAIN, 14);
    private Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
    private Font titleFont = new Font("Roboto", Font.BOLD, 24);
    private Font headerTableFont = new Font("Roboto", Font.BOLD, 16);
    
    public FormTimKiemHoaDon() {
        hoaDonDAO = new HoaDonDAO();
        initComponents();
        hienThiTatCaHoaDon();
    }
    
    private void initComponents() {
        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setMinimumSize(new Dimension(1130, 800));
        setPreferredSize(new Dimension(1130, 800));
        setLayout(new BorderLayout(0, 10));
        
        // Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center Panel (Search Form + Table)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(new Color(230, 245, 245));
        centerPanel.add(createSearchPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Tạo header panel với tiêu đề
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(1130, 70));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("TÌM KIẾM HÓA ĐƠN");
        lblTitle.setFont(titleFont);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblIcon = new JLabel(new FlatSVGIcon("./img/search.svg"));
        lblIcon.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        headerPanel.add(lblIcon, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Tạo panel tìm kiếm với tiêu chí
     */
    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel lblSearchTitle = new JLabel("Tiêu chí tìm kiếm");
        lblSearchTitle.setFont(new Font("Roboto", Font.BOLD, 18));
        lblSearchTitle.setForeground(new Color(0, 0, 205));
        lblSearchTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        mainPanel.add(lblSearchTitle, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Row 1: Mã HĐ, Ngày lập, Mã Thuế
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã HĐ:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.22;
        txtMaHD = createTextField();
        formPanel.add(txtMaHD, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.12;
        formPanel.add(createLabel("Ngày lập:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.22;
        txtNgayLap = createTextField();
        formPanel.add(txtNgayLap, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã thuế:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.22;
        txtMaThue = createTextField();
        formPanel.add(txtMaThue, gbc);
        
        // Row 2: Mã NV, Mã KH, Mã KM
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã NV:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.22;
        txtMaNV = createTextField();
        formPanel.add(txtMaNV, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã KH:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.22;
        txtMaKH = createTextField();
        formPanel.add(txtMaKH, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã KM:"), gbc);
        
        gbc.gridx = 5; gbc.weightx = 0.22;
        txtMaKM = createTextField();
        formPanel.add(txtMaKM, gbc);
        
        // Row 3: Mã PDT
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.12;
        formPanel.add(createLabel("Mã PDT:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.22;
        txtMaPDT = createTextField();
        formPanel.add(txtMaPDT, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    // button panel
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        // Nút Tìm kiếm
        btnTimKiem = new JButton("TÌM KIẾM");
        btnTimKiem.setFont(new Font("Roboto", Font.BOLD, 14));
        btnTimKiem.setPreferredSize(new Dimension(150, 40));
        btnTimKiem.setBackground(new Color(0, 0, 205));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.setFocusPainted(false);
        btnTimKiem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKiem.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnTimKiem.setIcon(new FlatSVGIcon(getClass().getResource("/img/search.svg")));
        btnTimKiem.addActionListener(e -> xuLyTimKiem());
        
        // Nút Làm mới
        btnLamMoi = new JButton("LÀM MỚI");
        btnLamMoi.setFont(new Font("Roboto", Font.BOLD, 14));
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.setBackground(Color.WHITE);
        btnLamMoi.setForeground(new Color(100, 100, 100));
        btnLamMoi.setFocusPainted(false);
        btnLamMoi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLamMoi.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnLamMoi.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnLamMoi.addActionListener(e -> lamMoi());
        
        // Nút Xuất Excel
        btnXuatExcel = new JButton("XUẤT EXCEL");
        btnXuatExcel.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXuatExcel.setPreferredSize(new Dimension(150, 40));
        btnXuatExcel.setBackground(new Color(46, 125, 50));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.setFocusPainted(false);
        btnXuatExcel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXuatExcel.setBorder(new LineBorder(new Color(46, 125, 50), 2, true));
        btnXuatExcel.setIcon(new FlatSVGIcon(getClass().getResource("/img/excel.svg")));
        btnXuatExcel.addActionListener(e -> xuatExcel());
        
        buttonPanel.add(btnTimKiem);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnXuatExcel);
        
        return buttonPanel;
    }
    

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(new Color(230, 245, 245));
        
        // Result Info Panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        resultPanel.setPreferredSize(new Dimension(1130, 50));
        
        lblKetQua = new JLabel("Tìm thấy 0 kết quả");
        lblKetQua.setFont(new Font("Roboto", Font.BOLD, 16));
        lblKetQua.setForeground(new Color(0, 0, 205));
        lblKetQua.setBorder(new EmptyBorder(10, 20, 10, 10));
        
        btnXemChiTiet = new JButton("XEM CHI TIẾT");
        btnXemChiTiet.setFont(new Font("Roboto", Font.BOLD, 13));
        btnXemChiTiet.setPreferredSize(new Dimension(140, 35));
        btnXemChiTiet.setBackground(new Color(33, 150, 243));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemChiTiet.setBorder(new LineBorder(new Color(33, 150, 243), 2, true));
        btnXemChiTiet.addActionListener(e -> xemChiTiet());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 7));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnXemChiTiet);
        
        resultPanel.add(lblKetQua, BorderLayout.WEST);
        resultPanel.add(btnPanel, BorderLayout.EAST);
        
        tablePanel.add(resultPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Mã HĐ", "Ngày lập", "Mã Thuế", "Mã NV", "Mã KH", "Mã KM", "Mã PDT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Roboto", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(headerTableFont);
        table.getTableHeader().setBackground(new Color(0, 0, 205));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Chiều rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    // Style label
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(labelFont);
        return label;
    }
    
    // style jtextfield
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(fieldFont);
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }
    
    /**
     * Xử lý tìm kiếm hóa đơn
     */
    private void xuLyTimKiem() {
        try {
            // Lấy các tiêu chí tìm kiếm
            String maHD = txtMaHD.getText().trim();
            String maNV = txtMaNV.getText().trim();
            String maKH = txtMaKH.getText().trim();
            String maThue = txtMaThue.getText().trim();
            String maKM = txtMaKM.getText().trim();
            String maPDT = txtMaPDT.getText().trim();
            String ngayLap = txtNgayLap.getText().trim();
            
            // Lấy tất cả hóa đơn
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getDsHoaDon();
            ArrayList<HoaDon> ketQua = new ArrayList<>();
            
            // Lọc theo tiêu chí
            for (HoaDon hd : allHoaDon) {
                if (!maHD.isEmpty() && !hd.getMaHD().toLowerCase().contains(maHD.toLowerCase())) {
                    continue;
                }
                if (!maNV.isEmpty() && (hd.getNhanVien() == null || 
                    !hd.getNhanVien().getMaNV().toLowerCase().contains(maNV.toLowerCase()))) {
                    continue;
                }
                if (!maKH.isEmpty() && (hd.getKhachHang() == null || 
                    !hd.getKhachHang().getMaKH().toLowerCase().contains(maKH.toLowerCase()))) {
                    continue;
                }
                if (!maThue.isEmpty() && (hd.getThue() == null || 
                    !hd.getThue().getMaThue().toLowerCase().contains(maThue.toLowerCase()))) {
                    continue;
                }
                if (!maKM.isEmpty() && (hd.getKhuyenMai() == null || 
                    !hd.getKhuyenMai().getMaKM().toLowerCase().contains(maKM.toLowerCase()))) {
                    continue;
                }
                if (!maPDT.isEmpty() && (hd.getPhieuDatThuoc() == null || 
                    !hd.getPhieuDatThuoc().getMaPhieuDat().toLowerCase().contains(maPDT.toLowerCase()))) {
                    continue;
                }
                if (!ngayLap.isEmpty() && (hd.getNgayLap() == null || 
                    !hd.getNgayLap().toString().contains(ngayLap))) {
                    continue;
                }
                
                ketQua.add(hd);
            }
            
            // Hiển thị kết quả
            hienThiKetQua(ketQua);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tìm kiếm: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Hiển thị kết quả tìm kiếm lên bảng
     */
    private void hienThiKetQua(ArrayList<HoaDon> dsHoaDon) {
        tableModel.setRowCount(0);
        
        for (HoaDon hd : dsHoaDon) {
            tableModel.addRow(new Object[]{
                hd.getMaHD(),
                hd.getNgayLap() != null ? sdf.format(hd.getNgayLap()) : "",
                hd.getThue() != null ? hd.getThue().getMaThue() : "",
                hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "",
                hd.getKhachHang() != null ? hd.getKhachHang().getMaKH() : "",
                hd.getKhuyenMai() != null ? hd.getKhuyenMai().getMaKM() : "",
                hd.getPhieuDatThuoc() != null ? hd.getPhieuDatThuoc().getMaPhieuDat() : ""
            });
        }
        
        lblKetQua.setText("Tìm thấy " + dsHoaDon.size() + " kết quả");
        
        if (dsHoaDon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy hóa đơn nào phù hợp!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Làm mới form
     */
    private void lamMoi() {
        txtMaHD.setText("");
        txtMaNV.setText("");
        txtMaKH.setText("");
        txtMaThue.setText("");
        txtMaKM.setText("");
        txtMaPDT.setText("");
        txtNgayLap.setText("");
        
        tableModel.setRowCount(0);
        lblKetQua.setText("Tìm thấy 0 kết quả");
        
        hienThiTatCaHoaDon();
    }
    
    /**
     * Xem chi tiết hóa đơn
     */
    private void xemChiTiet() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn hóa đơn cần xem!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maHD = tableModel.getValueAt(selectedRow, 0).toString();
        
        try {
            ArrayList<HoaDon> allHoaDon = hoaDonDAO.getDsHoaDon();
            HoaDon hd = null;
            
            for (HoaDon h : allHoaDon) {
                if (h.getMaHD().equals(maHD)) {
                    hd = h;
                    break;
                }
            }
            
            if (hd != null) {
                DialogChiTietHoaDon dialog = new DialogChiTietHoaDon(
                    (JFrame) SwingUtilities.getWindowAncestor(this),maHD
                );
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy hóa đơn này!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi lấy thông tin: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void xuatExcel() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Không có dữ liệu để xuất!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            "Chức năng xuất Excel đang được phát triển!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
        
        // TODO: Implement Excel export
    }
    
    private void hienThiTatCaHoaDon() {
        try {
            ArrayList<HoaDon> dsHoaDon = hoaDonDAO.getDsHoaDon();
            hienThiKetQua(dsHoaDon);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải danh sách hóa đơn: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
