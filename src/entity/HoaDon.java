package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class HoaDon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maHD;
    private Date ngayLap;
    private Thue thue;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;
    private PhieuDatThuoc phieuDatThuoc;
    
    public static final String REGEX_MA_HD = "^HD\\d{5}$";
    
    public HoaDon() {
        this("", new Date(), null, null, null, null, null);
    }
    
    public HoaDon(String maHD, Date ngayLap, Thue thue, NhanVien nhanVien, 
                  KhachHang khachHang, KhuyenMai khuyenMai, PhieuDatThuoc phieuDatThuoc) {
        setMaHD(maHD);
        setNgayLap(ngayLap);
        setThue(thue);
        setNhanVien(nhanVien);
        setKhachHang(khachHang);
        setKhuyenMai(khuyenMai);
        setPhieuDatThuoc(phieuDatThuoc);
    }
    
    public HoaDon(String maHD) {
    	setMaHD(maHD);
    }

    
    public void setMaHD(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) {
            this.maHD = "";
        } else if (!maHD.matches(REGEX_MA_HD)) {
            throw new IllegalArgumentException("Mã hóa đơn phải có dạng HD + 5 chữ số (VD: HD00001)");
        } else {
            this.maHD = maHD;
        }
    }
    
    public void setNgayLap(Date ngayLap) {
        Date currentDate = new Date();
        if (ngayLap != null && ngayLap.after(currentDate)) {
            throw new IllegalArgumentException("Ngày lập hóa đơn không được sau ngày hiện tại");
        }
        this.ngayLap = ngayLap;
    }
    
    public void setThue(Thue thue) {
        this.thue = thue;
    }
    
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
    
    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }
    
    public void setPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) {
        this.phieuDatThuoc = phieuDatThuoc;
    }
    
    public String getMaHD() {
        return maHD;
    }
    
    public Date getNgayLap() {
        return ngayLap;
    }
    
    public Thue getThue() {
        return thue;
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    public KhachHang getKhachHang() {
        return khachHang;
    }
    
    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }
    
    public PhieuDatThuoc getPhieuDatThuoc() {
        return phieuDatThuoc;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HoaDon other = (HoaDon) obj;
        return Objects.equals(maHD, other.maHD);
    }
}