package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.DanhMucThuocDAO;
import dao.ThuocDAO;
import entity.DanhMucThuoc;
import entity.Thuoc;

public class DialogThongTinChiTietThuoc extends JDialog {
    private JLabel lblMaThuoc, lblTenThuoc, lblDanhMuc, lblXuatXu, lblGiaBan, lblSoLuong,
                   lblNgaySanXuat, lblHanSuDung, lblDonViTinh;
    private JTextArea txtMoTa, txtThanhPhan;
    private ThuocDAO thuocDAO;
    private DanhMucThuocDAO dmtDAO;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public DialogThongTinChiTietThuoc(JFrame parent, String maThuoc) {
        super(parent, "Chi tiết thông tin thuốc", true);
        
        dmtDAO = new DanhMucThuocDAO();
        thuocDAO = new ThuocDAO();
        
        initComponents();
        loadData(maThuoc);
        
        setSize(900, 700);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        
        JLabel lblTitle = new JLabel("CHI TIẾT THÔNG TIN THUỐC");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setIcon(new FlatSVGIcon("./img/info.svg", 32, 32));
        headerPanel.add(lblTitle);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Thông tin cơ bản
        JPanel basicInfoPanel = new JPanel();
        basicInfoPanel.setLayout(new GridLayout(5, 4, 15, 10));
        basicInfoPanel.setBackground(Color.WHITE);
        basicInfoPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Thông tin cơ bản",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        // Row 1
        basicInfoPanel.add(createInfoLabel("Mã thuốc:"));
        lblMaThuoc = createValueLabel("");
        basicInfoPanel.add(lblMaThuoc);
        
        basicInfoPanel.add(createInfoLabel("Tên thuốc:"));
        lblTenThuoc = createValueLabel("");
        basicInfoPanel.add(lblTenThuoc);
        
        // Row 2
        basicInfoPanel.add(createInfoLabel("Danh mục:"));
        lblDanhMuc = createValueLabel("");
        basicInfoPanel.add(lblDanhMuc);
        
        basicInfoPanel.add(createInfoLabel("Đơn vị tính:"));
        lblDonViTinh = createValueLabel("");
        basicInfoPanel.add(lblDonViTinh);
        
        // Row 3
        basicInfoPanel.add(createInfoLabel("Xuất xứ:"));
        lblXuatXu = createValueLabel("");
        basicInfoPanel.add(lblXuatXu);
        
        basicInfoPanel.add(createInfoLabel("Giá bán:"));
        lblGiaBan = createMoneyLabel("");
        basicInfoPanel.add(lblGiaBan);
        
        // Row 4
        basicInfoPanel.add(createInfoLabel("Số lượng tồn:"));
        lblSoLuong = createValueLabel("");
        basicInfoPanel.add(lblSoLuong);
        
        basicInfoPanel.add(createInfoLabel("Ngày sản xuất:"));
        lblNgaySanXuat = createValueLabel("");
        basicInfoPanel.add(lblNgaySanXuat);
        
        // Row 5
        basicInfoPanel.add(createInfoLabel("Hạn sử dụng:"));
        lblHanSuDung = createValueLabel("");
        basicInfoPanel.add(lblHanSuDung);
        
        basicInfoPanel.add(new JLabel("")); // Empty cell
        basicInfoPanel.add(new JLabel("")); // Empty cell
        
        infoPanel.add(basicInfoPanel, BorderLayout.NORTH);
        
        // Thông tin chi tiết
        JPanel detailPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        detailPanel.setBackground(Color.WHITE);
        
        // Thành phần
        JPanel thanhPhanPanel = new JPanel(new BorderLayout());
        thanhPhanPanel.setBackground(Color.WHITE);
        thanhPhanPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Thành phần",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        txtThanhPhan = new JTextArea();
        txtThanhPhan.setFont(new Font("Roboto", Font.PLAIN, 13));
        txtThanhPhan.setLineWrap(true);
        txtThanhPhan.setWrapStyleWord(true);
        txtThanhPhan.setEditable(false);
        txtThanhPhan.setBackground(new Color(250, 250, 250));
        txtThanhPhan.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollThanhPhan = new JScrollPane(txtThanhPhan);
        scrollThanhPhan.setPreferredSize(new Dimension(850, 100));
        thanhPhanPanel.add(scrollThanhPhan, BorderLayout.CENTER);
        
        // Mô tả
        JPanel moTaPanel = new JPanel(new BorderLayout());
        moTaPanel.setBackground(Color.WHITE);
        moTaPanel.setBorder(new TitledBorder(
            new LineBorder(new Color(0, 0, 205), 2, true),
            "Mô tả",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 14),
            new Color(0, 0, 205)
        ));
        
        txtMoTa = new JTextArea();
        txtMoTa.setFont(new Font("Roboto", Font.PLAIN, 13));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setEditable(false);
        txtMoTa.setBackground(new Color(250, 250, 250));
        txtMoTa.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setPreferredSize(new Dimension(850, 100));
        moTaPanel.add(scrollMoTa, BorderLayout.CENTER);
        
        detailPanel.add(thanhPhanPanel);
        detailPanel.add(moTaPanel);
        
        infoPanel.add(detailPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnClose = new JButton("ĐÓNG");
        btnClose.setFont(new Font("Roboto", Font.BOLD, 13));
        btnClose.setPreferredSize(new Dimension(120, 40));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        
        buttonPanel.add(btnClose);
        
        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.PLAIN, 13));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }
    
    private JLabel createMoneyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.BOLD, 14));
        label.setForeground(new Color(220, 53, 69));
        return label;
    }
    
    private void loadData(String maThuoc) {
        try {
            // Lấy thông tin thuốc
            Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(maThuoc);
            
            if (thuoc == null) {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy thuốc!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            
            // Hiển thị thông tin thuốc
            lblMaThuoc.setText(thuoc.getMaThuoc());
            lblTenThuoc.setText(thuoc.getTenThuoc());
            lblDonViTinh.setText(thuoc.getDonViTinh());
            lblXuatXu.setText(thuoc.getXuatXu());
            lblGiaBan.setText(String.format("%,.0f VNĐ", thuoc.getGiaBan()));
            lblSoLuong.setText(String.valueOf(thuoc.getSoLuongTon()));
            
            // Lấy tên danh mục
            if (thuoc.getDanhMucThuoc() != null && thuoc.getDanhMucThuoc().getMaDanhMuc() != null) {
                DanhMucThuoc dmt = dmtDAO.getDanhMucThuocQuaMaDanhMuc(thuoc.getDanhMucThuoc().getMaDanhMuc());
                if (dmt != null) {
                    lblDanhMuc.setText(dmt.getTenDanhMuc());
                } else {
                    lblDanhMuc.setText(thuoc.getDanhMucThuoc().getMaDanhMuc());
                }
            } else {
                lblDanhMuc.setText("N/A");
            }
            
            // Hiển thị ngày tháng
            if (thuoc.getNgaySanXuat() != null) {
                lblNgaySanXuat.setText(dateFormat.format(thuoc.getNgaySanXuat()));
            } else {
                lblNgaySanXuat.setText("N/A");
            }
            
            if (thuoc.getHanSuDung() != null) {
                lblHanSuDung.setText(dateFormat.format(thuoc.getHanSuDung()));
            } else {
                lblHanSuDung.setText("N/A");
            }
            
            // Hiển thị thành phần và mô tả
            txtThanhPhan.setText(thuoc.getThanhPhan() != null ? thuoc.getThanhPhan() : "Không có thông tin");
            txtMoTa.setText(thuoc.getMoTa() != null ? thuoc.getMoTa() : "Không có thông tin");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi khi tải dữ liệu: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}