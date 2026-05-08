package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.DanhMucThuocDAO;
import dao.ThuocDAO;
import entity.DanhMucThuoc;
import entity.Thuoc;

public class DialogThemThuoc extends JDialog {
    private JTextField txtTenThuoc;
    private JTextField txtDonViTinh;
    private JTextField txtGiaBan;
    private JTextField txtSoLuong;
    private JTextField txtNgaySanXuat;
    private JTextField txtHanSuDung;
    private JTextField txtXuatXu;
    private JTextField txtHinhAnh;
    private JTextArea txtMoTa;
    private JTextArea txtThanhPhan;
    private JComboBox<String> cboxDanhMuc;
    
    private JButton btnLuu;
    private JButton btnHuy;
    
    private ThuocDAO thuocDAO;
    private DanhMucThuocDAO danhMucDAO;
    private ArrayList<DanhMucThuoc> dsDanhMuc;
    private boolean isSuccess = false;
    private String maThuocMoi = null;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public DialogThemThuoc(JFrame parent) {
        super(parent, "Thêm thuốc mới", true);
        thuocDAO = new ThuocDAO();
        danhMucDAO = new DanhMucThuocDAO();
        
        initComponents();
        loadDanhMuc();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(700, 750);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(230, 245, 245));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(700, 60));
        JLabel lblTitle = new JLabel("THÊM THUỐC MỚI");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Note Panel
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        notePanel.setBackground(new Color(255, 243, 205));
        notePanel.setBorder(new LineBorder(new Color(255, 193, 7), 1));
        JLabel lblNote = new JLabel("📌 Mã thuốc sẽ được tạo tự động (VD: TH00001)");
        lblNote.setFont(new Font("Roboto", Font.ITALIC, 13));
        lblNote.setForeground(new Color(102, 60, 0));
        notePanel.add(lblNote);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        int row = 0;
        
        // Tên thuốc
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Tên thuốc:"), gbc);
        gbc.gridx = 1;
        txtTenThuoc = createTextField();
        formPanel.add(txtTenThuoc, gbc);
        row++;
        
        // Danh mục
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        cboxDanhMuc = new JComboBox<>();
        cboxDanhMuc.setPreferredSize(new Dimension(300, 35));
        cboxDanhMuc.setFont(new Font("Roboto", Font.PLAIN, 14));
        formPanel.add(cboxDanhMuc, gbc);
        row++;
        
        // Đơn vị tính
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Đơn vị tính:"), gbc);
        gbc.gridx = 1;
        txtDonViTinh = createTextField();
        formPanel.add(txtDonViTinh, gbc);
        row++;
        
        // Xuất xứ
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Xuất xứ:"), gbc);
        gbc.gridx = 1;
        txtXuatXu = createTextField();
        formPanel.add(txtXuatXu, gbc);
        row++;
        
        // Giá bán
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Giá bán:"), gbc);
        gbc.gridx = 1;
        txtGiaBan = createTextField();
        formPanel.add(txtGiaBan, gbc);
        row++;
        
        // Số lượng
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Số lượng tồn:"), gbc);
        gbc.gridx = 1;
        txtSoLuong = createTextField();
        formPanel.add(txtSoLuong, gbc);
        row++;
        
        // Ngày sản xuất
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Ngày sản xuất:"), gbc);
        gbc.gridx = 1;
        txtNgaySanXuat = createTextField();
        txtNgaySanXuat.setToolTipText("Định dạng: dd/MM/yyyy");
        formPanel.add(txtNgaySanXuat, gbc);
        row++;
        
        // Hạn sử dụng
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Hạn sử dụng:"), gbc);
        gbc.gridx = 1;
        txtHanSuDung = createTextField();
        txtHanSuDung.setToolTipText("Định dạng: dd/MM/yyyy");
        formPanel.add(txtHanSuDung, gbc);
        row++;
        
        // Hình ảnh
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(createLabel("Hình ảnh:"), gbc);
        gbc.gridx = 1;
        txtHinhAnh = createTextField();
        formPanel.add(txtHinhAnh, gbc);
        row++;
        
        // Thành phần
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createLabel("Thành phần:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        txtThanhPhan = new JTextArea(3, 20);
        txtThanhPhan.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtThanhPhan.setLineWrap(true);
        txtThanhPhan.setWrapStyleWord(true);
        txtThanhPhan.setBorder(new LineBorder(Color.GRAY, 1));
        JScrollPane scrollThanhPhan = new JScrollPane(txtThanhPhan);
        scrollThanhPhan.setPreferredSize(new Dimension(300, 60));
        formPanel.add(scrollThanhPhan, gbc);
        row++;
        
        // Mô tả
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(createLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setFont(new Font("Roboto", Font.PLAIN, 14));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBorder(new LineBorder(Color.GRAY, 1));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setPreferredSize(new Dimension(300, 60));
        formPanel.add(scrollMoTa, gbc);
        
        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(null);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        btnLuu = new JButton("LƯU");
        btnLuu.setIcon(new FlatSVGIcon("./img/save.svg"));
        btnLuu.setFont(new Font("Roboto", Font.BOLD, 14));
        btnLuu.setPreferredSize(new Dimension(120, 40));
        btnLuu.setBackground(new Color(0, 0, 205));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLuu.setFocusPainted(false);
        btnLuu.addActionListener(e -> luuThuoc());
        
        btnHuy = new JButton("HỦY");
        btnHuy.setIcon(new FlatSVGIcon("./img/cancel.svg"));
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(120, 40));
        btnHuy.setBackground(new Color(220, 53, 69));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(new Color(230, 245, 245));
        mainPanel.add(notePanel, BorderLayout.NORTH);
        mainPanel.add(scrollForm, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(120, 30));
        return label;
    }
    
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 35));
        textField.setFont(new Font("Roboto", Font.PLAIN, 14));
        return textField;
    }
    
    private void loadDanhMuc() {
        try {
            dsDanhMuc = danhMucDAO.getDsDanhMucThuoc();
            cboxDanhMuc.removeAllItems();
            for (DanhMucThuoc dm : dsDanhMuc) {
                cboxDanhMuc.addItem(dm.getTenDanhMuc());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải danh mục: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void luuThuoc() {
        // Validate dữ liệu
        if (!validateData()) {
            return;
        }
        
        try {
            // Tạo đối tượng Thuoc (không cần mã thuốc)
            String tenThuoc = txtTenThuoc.getText().trim();
            String donViTinh = txtDonViTinh.getText().trim();
            double giaBan = Double.parseDouble(txtGiaBan.getText().trim());
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            Date ngaySanXuat = dateFormat.parse(txtNgaySanXuat.getText().trim());
            Date hanSuDung = dateFormat.parse(txtHanSuDung.getText().trim());
            String xuatXu = txtXuatXu.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();
            String thanhPhan = txtThanhPhan.getText().trim();
            String moTa = txtMoTa.getText().trim();
            
            // Lấy danh mục được chọn
            int selectedIndex = cboxDanhMuc.getSelectedIndex();
            DanhMucThuoc danhMuc = dsDanhMuc.get(selectedIndex);
            
            // Chuyển đổi java.util.Date sang java.sql.Date
            java.sql.Date sqlNgaySanXuat = new java.sql.Date(ngaySanXuat.getTime());
            java.sql.Date sqlHanSuDung = new java.sql.Date(hanSuDung.getTime());
            
            // Tạo thuốc với mã tạm (sẽ được tự động tạo trong DAO)
            Thuoc thuoc = new Thuoc("", tenThuoc, donViTinh, giaBan, soLuong, 
                                   sqlHanSuDung, moTa, danhMuc, hinhAnh, thanhPhan, 
                                   sqlNgaySanXuat, xuatXu);
            
            // Lưu vào database và nhận mã thuốc mới
            maThuocMoi = thuocDAO.themThuoc(thuoc);
            
            if (maThuocMoi != null) {
                JOptionPane.showMessageDialog(this, 
                    "Thêm thuốc thành công!\nMã thuốc: " + maThuocMoi, 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                isSuccess = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể thêm thuốc. Vui lòng thử lại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Giá bán và số lượng phải là số!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi thêm thuốc: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateData() {
        if (txtTenThuoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên thuốc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTenThuoc.requestFocus();
            return false;
        }
        
        if (txtDonViTinh.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đơn vị tính!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtDonViTinh.requestFocus();
            return false;
        }
        
        if (txtGiaBan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá bán!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtGiaBan.requestFocus();
            return false;
        }
        
        try {
            double gia = Double.parseDouble(txtGiaBan.getText().trim());
            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá bán phải lớn hơn 0!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtGiaBan.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá bán phải là số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtGiaBan.requestFocus();
            return false;
        }
        
        if (txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuong.requestFocus();
            return false;
        }
        
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng không được âm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                txtSoLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSoLuong.requestFocus();
            return false;
        }
        
        if (txtNgaySanXuat.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày sản xuất!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtNgaySanXuat.requestFocus();
            return false;
        }
        
        if (txtHanSuDung.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập hạn sử dụng!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHanSuDung.requestFocus();
            return false;
        }
        
        if (txtXuatXu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập xuất xứ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtXuatXu.requestFocus();
            return false;
        }
        
        if (txtHinhAnh.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đường dẫn hình ảnh!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtHinhAnh.requestFocus();
            return false;
        }
        
        if (txtThanhPhan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thành phần!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtThanhPhan.requestFocus();
            return false;
        }
        
        if (txtMoTa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mô tả!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtMoTa.requestFocus();
            return false;
        }
        
        if (txtMoTa.getText().trim().length() < 10) {
            JOptionPane.showMessageDialog(this, "Mô tả phải có ít nhất 10 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtMoTa.requestFocus();
            return false;
        }
        
        // Validate ngày tháng
        try {
            Date ngaySanXuat = dateFormat.parse(txtNgaySanXuat.getText().trim());
            Date hanSuDung = dateFormat.parse(txtHanSuDung.getText().trim());
            
            if (hanSuDung.before(ngaySanXuat)) {
                JOptionPane.showMessageDialog(this, "Hạn sử dụng phải sau ngày sản xuất!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public String getMaThuocMoi() {
        return maThuocMoi;
    }
}