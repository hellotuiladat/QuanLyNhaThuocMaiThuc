package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ConnectDB.DatabaseConnection;
import entity.ChiTietNhapThuoc;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhapThuoc;


public class PhieuNhapThuocDAO {
    
    private Connection getSafeConnection() throws SQLException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        if (conn == null || conn.isClosed()) {
            conn = DatabaseConnection.getInstance().getConnection();
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Không thể thiết lập kết nối đến cơ sở dữ liệu");
            }
        }
        return conn;
    }
    
    /**
     * Tự động phát sinh mã phiếu nhập
     */
    public String generateMaPhieuNhap() {
        String maPNH = "PN00001";
        String sql = "SELECT dbo.fn_GenerateMaPhieuNhap() AS MaPhieuNhapMoi";
        
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maPNH = rs.getString("MaPhieuNhapMoi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return maPNH;
    }
    
    /**
     * Lấy danh sách phiếu nhập thuốc
     */
    public ArrayList<PhieuNhapThuoc> getDSPhieuNhapThuoc() throws SQLException {
        ArrayList<PhieuNhapThuoc> temp = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhapHang ORDER BY maPhieuNhap DESC";
        
        try (Connection con = getSafeConnection()) {
             Statement stmt = con.createStatement();
             try (ResultSet rs = stmt.executeQuery(sql)) {
            
	            while (rs.next()) {
	                String maPhieuNhap = rs.getString("maPhieuNhap");
	                String maNV = rs.getString("maNV");
	                String maNCC = rs.getString("maNCC");
	                Date ngayNhap = rs.getDate("ngayNhap");
	                
	                PhieuNhapThuoc pnh = new PhieuNhapThuoc(maPhieuNhap, new NhanVien(maNV), new NhaCungCap(maNCC), ngayNhap);
	                temp.add(pnh);
	               
	            }
             }
        }
        return temp;
    }
    
    /**
     * Lấy chi tiết phiếu nhập thuốc
     */
    public ArrayList<ChiTietNhapThuoc> getChiTietPhieuNhap(String maPhieuNhap) throws SQLException {
        ArrayList<ChiTietNhapThuoc> temp = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE maPhieuNhap = ?";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuNhap);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maThuoc = rs.getString("maThuoc");
                    int soLuong = rs.getInt("soLuong");
                    double donGia = rs.getDouble("donGia");
                    
                    ChiTietNhapThuoc ctnh = new ChiTietNhapThuoc(maPhieuNhap, maThuoc, soLuong, donGia);
                    temp.add(ctnh);
                }
            }
        }
        return temp;
    }
    
    /**
     * Thêm phiếu nhập thuốc mới
     */
    public boolean themPhieuNhapThuoc(PhieuNhapThuoc pnh, ArrayList<ChiTietNhapThuoc> listCT) throws SQLException {
        String sqlPhieu = "INSERT INTO PhieuNhapHang(maPhieuNhap, maNV, maNCC, ngayNhap) VALUES (?,?,?,?)";
        String sqlCT = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap, maThuoc, soLuong, donGia) VALUES (?,?,?,?)";
        
        // ✅ Validate input
        if (pnh == null || pnh.getMaPhieuNhap() == null || pnh.getMaPhieuNhap().trim().isEmpty()) {
            throw new SQLException("Mã phiếu nhập không hợp lệ");
        }
        
        if (pnh.getNhanVien() == null || pnh.getNhaCungCap() == null) {
            throw new SQLException("Nhân viên hoặc nhà cung cấp không hợp lệ");
        }
        
        if (listCT == null || listCT.isEmpty()) {
            throw new SQLException("Phải có ít nhất một chi tiết phiếu nhập");
        }
        
        try (Connection con = getSafeConnection()) {
            con.setAutoCommit(false);
            
            try {
                // Thêm phiếu nhập
                try (PreparedStatement stmt = con.prepareStatement(sqlPhieu)) {
                    stmt.setString(1, pnh.getMaPhieuNhap());
                    stmt.setString(2, pnh.getNhanVien().getMaNV());
                    stmt.setString(3, pnh.getNhaCungCap().getMaNCC());
                    stmt.setDate(4, new Date(pnh.getNgayNhap().getTime()));
                    
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected == 0) {
                        con.rollback();
                        return false;
                    }
                }
                
                // Thêm chi tiết phiếu nhập
                for (ChiTietNhapThuoc ct : listCT) {
                    if (ct.getMaThuoc() == null || ct.getMaThuoc().trim().isEmpty()) {
                        con.rollback();
                        throw new SQLException("Mã thuốc không hợp lệ");
                    }
                    
                    if (ct.getSoLuong() <= 0 || ct.getDonGia() < 0) {
                        con.rollback();
                        throw new SQLException("Số lượng phải > 0 và giá không được âm");
                    }
                    
                    try (PreparedStatement stmt = con.prepareStatement(sqlCT)) {
                        stmt.setString(1, pnh.getMaPhieuNhap());
                        stmt.setString(2, ct.getMaThuoc());
                        stmt.setInt(3, ct.getSoLuong());
                        stmt.setDouble(4, ct.getDonGia());
                        
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected == 0) {
                            con.rollback();
                            return false;
                        }
                    }
                }
                
                con.commit();
                return true;
                
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
                throw e;
            }
        }
    }
    
    /**
     * Cập nhật phiếu nhập thuốc
     */
    public boolean capNhatPhieuNhapThuoc(PhieuNhapThuoc pnh, ArrayList<ChiTietNhapThuoc> listCT) throws SQLException {
        String sqlUpdatePhieu = "UPDATE PhieuNhapHang SET maNV=?, maNCC=?, ngayNhap=? WHERE maPhieuNhap=?";
        String sqlDeleteCT = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap=?";
        String sqlInsertCT = "INSERT INTO ChiTietPhieuNhap(maPhieuNhap, maThuoc, soLuong, donGia) VALUES (?,?,?,?)";
        
        // ✅ Validate input
        if (pnh == null || pnh.getMaPhieuNhap() == null || pnh.getMaPhieuNhap().trim().isEmpty()) {
            throw new SQLException("Mã phiếu nhập không hợp lệ");
        }
        
        if (pnh.getNhanVien() == null || pnh.getNhaCungCap() == null) {
            throw new SQLException("Nhân viên hoặc nhà cung cấp không hợp lệ");
        }
        
        if (listCT == null || listCT.isEmpty()) {
            throw new SQLException("Phải có ít nhất một chi tiết phiếu nhập");
        }
        
        // ✅ Sử dụng try-with-resources
        try (Connection con = getSafeConnection()) {
            con.setAutoCommit(false);
            
            try {
                // Cập nhật phiếu nhập
                try (PreparedStatement stmt = con.prepareStatement(sqlUpdatePhieu)) {
                    stmt.setString(1, pnh.getNhanVien().getMaNV());
                    stmt.setString(2, pnh.getNhaCungCap().getMaNCC());
                    stmt.setDate(3, new Date(pnh.getNgayNhap().getTime()));
                    stmt.setString(4, pnh.getMaPhieuNhap());
                    
                    stmt.executeUpdate();
                }
                
                // Xóa chi tiết cũ
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteCT)) {
                    stmt.setString(1, pnh.getMaPhieuNhap());
                    stmt.executeUpdate();
                }
                
                // Thêm chi tiết mới
                for (ChiTietNhapThuoc ct : listCT) {
                    // ✅ Validate chi tiết
                    if (ct.getMaThuoc() == null || ct.getMaThuoc().trim().isEmpty()) {
                        con.rollback();
                        throw new SQLException("Mã thuốc không hợp lệ");
                    }
                    
                    if (ct.getSoLuong() <= 0 || ct.getDonGia() < 0) {
                        con.rollback();
                        throw new SQLException("Số lượng phải > 0 và giá không được âm");
                    }
                    
                    try (PreparedStatement stmt = con.prepareStatement(sqlInsertCT)) {
                        stmt.setString(1, pnh.getMaPhieuNhap());
                        stmt.setString(2, ct.getMaThuoc());
                        stmt.setInt(3, ct.getSoLuong());
                        stmt.setDouble(4, ct.getDonGia());
                        
                        stmt.executeUpdate();
                    }
                }
                
                con.commit();
                return true;
                
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
                throw e;
            }
        }
    }
    
    /**
     * Xóa phiếu nhập thuốc (xóa cứng)
     */
    public boolean xoaPhieuNhapThuoc(String maPhieuNhap) throws SQLException {
        String sqlDeleteCT = "DELETE FROM ChiTietPhieuNhap WHERE maPhieuNhap=?";
        String sqlDeletePhieu = "DELETE FROM PhieuNhapHang WHERE maPhieuNhap=?";
        
        // ✅ Validate input
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty()) {
            throw new SQLException("Mã phiếu nhập không hợp lệ");
        }
        
        try (Connection con = getSafeConnection()) {
            con.setAutoCommit(false);
            
            try {
                // Xóa chi tiết trước
                try (PreparedStatement stmt = con.prepareStatement(sqlDeleteCT)) {
                    stmt.setString(1, maPhieuNhap);
                    stmt.executeUpdate();
                }
                
                // Xóa phiếu nhập
                try (PreparedStatement stmt = con.prepareStatement(sqlDeletePhieu)) {
                    stmt.setString(1, maPhieuNhap);
                    int rowsAffected = stmt.executeUpdate();
                    
                    if (rowsAffected == 0) {
                        con.rollback();
                        return false;
                    }
                }
                
                con.commit();
                return true;
                
            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                e.printStackTrace();
                throw e;
            }
        }
    }
    
    /**
     * Lấy phiếu nhập theo mã
     */
    public PhieuNhapThuoc getPhieuNhapTheoMa(String maPhieuNhap) throws SQLException {
        String sql = "SELECT * FROM PhieuNhapHang WHERE maPhieuNhap = ?";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuNhap);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String maNCC = rs.getString("maNCC");
                    Date ngayNhap = rs.getDate("ngayNhap");

                    return new PhieuNhapThuoc(maPhieuNhap, new NhanVien(maNV), new NhaCungCap(maNCC), ngayNhap);
                    
                }
            }
        }
        return null;
    }
    
    /**
     * Kiểm tra mã phiếu nhập có tồn tại không
     */
    public boolean kiemTraMaPhieuNhapTonTai(String maPhieuNhap) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuNhapHang WHERE maPhieuNhap = ?";
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, maPhieuNhap);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Tìm kiếm phiếu nhập theo mã hoặc ngày
     */
    public ArrayList<PhieuNhapThuoc> timKiemPhieuNhap(String maPhieuNhap, String maNV, String maNCC, 
                                                       java.util.Date ngayTu, java.util.Date ngayDen) throws SQLException {
        ArrayList<PhieuNhapThuoc> ketQua = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM PhieuNhapHang WHERE 1=1");
        ArrayList<Object> params = new ArrayList<>();
        
        if (maPhieuNhap != null && !maPhieuNhap.isEmpty()) {
            sql.append(" AND maPhieuNhap LIKE ?");
            params.add("%" + maPhieuNhap + "%");
        }
        
        if (maNV != null && !maNV.isEmpty()) {
            sql.append(" AND maNV = ?");
            params.add(maNV);
        }
        
        if (maNCC != null && !maNCC.isEmpty()) {
            sql.append(" AND maNCC = ?");
            params.add(maNCC);
        }
        
        if (ngayTu != null) {
            sql.append(" AND ngayNhap >= ?");
            params.add(new Date(ngayTu.getTime()));
        }
        
        if (ngayDen != null) {
            sql.append(" AND ngayNhap <= ?");
            params.add(new Date(ngayDen.getTime()));
        }
        
        sql.append(" ORDER BY maPhieuNhap DESC");
        
        try (Connection con = getSafeConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maPN = rs.getString("maPhieuNhap");
                    String maNVRS = rs.getString("maNV");
                    String maNccRS = rs.getString("maNCC");
                    Date ngayNhap = rs.getDate("ngayNhap");
                    
                    PhieuNhapThuoc pnh = new PhieuNhapThuoc(maPN, new NhanVien(maNVRS), new NhaCungCap(maNccRS), ngayNhap);
                    ketQua.add(pnh);
                    
                }
            }
        }
        return ketQua;
    }
    
}