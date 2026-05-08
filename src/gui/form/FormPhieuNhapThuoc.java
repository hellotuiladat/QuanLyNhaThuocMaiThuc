package gui.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import dao.PhieuNhapThuocDAO;
import entity.PhieuNhapThuoc;
import gui.dialog.DialogThemPhieuNhap;
import gui.dialog.DialogCapNhatPhieuNhap;
import gui.dialog.DialogThongTinPhieuNhap;

public class FormPhieuNhapThuoc extends JPanel {
    private JPanel actionPanel;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnInfo;
    private JButton btnReload;
    private JButton btnUpdate;
    private JComboBox<String> cboxSearch;
    private JPanel headerPanel;
    private JPanel jPanel1;
    private JPanel jPanel3;
    private JPanel jPanel5;
    private JScrollPane jScrollPane1;
    private JLabel lblTable;
    private JTable table;
    private JPanel tablePanel;
    private JTextField txtSearch;
    private DefaultTableModel tableModel;
    private PhieuNhapThuocDAO pnhDAO;
    
    Font headerTable = new Font("Roboto", Font.BOLD, 18);
    
    public FormPhieuNhapThuoc() {
        this.pnhDAO = new PhieuNhapThuocDAO();
        taoNoiDung();
        setupEventHandlers();
        loadTableData();
    }

    private void taoNoiDung() {
        headerPanel = new JPanel();
        jPanel1 = new JPanel();
        jPanel3 = new JPanel();
        cboxSearch = new JComboBox<>();
        txtSearch = new JTextField();
        btnReload = new JButton();
        actionPanel = new JPanel();
        btnAdd = new JButton();
        btnUpdate = new JButton();
        btnDelete = new JButton();
        btnInfo = new JButton();
        tablePanel = new JPanel();
        jScrollPane1 = new JScrollPane();
        table = new JTable();
        jPanel5 = new JPanel();
        lblTable = new JLabel();

        setBackground(new Color(230, 245, 245));
        setBorder(new LineBorder(new Color(230, 245, 245), 6, true));
        setMinimumSize(new Dimension(1130, 800));
        setPreferredSize(new Dimension(1130, 800));
        setLayout(new BorderLayout(0, 10));

        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(new LineBorder(new Color(232, 232, 232), 2, true));
        headerPanel.setLayout(new BorderLayout());

        jPanel1.setBackground(new Color(255, 255, 255));
        jPanel1.setPreferredSize(new Dimension(590, 100));
        jPanel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 32, 30));

        jPanel3.setBackground(new Color(255, 255, 255));
        jPanel3.setPreferredSize(new Dimension(584, 50));
        jPanel3.setLayout(new FlowLayout(FlowLayout.TRAILING, 24, 5));

        cboxSearch.setToolTipText("");
        cboxSearch.setPreferredSize(new Dimension(120, 40));
        String[] searchType = {"Tất cả", "Mã phiếu", "Mã nhân viên", "Mã nhà cung cấp", "Ngày nhập"};
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(searchType);
        cboxSearch.setModel(model);
        jPanel3.add(cboxSearch);

        txtSearch.setToolTipText("Tìm kiếm");
        txtSearch.setPreferredSize(new Dimension(240, 40));
        txtSearch.setSelectionColor(new Color(230, 245, 245));
        jPanel3.add(txtSearch);

        btnReload.setIcon(new FlatSVGIcon(getClass().getResource("/img/reload.svg")));
        btnReload.setToolTipText("Làm mới");
        btnReload.setBorder(null);
        btnReload.setBorderPainted(false);
        btnReload.setContentAreaFilled(false);
        btnReload.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReload.setFocusPainted(false);
        btnReload.setFocusable(false);
        btnReload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReload.setPreferredSize(new Dimension(48, 48));
        jPanel3.add(btnReload);

        jPanel1.add(jPanel3);

        headerPanel.add(jPanel1, BorderLayout.CENTER);

        actionPanel.setBackground(new Color(255, 255, 255));
        actionPanel.setPreferredSize(new Dimension(600, 100));
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 15));

        btnAdd.setFont(new Font("Roboto", Font.BOLD, 14));
        btnAdd.setIcon(new FlatSVGIcon(getClass().getResource("/img/add.svg")));
        btnAdd.setText("THÊM");
        btnAdd.setBorder(null);
        btnAdd.setBorderPainted(false);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setFocusPainted(false);
        btnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdd.setPreferredSize(new Dimension(90, 90));
        btnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnAdd);

        btnUpdate.setFont(new Font("Roboto", Font.BOLD, 14));
        btnUpdate.setIcon(new FlatSVGIcon(getClass().getResource("/img/update.svg")));
        btnUpdate.setText("SỬA");
        btnUpdate.setBorder(null);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setContentAreaFilled(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setFocusPainted(false);
        btnUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUpdate.setPreferredSize(new Dimension(90, 90));
        btnUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnUpdate);

        btnDelete.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDelete.setIcon(new FlatSVGIcon(getClass().getResource("/img/delete.svg")));
        btnDelete.setText("XÓA");
        btnDelete.setBorder(null);
        btnDelete.setBorderPainted(false);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setFocusPainted(false);
        btnDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDelete.setPreferredSize(new Dimension(90, 90));
        btnDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnDelete);

        btnInfo.setFont(new Font("Roboto", Font.BOLD, 14));
        btnInfo.setIcon(new FlatSVGIcon(getClass().getResource("/img/info.svg")));
        btnInfo.setText("INFO");
        btnInfo.setBorder(null);
        btnInfo.setBorderPainted(false);
        btnInfo.setContentAreaFilled(false);
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.setFocusPainted(false);
        btnInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInfo.setPreferredSize(new Dimension(90, 90));
        btnInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        actionPanel.add(btnInfo);

        headerPanel.add(actionPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.PAGE_START);

        tablePanel.setBorder(new LineBorder(new Color(230, 230, 230), 2, true));
        tablePanel.setLayout(new BorderLayout());
        
        String[] tableTitle = {"Mã phiếu nhập", "Mã nhân viên", "Mã nhà cung cấp", "Ngày nhập"};
        tableModel = new DefaultTableModel(tableTitle, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        table.getTableHeader().setFont(headerTable);
        table.getTableHeader().setBackground(new Color(0, 0, 205));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setModel(tableModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        table.setFocusable(false);
        table.setRowHeight(40);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        
        jScrollPane1.setViewportView(table);

        tablePanel.add(jScrollPane1, BorderLayout.CENTER);

        jPanel5.setBackground(new Color(0, 0, 205));
        jPanel5.setMinimumSize(new Dimension(100, 60));
        jPanel5.setPreferredSize(new Dimension(500, 40));
        jPanel5.setLayout(new BorderLayout());

        lblTable.setFont(new Font("Roboto Medium", Font.BOLD, 30));
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTable.setText("THÔNG TIN PHIẾU NHẬP THUỐC");
        jPanel5.add(lblTable, BorderLayout.CENTER);

        tablePanel.add(jPanel5, BorderLayout.NORTH);

        add(tablePanel, BorderLayout.CENTER);
    }

    /**
     * Thiết lập sự kiện cho các button
     */
    private void setupEventHandlers() {
        // Nút thêm phiếu nhập
        btnAdd.addActionListener(e -> xuLyThemPhieuNhap());

        // Nút sửa phiếu nhập
        btnUpdate.addActionListener(e -> xuLyCapNhatPhieuNhap());

        // Nút xóa phiếu nhập
        btnDelete.addActionListener(e -> xuLyXoaPhieuNhap());

        // Nút xem chi tiết
        btnInfo.addActionListener(e -> xuLyXemChiTiet());

        // Nút làm mới
        btnReload.addActionListener(e -> reloadTable());

        // Tìm kiếm khi nhập text
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                xuLyTimKiem();
            }
        });

        // Tìm kiếm khi thay đổi combo box
        cboxSearch.addActionListener(e -> {
            txtSearch.setText("");
            loadTableData();
        });
    }

    /**
     * Xử lý thêm phiếu nhập thuốc mới
     */
    private void xuLyThemPhieuNhap() {
        DialogThemPhieuNhap dialog = new DialogThemPhieuNhap(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            this
        );
        dialog.setVisible(true);
    }

    /**
     * Xử lý cập nhật phiếu nhập thuốc
     */
    private void xuLyCapNhatPhieuNhap() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, 
                "Vui lòng chọn phiếu nhập để sửa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String maPhieuNhap = (String) table.getValueAt(selectedRow, 0);
            PhieuNhapThuoc pnh = pnhDAO.getPhieuNhapTheoMa(maPhieuNhap);
            
            if (pnh == null) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Không tìm thấy phiếu nhập này!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            DialogCapNhatPhieuNhap dialog = new DialogCapNhatPhieuNhap(
                (Frame) SwingUtilities.getWindowAncestor(this), 
                this, 
                pnh
            );
            dialog.setVisible(true);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, 
                "Lỗi khi tải dữ liệu: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Xử lý xóa phiếu nhập thuốc
     */
    private void xuLyXoaPhieuNhap() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, 
                "Vui lòng chọn phiếu nhập để xóa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String maPhieuNhap = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc muốn xóa phiếu nhập này?\n\nMã phiếu: " + maPhieuNhap, 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = pnhDAO.xoaPhieuNhapThuoc(maPhieuNhap);

                if (result) {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Xóa phiếu nhập thành công!", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    reloadTable();
                } else {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Xóa phiếu nhập thất bại!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    this, 
                    "Lỗi khi xóa: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * Xử lý xem chi tiết phiếu nhập
     */
    private void xuLyXemChiTiet() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, 
                "Vui lòng chọn phiếu nhập để xem!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String maPhieuNhap = (String) table.getValueAt(selectedRow, 0);
            PhieuNhapThuoc pnh = pnhDAO.getPhieuNhapTheoMa(maPhieuNhap);
            
            if (pnh == null) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Không tìm thấy phiếu nhập này!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            
            DialogThongTinPhieuNhap dialog = new DialogThongTinPhieuNhap(
                (Frame) SwingUtilities.getWindowAncestor(this), 
                pnh
            );
            dialog.setVisible(true);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, 
                "Lỗi khi tải dữ liệu: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Xử lý tìm kiếm phiếu nhập
     */
    private void xuLyTimKiem() {
        String tuKhoa = txtSearch.getText().trim();
        
        if (tuKhoa.isEmpty()) {
            loadTableData();
            return;
        }

        try {
            String searchType = (String) cboxSearch.getSelectedItem();
            ArrayList<PhieuNhapThuoc> ketQua = new ArrayList<>();

            if (searchType.equals("Mã phiếu")) {
                ketQua = pnhDAO.timKiemPhieuNhap(tuKhoa, null, null, null, null);
            } else if (searchType.equals("Mã nhân viên")) {
                ketQua = pnhDAO.timKiemPhieuNhap(null, tuKhoa, null, null, null);
            } else if (searchType.equals("Mã nhà cung cấp")) {
                ketQua = pnhDAO.timKiemPhieuNhap(null, null, tuKhoa, null, null);
            } else {
                ketQua = pnhDAO.getDSPhieuNhapThuoc();
            }

            loadTableFromList(ketQua);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, 
                "Lỗi khi tìm kiếm: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Tải dữ liệu vào bảng từ database
     */
    public void loadTableData() {
        try {
            ArrayList<PhieuNhapThuoc> list = pnhDAO.getDSPhieuNhapThuoc();
            loadTableFromList(list);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this, 
                "Lỗi khi tải dữ liệu: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Tải dữ liệu vào bảng từ một danh sách
     */
    private void loadTableFromList(ArrayList<PhieuNhapThuoc> list) {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (PhieuNhapThuoc pnh : list) {
            Object[] row = {
                pnh.getMaPhieuNhap(),
                pnh.getNhanVien() != null ? pnh.getNhanVien().getMaNV() : "",
                pnh.getNhaCungCap() != null ? pnh.getNhaCungCap().getMaNCC() : "",
                sdf.format(pnh.getNgayNhap())
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Làm mới bảng dữ liệu
     */
    public void reloadTable() {
        txtSearch.setText("");
        cboxSearch.setSelectedIndex(0);
        loadTableData();
    }
}