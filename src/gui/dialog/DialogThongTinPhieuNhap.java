package gui.dialog;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.NhanVienDAO;
import dao.PhieuNhapThuocDAO;
import dao.ThuocDAO;
import entity.PhieuNhapThuoc;
import entity.ChiTietNhapThuoc;
import entity.Thuoc;

public class DialogThongTinPhieuNhap extends JDialog {
    private JTable tableChiTiet;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;
    private JButton btnDong;
    
    private PhieuNhapThuocDAO pnhDAO;
    private ThuocDAO thuocDAO;
    private PhieuNhapThuoc phieuNhap;
    private NhanVienDAO nvDAO;
    
    public DialogThongTinPhieuNhap(Frame parent, PhieuNhapThuoc pnh) {
        super(parent, "Chi tiết phiếu nhập thuốc", true);
        this.phieuNhap = pnh;
        this.pnhDAO = new PhieuNhapThuocDAO();
        this.thuocDAO = new ThuocDAO();
        this.nvDAO = new NhanVienDAO();
        initComponents();
        loadData();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(1000, 700);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(1000, 60));
        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU NHẬP THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new LineBorder(new Color(232, 232, 232), 2, true));
        infoPanel.setPreferredSize(new Dimension(1000, 120));
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 20, 12, 20);
        
        Font labelFont = new Font("Roboto", Font.BOLD, 14);
        Font valueFont = new Font("Roboto", Font.PLAIN, 14);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Mã Phiếu
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.15;
        JLabel lblMaPhieu = new JLabel("Mã phiếu:");
        lblMaPhieu.setFont(labelFont);
        infoPanel.add(lblMaPhieu, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        JLabel valueMaPhieu = new JLabel(phieuNhap.getMaPhieuNhap());
        valueMaPhieu.setFont(valueFont);
        valueMaPhieu.setForeground(new Color(0, 102, 204));
        infoPanel.add(valueMaPhieu, gbc);
        
        // Nhân viên
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        JLabel lblNhanVien = new JLabel("Nhân viên:");
        lblNhanVien.setFont(labelFont);
        infoPanel.add(lblNhanVien, gbc);
        
        
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        String tenNV = "N/A";
		try {
			tenNV = nvDAO.getNhanVienTheoMa(phieuNhap.getNhanVien().getMaNV()).getTenNV();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JLabel valueNhanVien = new JLabel(tenNV);
        valueNhanVien.setFont(valueFont);
        infoPanel.add(valueNhanVien, gbc);
        
        // Ngày nhập
        gbc.gridx = 4;
        gbc.weightx = 0.15;
        JLabel lblNgayNhap = new JLabel("Ngày nhập:");
        lblNgayNhap.setFont(labelFont);
        infoPanel.add(lblNgayNhap, gbc);
        
        gbc.gridx = 5;
        gbc.weightx = 0.15;
        JLabel valueNgayNhap = new JLabel(sdf.format(phieuNhap.getNgayNhap()));
        valueNgayNhap.setFont(valueFont);
        infoPanel.add(valueNgayNhap, gbc);
        
        // Nhà cung cấp (row 2)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.15;
        JLabel lblNCC = new JLabel("Nhà cung cấp:");
        lblNCC.setFont(labelFont);
        infoPanel.add(lblNCC, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        String tenNCC = phieuNhap.getNhaCungCap() != null ? phieuNhap.getNhaCungCap().getTenNCC() : "N/A";
        JLabel valueNCC = new JLabel(tenNCC);
        valueNCC.setFont(valueFont);
        infoPanel.add(valueNCC, gbc);
        
        // Mã nhân viên
        gbc.gridx = 2;
        gbc.weightx = 0.15;
        JLabel lblMaNV = new JLabel("Mã NV:");
        lblMaNV.setFont(labelFont);
        infoPanel.add(lblMaNV, gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 0.15;
        String maNV = phieuNhap.getNhanVien() != null ? phieuNhap.getNhanVien().getMaNV() : "N/A";
        JLabel valueMaNV = new JLabel(maNV);
        valueMaNV.setFont(valueFont);
        valueMaNV.setForeground(new Color(0, 102, 204));
        infoPanel.add(valueMaNV, gbc);
        
        // Mã nhà cung cấp
        gbc.gridx = 4;
        gbc.weightx = 0.15;
        JLabel lblMaNCC = new JLabel("Mã NCC:");
        lblMaNCC.setFont(labelFont);
        infoPanel.add(lblMaNCC, gbc);
        
        gbc.gridx = 5;
        gbc.weightx = 0.15;
        String maNCC = phieuNhap.getNhaCungCap() != null ? phieuNhap.getNhaCungCap().getMaNCC() : "N/A";
        JLabel valueMaNCC = new JLabel(maNCC);
        valueMaNCC.setFont(valueFont);
        valueMaNCC.setForeground(new Color(0, 102, 204));
        infoPanel.add(valueMaNCC, gbc);
        
        add(infoPanel, BorderLayout.NORTH);
        
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        tablePanel.setLayout(new BorderLayout(0, 10));
        
        JPanel titleTablePanel = new JPanel();
        titleTablePanel.setBackground(new Color(0, 0, 205));
        titleTablePanel.setPreferredSize(new Dimension(1000, 40));
        titleTablePanel.setLayout(new BorderLayout());
        JLabel lblTitleTable = new JLabel("DANH SÁCH CHI TIẾT NHẬP THUỐC");
        lblTitleTable.setFont(new Font("Roboto", Font.BOLD, 15));
        lblTitleTable.setForeground(Color.WHITE);
        lblTitleTable.setBorder(new EmptyBorder(0, 10, 0, 10));
        titleTablePanel.add(lblTitleTable, BorderLayout.WEST);
        
        tablePanel.add(titleTablePanel, BorderLayout.NORTH);
        
        String[] columnNames = {"STT", "Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableChiTiet = new JTable(tableModel);
        tableChiTiet.setRowHeight(35);
        tableChiTiet.setFont(new Font("Roboto", Font.PLAIN, 13));
        tableChiTiet.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 13));
        tableChiTiet.getTableHeader().setBackground(new Color(0, 102, 204));
        tableChiTiet.getTableHeader().setForeground(Color.WHITE);
        tableChiTiet.setShowHorizontalLines(true);
        tableChiTiet.setGridColor(new Color(220, 220, 220));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        // Set renderer cho các cột
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            if (i == 1 || i == 2) {
                // Mã thuốc và Tên thuốc
                tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer());
            } else {
                // Các cột khác
                if (i == 0) {
                    tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    tableChiTiet.getColumnModel().getColumn(i).setPreferredWidth(40);
                } else if (i == 2) {
                    tableChiTiet.getColumnModel().getColumn(i).setPreferredWidth(200);
                } else {
                    tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
                }
            }
        }
        
        tableChiTiet.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableChiTiet.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableChiTiet.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableChiTiet.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableChiTiet.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableChiTiet.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane tableScroll = new JScrollPane(tableChiTiet);
        tableScroll.setBackground(Color.WHITE);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        // Total Panel
        JPanel totalPanel = new JPanel();
        totalPanel.setBackground(Color.WHITE);
        totalPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        totalPanel.setPreferredSize(new Dimension(1000, 60));
        totalPanel.setLayout(new BorderLayout());
        
        JLabel lblTongLabel = new JLabel("TỔNG TIỀN NHẬP:");
        lblTongLabel.setFont(new Font("Roboto", Font.BOLD, 15));
        lblTongLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        lblTongTien = new JLabel("0.00 VNĐ");
        lblTongTien.setFont(new Font("Roboto", Font.BOLD, 16));
        lblTongTien.setForeground(new Color(220, 20, 60)); // Crimson red
        lblTongTien.setBorder(new EmptyBorder(0, 0, 0, 20));
        lblTongTien.setHorizontalAlignment(JLabel.RIGHT);
        
        totalPanel.add(lblTongLabel, BorderLayout.WEST);
        totalPanel.add(lblTongTien, BorderLayout.EAST);
        
        tablePanel.add(totalPanel, BorderLayout.SOUTH);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(1000, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.setBackground(new Color(0, 0, 205));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnDong.addActionListener(e -> dispose());
        
        buttonPanel.add(btnDong);
        
        add(buttonPanel, BorderLayout.PAGE_END);
    }
    
    private void loadData() {
        try {
            ArrayList<ChiTietNhapThuoc> chiTiets = pnhDAO.getChiTietPhieuNhap(phieuNhap.getMaPhieuNhap());
            
            double tongTien = 0;
            int stt = 1;
            
            for (ChiTietNhapThuoc ct : chiTiets) {
                // Lấy thông tin thuốc
                Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(ct.getMaThuoc());
                String tenThuoc = thuoc != null ? thuoc.getTenThuoc() : "N/A";
                
                double thanhTien = ct.getSoLuong() * ct.getDonGia();
                tongTien += thanhTien;
                
                tableModel.addRow(new Object[]{
                    stt,
                    ct.getMaThuoc(),
                    tenThuoc,
                    ct.getSoLuong(),
                    String.format("%.2f", ct.getDonGia()),
                    String.format("%.2f", thanhTien)
                });
                
                stt++;
            }
            
            // Cập nhật tổng tiền
            lblTongTien.setText(String.format("%.2f VNĐ", tongTien));
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}