package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ConnectDB.DatabaseConnection;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.PhieuDatThuoc;
import entity.Thue;

public class HoaDonDAO {
    
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
     * Lấy tất cả hóa đơn
     */
    public ArrayList<HoaDon> getDsHoaDon() throws SQLException {
        ArrayList<HoaDon> temp = new ArrayList<HoaDon>();
        String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC";
        try (Connection con = getSafeConnection()) {
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String maHD = rs.getString("maHD");
                    Date ngayLap = rs.getDate("ngayLap");
                    String maThue = rs.getString("maThue");
                    String maNhanVien = rs.getString("maNV");
                    String maKH = rs.getString("maKH");
                    String maKM = rs.getString("maKM");
                    String maPDT = rs.getString("maPhieuDat");
                    
                    HoaDon hd = new HoaDon(maHD, ngayLap, 
                        new Thue(maThue), 
                        new NhanVien(maNhanVien), 
                        maKH != null ? new KhachHang(maKH) : null,
                        maKM != null ? new KhuyenMai(maKM) : null, 
                        maPDT != null ? new PhieuDatThuoc(maPDT) : null);
                    temp.add(hd);
                }
            }
        }
        return temp;
    }
    
    /**
     * Lấy hóa đơn theo mã
     */
    public HoaDon getHoaDonTheoMa(String maHD) throws SQLException {
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHD);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date ngayLap = rs.getDate("ngayLap");
                    String maThue = rs.getString("maThue");
                    String maNhanVien = rs.getString("maNV");
                    String maKH = rs.getString("maKH");
                    String maKM = rs.getString("maKM");
                    String maPDT = rs.getString("maPhieuDat");
                    
                    return new HoaDon(maHD, ngayLap, 
                        new Thue(maThue), 
                        new NhanVien(maNhanVien), 
                        maKH != null ? new KhachHang(maKH) : null,
                        maKM != null ? new KhuyenMai(maKM) : null, 
                        maPDT != null ? new PhieuDatThuoc(maPDT) : null);
                }
            }
        }
        return null;
    }
    
    /**
     * Tạo mã hóa đơn tự động
     */
    public String generateMaHD() throws SQLException {
        String maHD = "HD00001";
        String sql = "SELECT dbo.fn_GenerateMaHD() AS MaHDMoi";
        
        try (Connection conn = getSafeConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maHD = rs.getString("MaHDMoi");
            }
        }
        return maHD;
    }
    
    /**
     * Thêm hóa đơn mới
     */
    public boolean themHoaDon(HoaDon hd) throws SQLException {
        String sql = "INSERT INTO HoaDon (maHD,ngayLap,maThue,maNV,maKH,maKM,maPhieuDat)" +
                     " VALUES(?,?,?,?,?,?,?)";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, hd.getMaHD());
            stmt.setDate(2, new java.sql.Date(hd.getNgayLap().getTime()));
            stmt.setString(3, hd.getThue().getMaThue());
            stmt.setString(4, hd.getNhanVien().getMaNV());
            
            if (hd.getKhachHang() != null && hd.getKhachHang().getMaKH() != null) {
                stmt.setString(5, hd.getKhachHang().getMaKH());
            } else {
                stmt.setNull(5, java.sql.Types.VARCHAR);
            }
            
            if (hd.getKhuyenMai() != null && hd.getKhuyenMai().getMaKM() != null) {
                stmt.setString(6, hd.getKhuyenMai().getMaKM());
            } else {
                stmt.setNull(6, java.sql.Types.VARCHAR);
            }
            
            if (hd.getPhieuDatThuoc() != null && hd.getPhieuDatThuoc().getMaPhieuDat() != null) {
                stmt.setString(7, hd.getPhieuDatThuoc().getMaPhieuDat());
            } else {
                stmt.setNull(7, java.sql.Types.VARCHAR);
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    /**
     * Tính tổng tiền của hóa đơn (từ chi tiết hóa đơn)
     */
    public double getTongTienHoaDon(String maHD) throws SQLException {
        String sql = "SELECT SUM(soLuong * donGia) AS tongTien FROM ChiTietHoaDon WHERE maHD = ?";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maHD);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("tongTien");
                }
            }
        }
        return 0;
    }
    
    /**
     * Tìm kiếm hóa đơn theo nhiều tiêu chí
     */
    public ArrayList<HoaDon> timKiemHoaDon(String tuKhoa, Date tuNgay, Date denNgay) throws SQLException {
        ArrayList<HoaDon> dsHD = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM HoaDon WHERE 1=1");
        
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            sql.append(" AND (maHD LIKE ? OR maNV LIKE ? OR maKH LIKE ?)");
        }
        
        if (tuNgay != null) {
            sql.append(" AND ngayLap >= ?");
        }
        
        if (denNgay != null) {
            sql.append(" AND ngayLap <= ?");
        }
        
        sql.append(" ORDER BY ngayLap DESC");
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            
            if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
                String keyword = "%" + tuKhoa + "%";
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
                stmt.setString(paramIndex++, keyword);
            }
            
            if (tuNgay != null) {
                stmt.setDate(paramIndex++, tuNgay);
            }
            
            if (denNgay != null) {
                stmt.setDate(paramIndex++, denNgay);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maHD = rs.getString("maHD");
                    Date ngayLap = rs.getDate("ngayLap");
                    String maThue = rs.getString("maThue");
                    String maNhanVien = rs.getString("maNV");
                    String maKH = rs.getString("maKH");
                    String maKM = rs.getString("maKM");
                    String maPDT = rs.getString("maPhieuDat");
                    
                    HoaDon hd = new HoaDon(maHD, ngayLap, 
                        new Thue(maThue), 
                        new NhanVien(maNhanVien), 
                        maKH != null ? new KhachHang(maKH) : null,
                        maKM != null ? new KhuyenMai(maKM) : null, 
                        maPDT != null ? new PhieuDatThuoc(maPDT) : null);
                    dsHD.add(hd);
                }
            }
        }
        return dsHD;
    }
}