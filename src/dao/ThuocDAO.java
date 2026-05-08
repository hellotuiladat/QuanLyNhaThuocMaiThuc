package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ConnectDB.DatabaseConnection;
import entity.DanhMucThuoc;
import entity.Thuoc;

public class ThuocDAO {
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
    

    private java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new java.sql.Date(utilDate.getTime());
    }

    public String generateMaThuoc() throws SQLException {
        String sql = "SELECT dbo.fn_GenerateMaThuoc() AS maThuoc";
        try (Connection con = getSafeConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("maThuoc");
            }
        }
        throw new SQLException("Không thể tạo mã thuốc tự động");
    }
    
    public ArrayList<Thuoc> getDsThuoc() throws SQLException {
    	ArrayList<Thuoc> temp = new ArrayList<>();
    	String sql = "SELECT * FROM Thuoc";
    	try (Connection con = getSafeConnection()) {
    		Statement stmt = con.createStatement();
    		try (ResultSet rs = stmt.executeQuery(sql)) {
    			while (rs.next()) {
                    String mt = rs.getString("maThuoc");
                    String tenThuoc = rs.getString("tenThuoc");
                    String donViTinh = rs.getString("donViTinh");
                    double giaBan = rs.getDouble("giaBan");
                    int soLuong = rs.getInt("soLuongTon");
                    Date hanSuDung = rs.getDate("hanSuDung");
                    String moTa = rs.getString("moTa");
                    String maDanhMuc = rs.getString("maDanhMuc");
                    String hinhAnh = rs.getString("hinhAnh");
                    String thanhPhan = rs.getString("thanhPhan");
                    Date ngaySanXuat = rs.getDate("ngaySanXuat");
                    String xuatXu = rs.getString("xuatXu");
                    Thuoc thuoc = new Thuoc(mt, tenThuoc, donViTinh, giaBan, soLuong, hanSuDung, moTa, new DanhMucThuoc(maDanhMuc), hinhAnh, thanhPhan, ngaySanXuat, xuatXu);
                    temp.add(thuoc);
    			}
    		}
    	}
    	return temp;
    }
    
    public Thuoc getThuocTheoMaThuoc(String maThuoc) throws SQLException {
        String sql = "SELECT * FROM Thuoc WHERE maThuoc = ?";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String mt = rs.getString("maThuoc");
                    String tenThuoc = rs.getString("tenThuoc");
                    String donViTinh = rs.getString("donViTinh");
                    double giaBan = rs.getDouble("giaBan");
                    int soLuong = rs.getInt("soLuongTon");
                    Date hanSuDung = rs.getDate("hanSuDung");
                    String moTa = rs.getString("moTa");
                    String maDanhMuc = rs.getString("maDanhMuc");
                    String hinhAnh = rs.getString("hinhAnh");
                    String thanhPhan = rs.getString("thanhPhan");
                    Date ngaySanXuat = rs.getDate("ngaySanXuat");
                    String xuatXu = rs.getString("xuatXu");
                    
                    return new Thuoc(mt, tenThuoc, donViTinh, giaBan, soLuong, hanSuDung, moTa, new DanhMucThuoc(maDanhMuc), hinhAnh, thanhPhan, ngaySanXuat, xuatXu);
                }
            }
        }
        return null;
    }
    
    public boolean updateSoLuongTonTheoMaThuoc(String maThuoc, int soLuongNew) throws SQLException {
        String sql = "UPDATE Thuoc SET SoLuongTon = ? WHERE MaThuoc = ?";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, soLuongNew);
            stmt.setString(2, maThuoc);

            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        }
    }

    
    public int getSoLuongTonTheoMaThuoc(String maThuoc) throws SQLException {
        String sql = "SELECT SoLuongTon FROM Thuoc WHERE MaThuoc = ?";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SoLuongTon");
            }
        }
        return 0;
    }
    

    public String themThuoc(Thuoc thuoc) throws SQLException {
        // Lấy mã thuốc tự động
        String maThuocMoi = generateMaThuoc();
        
        String sql = "INSERT INTO Thuoc (maThuoc, tenThuoc, donViTinh, giaBan, soLuongTon, " +
                     "hanSuDung, moTa, maDanhMuc, hinhAnh, thanhPhan, ngaySanXuat, xuatXu) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuocMoi);
            stmt.setString(2, thuoc.getTenThuoc());
            stmt.setString(3, thuoc.getDonViTinh());
            stmt.setDouble(4, thuoc.getGiaBan());
            stmt.setInt(5, thuoc.getSoLuongTon());
            stmt.setDate(6, convertUtilDateToSqlDate(thuoc.getHanSuDung()));
            stmt.setString(7, thuoc.getMoTa());
            stmt.setString(8, thuoc.getDanhMucThuoc().getMaDanhMuc());
            stmt.setString(9, thuoc.getHinhAnh());
            stmt.setString(10, thuoc.getThanhPhan());
            stmt.setDate(11, convertUtilDateToSqlDate(thuoc.getNgaySanXuat()));
            stmt.setString(12, thuoc.getXuatXu());
            
            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0 ? maThuocMoi : null;
        }
    }
    
    /**
     * Cập nhật thông tin thuốc theo mã thuốc
     * @param thuoc Đối tượng Thuoc chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     * @throws SQLException
     */
    public boolean capNhatThuoc(Thuoc thuoc) throws SQLException {
        String sql = "UPDATE Thuoc SET tenThuoc = ?, donViTinh = ?, giaBan = ?, " +
                     "soLuongTon = ?, hanSuDung = ?, moTa = ?, maDanhMuc = ?, " +
                     "hinhAnh = ?, thanhPhan = ?, ngaySanXuat = ?, xuatXu = ? " +
                     "WHERE maThuoc = ?";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, thuoc.getTenThuoc());
            stmt.setString(2, thuoc.getDonViTinh());
            stmt.setDouble(3, thuoc.getGiaBan());
            stmt.setInt(4, thuoc.getSoLuongTon());
            stmt.setDate(5, convertUtilDateToSqlDate(thuoc.getHanSuDung()));
            stmt.setString(6, thuoc.getMoTa());
            stmt.setString(7, thuoc.getDanhMucThuoc().getMaDanhMuc());
            stmt.setString(8, thuoc.getHinhAnh());
            stmt.setString(9, thuoc.getThanhPhan());
            stmt.setDate(10, convertUtilDateToSqlDate(thuoc.getNgaySanXuat()));
            stmt.setString(11, thuoc.getXuatXu());
            stmt.setString(12, thuoc.getMaThuoc());
            
            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        }
    }
    
    /**
     * Xóa thuốc theo mã thuốc
     * @param maThuoc Mã thuốc cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     * @throws SQLException
     */
    public boolean xoaThuoc(String maThuoc) throws SQLException {
        String sql = "DELETE FROM Thuoc WHERE maThuoc = ?";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            
            int rowAffected = stmt.executeUpdate();
            return rowAffected > 0;
        }
    }
    
    /**
     * Kiểm tra xem thuốc có tồn tại trong cơ sở dữ liệu không
     * @param maThuoc Mã thuốc cần kiểm tra
     * @return true nếu tồn tại, false nếu không tồn tại
     * @throws SQLException
     */
    public boolean kiemTraTonTai(String maThuoc) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Thuoc WHERE maThuoc = ?";
        
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maThuoc);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}