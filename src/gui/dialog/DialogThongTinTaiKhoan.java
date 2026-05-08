package gui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DialogThongTinTaiKhoan extends JDialog {
    public DialogThongTinTaiKhoan(Frame parent, String tenDangNhap, String maNV, String vaiTro, String trangThai) {
        super(parent, "Thông tin tài khoản", true);
        initComponents(tenDangNhap, maNV, vaiTro, trangThai);
        setLocationRelativeTo(parent);
    }

    private void initComponents(String tenDangNhap, String maNV, String vaiTro, String trangThai) {
        setSize(600, 450);
        setResizable(false);
        getContentPane().setBackground(new Color(230, 245, 245));
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 0, 205));
        headerPanel.setPreferredSize(new Dimension(600, 60));

        JLabel lblTitle = new JLabel("THÔNG TIN TÀI KHOẢN");
        lblTitle.setFont(new Font("Roboto", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);

        Font labelFont = new Font("Roboto", Font.BOLD, 15);
        Font valueFont = new Font("Roboto", Font.PLAIN, 15);

        addInfoRow(contentPanel, gbc, 0, "Tên đăng nhập:", tenDangNhap, labelFont, valueFont);
        addInfoRow(contentPanel, gbc, 1, "Mã nhân viên:", maNV, labelFont, valueFont);
        addInfoRow(contentPanel, gbc, 2, "Vai trò:", vaiTro, labelFont, valueFont);
        addInfoRow(contentPanel, gbc, 3, "Trạng thái:", trangThai, labelFont, valueFont);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(230, 245, 245));

        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Roboto", Font.BOLD, 14));
        btnDong.setPreferredSize(new Dimension(140, 40));
        btnDong.setBackground(new Color(0, 0, 205));
        btnDong.setForeground(Color.WHITE);
        btnDong.setFocusPainted(false);
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.setBorder(new LineBorder(new Color(0, 0, 205), 2, true));
        btnDong.addActionListener(e -> dispose());

        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText,
            Font labelFont, Font valueFont) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(labelFont);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;

        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        panel.add(value, gbc);
    }
}
