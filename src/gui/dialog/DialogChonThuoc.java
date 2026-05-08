package gui.dialog;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import dao.ThuocDAO;
import entity.Thuoc;

public class DialogChonThuoc extends JDialog {
    private ThuocDAO thuocDAO;
    private JTable tableThuoc;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JButton btnChon, btnHuy;
    private NumberFormat currencyFormat;
    private List<Thuoc> thuocDaChon;
    
    public DialogChonThuoc(JDialog parent) {
        super(parent, "Chọn thuốc thay thế", true);
        this.thuocDAO = new ThuocDAO();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.thuocDaChon = new ArrayList<>();
        
        initComponents();
        setupEventHandlers();
        loadData();
        
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(800, 600);
        
        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        
        // Panel bảng thuốc
        JPanel tablePanel = createTablePanel();
        
        // Panel nút
        JPanel buttonPanel = createButtonPanel();
        
        add(searchPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm thuốc"));
        panel.setBackground(Color.WHITE);
        
        panel.add(new JLabel("Tìm kiếm:"));
        
        txtTimKiem = new JTextField(30);
        txtTimKiem.setToolTipText("Nhập tên thuốc hoặc mã thuốc");
        panel.add(txtTimKiem);
        
        JButton btnTimKiem = new JButton("Tìm");
        btnTimKiem.addActionListener(this::btnTimKiemActionPerformed);
        panel.add(btnTimKiem);
        
        JButton btnLamMoi = new JButton("Làm mới");
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadData();
        });
        panel.add(btnLamMoi);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách thuốc"));
        panel.setBackground(Color.WHITE);
        
        // Bảng thuốc
        String[] columnNames = {"Chọn", "Mã thuốc", "Tên thuốc", "Đơn vị tính", "Tồn kho", "Giá bán", "Hạn sử dụng"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Chỉ cho phép chỉnh sửa cột checkbox
            }
        };
        
        tableThuoc = new JTable(tableModel);
        tableThuoc.setFont(new Font("Arial", Font.PLAIN, 12));
        tableThuoc.setRowHeight(25);
        tableThuoc.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableThuoc.getTableHeader().setBackground(new Color(240, 240, 240));
        tableThuoc.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Custom renderer cho cột giá bán
        tableThuoc.getColumnModel().getColumn(5).setCellRenderer(new CurrencyRenderer());
        
        // Custom renderer cho cột tồn kho
        tableThuoc.getColumnModel().getColumn(4).setCellRenderer(new StockRenderer());
        
        // Set width cho các cột
        tableThuoc.getColumnModel().getColumn(0).setPreferredWidth(50);   // Checkbox
        tableThuoc.getColumnModel().getColumn(1).setPreferredWidth(80);   // Mã thuốc
        tableThuoc.getColumnModel().getColumn(2).setPreferredWidth(200);  // Tên thuốc
        tableThuoc.getColumnModel().getColumn(3).setPreferredWidth(80);   // Đơn vị tính
        tableThuoc.getColumnModel().getColumn(4).setPreferredWidth(80);   // Tồn kho
        tableThuoc.getColumnModel().getColumn(5).setPreferredWidth(100);  // Giá bán
        tableThuoc.getColumnModel().getColumn(6).setPreferredWidth(100);  // Hạn sử dụng
        
        JScrollPane scrollPane = new JScrollPane(tableThuoc);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel thông tin
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblInfo = new JLabel("💡 Chọn các thuốc bạn muốn sử dụng để thay thế. Chỉ hiển thị thuốc còn tồn kho.");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        infoPanel.add(lblInfo);
        
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        
        btnChon = new JButton("Chọn thuốc");
        btnChon.setBackground(new Color(0, 123, 255));
        btnChon.setForeground(Color.WHITE);
        btnChon.setPreferredSize(new Dimension(120, 35));
        btnChon.addActionListener(this::btnChonActionPerformed);
        
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(108, 117, 125));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setPreferredSize(new Dimension(80, 35));
        btnHuy.addActionListener(e -> dispose());
        
        panel.add(btnChon);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnHuy);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Enter trong ô tìm kiếm
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnTimKiemActionPerformed(null);
                }
            }
        });
        
        // Double click để chọn thuốc
        tableThuoc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableThuoc.getSelectedRow();
                    if (row != -1) {
                        // Toggle checkbox
                        boolean currentValue = (Boolean) tableModel.getValueAt(row, 0);
                        tableModel.setValueAt(!currentValue, row, 0);
                    }
                }
            }
        });
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            
            List<Thuoc> danhSachThuoc = thuocDAO.getDsThuoc();
            
            for (Thuoc thuoc : danhSachThuoc) {
                // Chỉ hiển thị thuốc còn tồn kho
                if (thuoc.getSoLuongTon() > 0) {
                    Object[] row = {
                        false, // Checkbox
                        thuoc.getMaThuoc(),
                        thuoc.getTenThuoc(),
                        thuoc.getDonViTinh(),
                        thuoc.getSoLuongTon(),
                        thuoc.getGiaBan(),
                        thuoc.getHanSuDung() != null ? 
                            new java.text.SimpleDateFormat("dd/MM/yyyy").format(thuoc.getHanSuDung()) : "Không xác định"
                    };
                    tableModel.addRow(row);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách thuốc: " + e.getMessage());
        }
    }
    
    private void btnTimKiemActionPerformed(ActionEvent e) {
        try {
            String keyword = txtTimKiem.getText().trim().toLowerCase();
            tableModel.setRowCount(0);
            
            List<Thuoc> danhSachThuoc = thuocDAO.getDsThuoc();
            
            for (Thuoc thuoc : danhSachThuoc) {
                // Chỉ hiển thị thuốc còn tồn kho và khớp với từ khóa
                if (thuoc.getSoLuongTon() > 0 && 
                    (keyword.isEmpty() || 
                     thuoc.getTenThuoc().toLowerCase().contains(keyword) ||
                     thuoc.getMaThuoc().toLowerCase().contains(keyword))) {
                    
                    Object[] row = {
                        false, // Checkbox
                        thuoc.getMaThuoc(),
                        thuoc.getTenThuoc(),
                        thuoc.getDonViTinh(),
                        thuoc.getSoLuongTon(),
                        thuoc.getGiaBan(),
                        thuoc.getHanSuDung() != null ? 
                            new java.text.SimpleDateFormat("dd/MM/yyyy").format(thuoc.getHanSuDung()) : "Không xác định"
                    };
                    tableModel.addRow(row);
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }
    
    private void btnChonActionPerformed(ActionEvent e) {
        thuocDaChon.clear();
        
        // Lấy danh sách thuốc được chọn
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Boolean isChecked = (Boolean) tableModel.getValueAt(i, 0);
            if (isChecked != null && isChecked) {
                try {
                    String maThuoc = tableModel.getValueAt(i, 1).toString();
                    Thuoc thuoc = thuocDAO.getThuocTheoMaThuoc(maThuoc);
                    if (thuoc != null) {
                        thuocDaChon.add(thuoc);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        if (thuocDaChon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một thuốc!");
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
            "Đã chọn " + thuocDaChon.size() + " thuốc thành công!",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
    
    public List<Thuoc> getThuocDaChon() {
        return thuocDaChon;
    }
    
    // Custom renderer cho cột tiền
    private class CurrencyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Double) {
                setText(currencyFormat.format((Double) value));
            }
            
            setHorizontalAlignment(RIGHT);
            return this;
        }
    }
    
    // Custom renderer cho cột tồn kho
    private class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof Integer) {
                int stock = (Integer) value;
                setText(String.valueOf(stock));
                
                // Màu sắc theo tồn kho
                if (stock == 0) {
                    setForeground(Color.RED);
                } else if (stock < 10) {
                    setForeground(new Color(255, 140, 0)); // Orange
                } else {
                    setForeground(new Color(0, 128, 0)); // Green
                }
            }
            
            setHorizontalAlignment(CENTER);
            return this;
        }
    }
}