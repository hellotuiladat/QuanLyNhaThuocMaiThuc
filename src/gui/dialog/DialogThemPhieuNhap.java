package gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.PhieuNhapThuocDAO;
import dao.NhanVienDAO;
import dao.NhaCungCapDAO;
import dao.ThuocDAO;
import entity.PhieuNhapThuoc;
import entity.NhanVien;
import entity.NhaCungCap;
import entity.Thuoc;
import entity.ChiTietNhapThuoc;
import gui.form.FormPhieuNhapThuoc;

public class DialogThemPhieuNhap extends JDialog {
    private JComboBox<String> cboNhanVien;
    private JComboBox<String> cboNhaCungCap;
    private JComboBox<String> cboThuoc;
    private JSpinner spnSoLuong;
    private JTextField txtDonGia;
    private JDateChooser dateChooserNgayNhap;
    private JTable tableChiTiet;
    private DefaultTableModel tableModel;
    private JButton btnThemChiTiet;
    private JButton btnXoaChiTiet;
    private JButton btnXacNhan;
    private JButton btnHuy;
    
    private ArrayList<ChiTietNhapThuoc> listChiTiet;
    private PhieuNhapThuocDAO pnhDAO;
    private NhanVienDAO nvDAO;
    private NhaCungCapDAO nccDAO;
    private ThuocDAO thuocDAO;
    private FormPhieuNhapThuoc parentForm;
    
    public DialogThemPhieuNhap(Frame parent, FormPhieuNhapThuoc form) {
        super(parent, "Thêm phiếu nhập thuốc mới", true);
        this.parentForm = form;
        this.pnhDAO = new PhieuNhapThuocDAO();
        this.nvDAO = new NhanVienDAO();
        this.nccDAO = new NhaCungCapDAO();
        this.thuocDAO = new ThuocDAO();
        this.listChiTiet = new ArrayList<>();
        
        initComponents();
        loadComboBoxData();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(1000, 750);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 205));
        titlePanel.setPreferredSize(new Dimension(1000, 60));
        JLabel lblTitle = new JLabel("THÊM PHIẾU NHẬP THUỐC MỚI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        Font labelFont = new Font("Roboto", Font.PLAIN, 14);
        Font fieldFont = new Font("Roboto", Font.PLAIN, 14);
        
        // Nhân Viên
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblNhanVien = new JLabel("Nhân viên: *");
        lblNhanVien.setFont(labelFont);
        formPanel.add(lblNhanVien, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cboNhanVien = new JComboBox<>();
        cboNhanVien.setFont(fieldFont);
        cboNhanVien.setPreferredSize(new Dimension(300, 35));
        cboNhanVien.setBackground(Color.WHITE);
        formPanel.add(cboNhanVien, gbc);
        
        // Nhà Cung Cấp
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblNhaCungCap = new JLabel("Nhà cung cấp: *");
        lblNhaCungCap.setFont(labelFont);
        formPanel.add(lblNhaCungCap, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cboNhaCungCap = new JComboBox<>();
        cboNhaCungCap.setFont(fieldFont);
        cboNhaCungCap.setPreferredSize(new Dimension(300, 35));
        cboNhaCungCap.setBackground(Color.WHITE);
        formPanel.add(cboNhaCungCap, gbc);
        
        // Ngày nhập
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblNgayNhap = new JLabel("Ngày nhập: *");
        lblNgayNhap.setFont(labelFont);
        formPanel.add(lblNgayNhap, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        dateChooserNgayNhap = new JDateChooser();
        dateChooserNgayNhap.setDateFormatString("dd/MM/yyyy");
        dateChooserNgayNhap.setFont(fieldFont);
        dateChooserNgayNhap.setPreferredSize(new Dimension(300, 35));
        dateChooserNgayNhap.setDate(new Date());
        dateChooserNgayNhap.setBackground(Color.WHITE);
        formPanel.add(dateChooserNgayNhap, gbc);
        
        // Separator
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 15, 5);
        JSeparator separator = new JSeparator();
        formPanel.add(separator, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Thuốc
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel lblThuoc = new JLabel("Thuốc: *");
        lblThuoc.setFont(labelFont);
        formPanel.add(lblThuoc, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cboThuoc = new JComboBox<>();
        cboThuoc.setFont(fieldFont);
        cboThuoc.setPreferredSize(new Dimension(300, 35));
        cboThuoc.setBackground(Color.WHITE);
        formPanel.add(cboThuoc, gbc);
        
        // Số lượng
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        JLabel lblSoLuong = new JLabel("Số lượng: *");
        lblSoLuong.setFont(labelFont);
        formPanel.add(lblSoLuong, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        spnSoLuong.setFont(fieldFont);
        spnSoLuong.setPreferredSize(new Dimension(300, 35));
        formPanel.add(spnSoLuong, gbc);
        
        // Đơn giá
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        JLabel lblDonGia = new JLabel("Đơn giá: *");
        lblDonGia.setFont(labelFont);
        formPanel.add(lblDonGia, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtDonGia = new JTextField();
        txtDonGia.setFont(fieldFont);
        txtDonGia.setPreferredSize(new Dimension(300, 35));
        txtDonGia.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtDonGia, gbc);
        
        // Nút thêm chi tiết
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        btnThemChiTiet = new JButton("THÊM CHI TIẾT");
        btnThemChiTiet.setFont(new Font("Roboto", Font.BOLD, 12));
        btnThemChiTiet.setPreferredSize(new Dimension(200, 35));
        btnThemChiTiet.setBackground(new Color(0, 102, 204));
        btnThemChiTiet.setForeground(Color.WHITE);
        btnThemChiTiet.setFocusPainted(false);
        btnThemChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnThemChiTiet.addActionListener(e -> themChiTiet());
        formPanel.add(btnThemChiTiet, gbc);
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        // Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        tablePanel.setLayout(new BorderLayout());
        
        String[] columnNames = {"Mã thuốc", "Tên thuốc", "Số lượng", "Đơn giá", "Thành tiền", "Xóa"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép click nút Xóa
            }
        };
        
        tablePanel.add(new JLabel("CHI TIẾT NHẬP THUỐC"), BorderLayout.NORTH);
        tablePanel.setBorder(new LineBorder(new Color(200, 200, 200), 1));
        
        tableChiTiet = new JTable(tableModel);
        tableChiTiet.setRowHeight(30);
        tableChiTiet.setFont(new Font("Roboto", Font.PLAIN, 12));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            tableChiTiet.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane tableScroll = new JScrollPane(tableChiTiet);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.SOUTH);
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 245, 245));
        buttonPanel.setPreferredSize(new Dimension(1000, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        btnXacNhan = new JButton("XÁC NHẬN");
        btnXacNhan.setFont(new Font("Roboto", Font.BOLD, 14));
        btnXacNhan.setPreferredSize(new Dimension(140, 40));
        btnXacNhan.setBackground(new Color(0, 0, 205));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFocusPainted(false);
        btnXacNhan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXacNhan.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnXacNhan.addActionListener(e -> xuLyThemPhieuNhap());
        
        btnHuy = new JButton("HỦY");
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(140, 40));
        btnHuy.setBackground(Color.WHITE);
        btnHuy.setForeground(new Color(100, 100, 100));
        btnHuy.setFocusPainted(false);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setBorder(new LineBorder(new Color(200, 200, 200), 2, true));
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnHuy);
        
        add(buttonPanel, BorderLayout.PAGE_END);
    }
    
    private void loadComboBoxData() {
        try {
            // Load Nhân viên
            ArrayList<NhanVien> listNV = nvDAO.getDSNhanVien();
            cboNhanVien.addItem("-- Chọn nhân viên --");
            for (NhanVien nv : listNV) {
                cboNhanVien.addItem(nv.getMaNV() + " - " + nv.getTenNV());
            }
            
            // Load Nhà cung cấp
            ArrayList<NhaCungCap> listNCC = nccDAO.getDSNhaCungCap();
            cboNhaCungCap.addItem("-- Chọn nhà cung cấp --");
            for (NhaCungCap ncc : listNCC) {
                cboNhaCungCap.addItem(ncc.getMaNCC() + " - " + ncc.getTenNCC());
            }
            
            // Load Thuốc
            ArrayList<Thuoc> listThuoc = thuocDAO.getDsThuoc();
            cboThuoc.addItem("-- Chọn thuốc --");
            for (Thuoc thuoc : listThuoc) {
                cboThuoc.addItem(thuoc.getMaThuoc() + " - " + thuoc.getTenThuoc());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void themChiTiet() {
        if (!validateChiTiet()) {
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String maThuoc = cboThuoc.getSelectedItem().toString().split(" - ")[0];
            String tenThuoc = cboThuoc.getSelectedItem().toString().split(" - ")[1];
            int soLuong = (Integer) spnSoLuong.getValue();
            double donGia = Double.parseDouble(txtDonGia.getText().trim());
            double thanhTien = soLuong * donGia;
            
            // Kiểm tra thuốc đã có trong danh sách chưa
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(maThuoc)) {
                    JOptionPane.showMessageDialog(this, 
                        "Thuốc này đã có trong danh sách!", 
                        "Cảnh báo", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            // Thêm vào danh sách
            ChiTietNhapThuoc ct = new ChiTietNhapThuoc("", maThuoc, soLuong, donGia);
            listChiTiet.add(ct);
            
            // Thêm vào bảng
            tableModel.addRow(new Object[]{
                maThuoc, 
                tenThuoc, 
                soLuong, 
                String.format("%.2f", donGia),
                String.format("%.2f", thanhTien),
                "Xóa"
            });
            
            // Reset form
            cboThuoc.setSelectedIndex(0);
            spnSoLuong.setValue(1);
            txtDonGia.setText("");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi thêm chi tiết: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateChiTiet() {
        if (cboThuoc.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thuốc!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if ((Integer) spnSoLuong.getValue() <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Số lượng phải lớn hơn 0!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        String donGiaStr = txtDonGia.getText().trim();
        if (donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đơn giá!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            double donGia = Double.parseDouble(donGiaStr);
            if (donGia < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Đơn giá không được âm!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Đơn giá phải là số!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void xuLyThemPhieuNhap() {
        if (!validateForm()) {
            return;
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng thêm ít nhất một chi tiết nhập thuốc!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String maPhieuNhap = pnhDAO.generateMaPhieuNhap();
            
            String maNV = cboNhanVien.getSelectedItem().toString().split(" - ")[0];
            String maNCC = cboNhaCungCap.getSelectedItem().toString().split(" - ")[0];
            Date ngayNhap = dateChooserNgayNhap.getDate();
            
            // Lấy object NhanVien và NhaCungCap
            NhanVien nv = nvDAO.getNhanVienTheoMa(maNV);
            NhaCungCap ncc = nccDAO.getNhaCungCapTheoMa(maNCC);
            
            // Cập nhật maPhieuNhap cho các chi tiết
            for (ChiTietNhapThuoc ct : listChiTiet) {
                ct.setMaPhieuNhap(maPhieuNhap);
            }
            
            PhieuNhapThuoc pnh = new PhieuNhapThuoc(maPhieuNhap, nv, ncc, ngayNhap);
            
            boolean result = pnhDAO.themPhieuNhapThuoc(pnh, listChiTiet);
            
            if (result) {
                JOptionPane.showMessageDialog(this, 
                    "Thêm phiếu nhập thuốc thành công!\nMã phiếu: " + maPhieuNhap, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                parentForm.loadTableData();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Thêm phiếu nhập thuốc thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Có lỗi xảy ra: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateForm() {
        if (cboNhanVien.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn nhân viên!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (cboNhaCungCap.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn nhà cung cấp!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (dateChooserNgayNhap.getDate() == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn ngày nhập!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Date ngayNhap = dateChooserNgayNhap.getDate();
        if (ngayNhap.after(new Date())) {
            JOptionPane.showMessageDialog(this, 
                "Ngày nhập không được sau ngày hiện tại!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
}