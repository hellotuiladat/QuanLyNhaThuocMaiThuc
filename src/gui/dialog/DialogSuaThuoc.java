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

public class DialogSuaThuoc extends JDialog {
    private JTextField txtMaThuoc;
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
    
    private JButton btnCapNhat;
    private JButton btnHuy;
    
    private ThuocDAO thuocDAO;
    private DanhMucThuocDAO danhMucDAO;
    private ArrayList<DanhMucThuoc> dsDanhMuc;
    private Thuoc thuocHienTai;
    private boolean isSuccess = false;
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public DialogSuaThuoc(JFrame parent, Thuoc thuoc) {
        super(parent, "Sửa thông tin thuốc", true);
        this.thuocHienTai = thuoc;
        thuocDAO = new ThuocDAO();
        danhMucDAO = new DanhMucThuocDAO();
        
        initComponents();
        loadDanhMuc();
        loadDataToForm();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setSize(700, 800);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(230, 245, 245));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(700, 60));
        JLabel lblTitle = new JLabel("SỬA THÔNG TIN THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Mã thuốc (không cho sửa)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Mã thuốc:"), gbc);
        gbc.gridx = 1;
        txtMaThuoc = createTextField();
        txtMaThuoc.setEditable(false);
        txtMaThuoc.setBackground(new Color(240, 240, 240));
        formPanel.add(txtMaThuoc, gbc);
        
        // Tên thuốc
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Tên thuốc:"), gbc);
        gbc.gridx = 1;
        txtTenThuoc = createTextField();
        formPanel.add(txtTenThuoc, gbc);
        
        // Danh mục
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Danh mục:"), gbc);
        gbc.gridx = 1;
        cboxDanhMuc = new JComboBox<>();
        cboxDanhMuc.setPreferredSize(new Dimension(300, 35));
        cboxDanhMuc.setFont(new Font("Roboto", Font.PLAIN, 14));
        formPanel.add(cboxDanhMuc, gbc);
        
        // Đơn vị tính
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("Đơn vị tính:"), gbc);
        gbc.gridx = 1;
        txtDonViTinh = createTextField();
        formPanel.add(txtDonViTinh, gbc);
        
        // Xuất xứ
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(createLabel("Xuất xứ:"), gbc);
        gbc.gridx = 1;
        txtXuatXu = createTextField();
        formPanel.add(txtXuatXu, gbc);
        
        // Giá bán
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(createLabel("Giá bán:"), gbc);
        gbc.gridx = 1;
        txtGiaBan = createTextField();
        formPanel.add(txtGiaBan, gbc);
        
        // Số lượng
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(createLabel("Số lượng tồn:"), gbc);
        gbc.gridx = 1;
        txtSoLuong = createTextField();
        formPanel.add(txtSoLuong, gbc);
        
        // Ngày sản xuất
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(createLabel("Ngày sản xuất:"), gbc);
        gbc.gridx = 1;
        txtNgaySanXuat = createTextField();
        txtNgaySanXuat.setToolTipText("Định dạng: dd/MM/yyyy");
        formPanel.add(txtNgaySanXuat, gbc);
        
        // Hạn sử dụng
        gbc.gridx = 0; gbc.gridy = 8;
        formPanel.add(createLabel("Hạn sử dụng:"), gbc);
        gbc.gridx = 1;
        txtHanSuDung = createTextField();
        txtHanSuDung.setToolTipText("Định dạng: dd/MM/yyyy");
        formPanel.add(txtHanSuDung, gbc);
        
        // Hình ảnh
        gbc.gridx = 0; gbc.gridy = 9;
        formPanel.add(createLabel("Hình ảnh:"), gbc);
        gbc.gridx = 1;
        txtHinhAnh = createTextField();
        formPanel.add(txtHinhAnh, gbc);
        
        // Thành phần
        gbc.gridx = 0; gbc.gridy = 10;
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
        
        // Mô tả
        gbc.gridx = 0; gbc.gridy = 11;
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
        
        btnCapNhat = new JButton("CẬP NHẬT");
        btnCapNhat.setIcon(new FlatSVGIcon("./img/update.svg"));
        btnCapNhat.setFont(new Font("Roboto", Font.BOLD, 14));
        btnCapNhat.setPreferredSize(new Dimension(140, 40));
        btnCapNhat.setBackground(new Color(0, 0, 205));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCapNhat.setFocusPainted(false);
        btnCapNhat.addActionListener(e -> capNhatThuoc());
        
        btnHuy = new JButton("HỦY");
        btnHuy.setIcon(new FlatSVGIcon("./img/cancel.svg"));
        btnHuy.setFont(new Font("Roboto", Font.BOLD, 14));
        btnHuy.setPreferredSize(new Dimension(120, 40));
        btnHuy.setBackground(new Color(220, 53, 69));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setFocusPainted(false);
        btnHuy.addActionListener(e -> dispose());
        
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnHuy);
        
        add(headerPanel, BorderLayout.NORTH);
        add(scrollForm, BorderLayout.CENTER);
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
    
    private void loadDataToForm() {
        if (thuocHienTai != null) {
            txtMaThuoc.setText(thuocHienTai.getMaThuoc());
            txtTenThuoc.setText(thuocHienTai.getTenThuoc());
            txtDonViTinh.setText(thuocHienTai.getDonViTinh());
            txtGiaBan.setText(String.valueOf(thuocHienTai.getGiaBan()));
            txtSoLuong.setText(String.valueOf(thuocHienTai.getSoLuongTon()));
            txtNgaySanXuat.setText(dateFormat.format(thuocHienTai.getNgaySanXuat()));
            txtHanSuDung.setText(dateFormat.format(thuocHienTai.getHanSuDung()));
            txtXuatXu.setText(thuocHienTai.getXuatXu());
            txtHinhAnh.setText(thuocHienTai.getHinhAnh());
            txtThanhPhan.setText(thuocHienTai.getThanhPhan());
            txtMoTa.setText(thuocHienTai.getMoTa());
            
            // Set danh mục được chọn
            String tenDanhMuc = thuocHienTai.getDanhMucThuoc().getTenDanhMuc();
            cboxDanhMuc.setSelectedItem(tenDanhMuc);
        }
    }
    
    private void capNhatThuoc() {
        // Validate dữ liệu
        if (!validateData()) {
            return;
        }
        
        try {
            // Lấy dữ liệu từ form
            String maThuoc = txtMaThuoc.getText().trim();
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
            
            // Tạo đối tượng Thuoc với thông tin mới
            Thuoc thuocMoi = new Thuoc(maThuoc, tenThuoc, donViTinh, giaBan, soLuong, 
                                       sqlHanSuDung, moTa, danhMuc, hinhAnh, thanhPhan, 
                                       sqlNgaySanXuat, xuatXu);
            
            // Cập nhật vào database
            boolean success = thuocDAO.capNhatThuoc(thuocMoi);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật thuốc thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                isSuccess = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể cập nhật thuốc. Vui lòng thử lại!", 
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
                "Lỗi khi cập nhật thuốc: " + e.getMessage(), 
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
}