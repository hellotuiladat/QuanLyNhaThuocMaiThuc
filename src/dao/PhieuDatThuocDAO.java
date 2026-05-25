package dao;

import java.sql.Connection;
import java.sql.Date; // Lưu ý: Dùng java.sql.Date cho JDBC
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ConnectDB.DatabaseConnection;
import entity.ChiTietPhieuDatThuoc; // Import thêm entity này
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatThuoc;

public class PhieuDatThuocDAO {

    // ... (Giữ nguyên các hàm getSafeConnection, generateMaPhieuDat, getDsPhieuDatThuoc...)

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

    public String generateMaPhieuDat() throws SQLException {
        String sql = "select dbo.fn_GenerateMaPhieuDat() as MaPhieuDatMoi";
        try (Connection con = getSafeConnection()) {
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getString("MaPhieuDatMoi");
                }
            }
        }
        return null;
    }

    public ArrayList<PhieuDatThuoc> getDsPhieuDatThuoc() throws Exception {
        ArrayList<PhieuDatThuoc> temp = new ArrayList<PhieuDatThuoc>();
        String sql = "SELECT * FROM PhieuDatThuoc ORDER BY CAST(SUBSTRING(maPhieuDat, 4, 5) AS INT) DESC";
        try (Connection con = getSafeConnection()) {
            damBaoCotMaNhanVien(con);
            Statement stmt = con.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String maPhieuDat = rs.getString("maPhieuDat");
                    Date ngayDat = rs.getDate("ngayDat");
                    String maKH = rs.getString("maKH");
                    String diaChi = rs.getString("diaChi");
                    String hinhThucThanhToan = rs.getString("hinhThucThanhToan");
                    String trangThai = rs.getString("trangThai");
                    String maNV = rs.getString("maNV");
                    PhieuDatThuoc pdt = new PhieuDatThuoc(maPhieuDat, ngayDat, new KhachHang(maKH),
                            maNV == null ? null : new NhanVien(maNV), diaChi, hinhThucThanhToan, trangThai);
                    temp.add(pdt);
                }
            }
        }
        return temp;
    }

    public PhieuDatThuoc getPhieuDatThuocQuaMaPhieuDat(String maPhieuDat) throws SQLException {
        String sql = "SELECT * FROM PhieuDatThuoc WHERE maPhieuDat = ?";
        try (Connection con = getSafeConnection()) {
            damBaoCotMaNhanVien(con);
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPhieuDat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Date ngayDat = rs.getDate("ngayDat");
                    String maKH = rs.getString("maKH");
                    String diaChi = rs.getString("diaChi");
                    String hinhThucThanhToan = rs.getString("hinhThucThanhToan");
                    String trangThai = rs.getString("trangThai");
                    String maNV = rs.getString("maNV");

                    return new PhieuDatThuoc(maPhieuDat, ngayDat, new KhachHang(maKH),
                            maNV == null ? null : new NhanVien(maNV), diaChi, hinhThucThanhToan, trangThai);
                }
            }
        }
        return null;
    }

    public String getTenNhanVienThanhToanTheoPhieuDat(String maPhieuDat) throws SQLException {
        String sql = "SELECT TOP 1 nv.hoTen "
                + "FROM HoaDon hd "
                + "LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV "
                + "WHERE hd.maPhieuDat = ? "
                + "ORDER BY hd.ngayLap DESC, CAST(SUBSTRING(hd.maHD, 3, 5) AS INT) DESC";
        try (Connection con = getSafeConnection();
                PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maPhieuDat);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("hoTen");
                }
            }
        }
        return null;
    }

    public boolean capNhatTrangThaiPhieuDatThuoc(String maPhieuDat, String trangThaiMoi) throws SQLException {
        String sql = "UPDATE PhieuDatThuoc SET trangThai = ? WHERE maPhieuDat = ?";
        try (Connection con = getSafeConnection()) {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, trangThaiMoi);
            stmt.setString(2, maPhieuDat);

            int rowAfftected = stmt.executeUpdate();
            return rowAfftected > 0;
        }
    }

    public boolean themPhieuDatThuoc(PhieuDatThuoc pdt) throws SQLException {
	try (Connection con = getSafeConnection()) {
            damBaoCotMaNhanVien(con);
		return themPhieuDatThuoc(con, pdt);
	}
    }

    public boolean taoPhieuDatVaTruTon(PhieuDatThuoc pdt, ArrayList<ChiTietPhieuDatThuoc> dsChiTiet)
            throws SQLException {
        if (pdt == null || dsChiTiet == null || dsChiTiet.isEmpty()) {
            throw new SQLException("Phieu dat phai co it nhat mot chi tiet");
        }

        try (Connection con = getSafeConnection()) {
            damBaoCotMaNhanVien(con);
            boolean oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                if (!themPhieuDatThuoc(con, pdt)) {
                    con.rollback();
                    return false;
                }

                LoThuocDAO loDAO = new LoThuocDAO();
                for (ChiTietPhieuDatThuoc ct : dsChiTiet) {
                    ct.getPhieuDatThuoc().setMaPhieuDat(pdt.getMaPhieuDat());
                    if (!themChiTietPhieuDatThuoc(con, ct)) {
                        con.rollback();
                        return false;
                    }
                    String maThuoc = ct.getThuoc().getMaThuoc();
                    if (!loDAO.truTonTheoFEFO(con, maThuoc, ct.getSoLuong())) {
                        throw new SQLException("Ton kho lo khong du cho thuoc " + maThuoc);
                    }
                }

                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(oldAutoCommit);
            }
        }
    }

    private boolean themPhieuDatThuoc(Connection con, PhieuDatThuoc pdt) throws SQLException {
        damBaoCotMaNhanVien(con);
    	String sql = "INSERT PhieuDatThuoc(maPhieuDat,ngayDat,maKH,maNV,diaChi,hinhThucThanhToan"
    			+ ",trangThai) VALUES(?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
    		stmt.setString(1, pdt.getMaPhieuDat());
    		stmt.setDate(2, new Date(pdt.getNgayDat().getTime()));
    		stmt.setString(3, pdt.getKhachHang().getMaKH());
            if (pdt.getNhanVien() != null && pdt.getNhanVien().getMaNV() != null
                    && !pdt.getNhanVien().getMaNV().trim().isEmpty()) {
                stmt.setString(4, pdt.getNhanVien().getMaNV());
            } else {
                stmt.setNull(4, java.sql.Types.NVARCHAR);
            }
    		stmt.setString(5, pdt.getDiaChi());
    		stmt.setString(6, pdt.getHinhThucThanhToan());
    		stmt.setString(7, pdt.getTrangThai());
    		return stmt.executeUpdate() > 0;
    	}
    }

    private void damBaoCotMaNhanVien(Connection con) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_NAME = 'PhieuDatThuoc' AND COLUMN_NAME = 'maNV'";
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("ALTER TABLE PhieuDatThuoc ADD maNV NVARCHAR(20) NULL");
        }
    }

    private boolean themChiTietPhieuDatThuoc(Connection con, ChiTietPhieuDatThuoc ctpdt) throws SQLException {
    	String sql = "INSERT ChiTietPhieuDatThuoc (maPhieuDat,maThuoc,soLuong,donGia)"
    			+ " VALUES (?,?,?,?)";
    	try (PreparedStatement stmt = con.prepareStatement(sql)) {
    		stmt.setString(1, ctpdt.getPhieuDatThuoc().getMaPhieuDat());
    		stmt.setString(2, ctpdt.getThuoc().getMaThuoc());
    		stmt.setInt(3, ctpdt.getSoLuong());
    		stmt.setDouble(4, ctpdt.getDonGia());
    		return stmt.executeUpdate() > 0;
    	}
    }
}
