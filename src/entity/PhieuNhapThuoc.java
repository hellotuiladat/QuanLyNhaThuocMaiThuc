package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PhieuNhapThuoc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maPhieuNhap;
    private NhanVien nhanVien;
    private NhaCungCap nhaCungCap;
    private Date ngayNhap;
    
    public static final String REGEX_MA_PHIEU_NHAP = "^PN\\d{5}$";
    
    public PhieuNhapThuoc() {
        this("", null, null, new Date());
    }
    
    public PhieuNhapThuoc(String maPhieuNhap, NhanVien nhanVien, NhaCungCap nhaCungCap, Date ngayNhap) {
        setMaPhieuNhap(maPhieuNhap);
        setNhanVien(nhanVien);
        setNhaCungCap(nhaCungCap);
        setNgayNhap(ngayNhap);
    }
    
    public PhieuNhapThuoc(PhieuNhapThuoc pnh) {
        if (pnh != null) {
            this.maPhieuNhap = pnh.maPhieuNhap;
            this.nhanVien = pnh.nhanVien;
            this.nhaCungCap = pnh.nhaCungCap;
            this.ngayNhap = pnh.ngayNhap;
        }
    }
    
    public void setMaPhieuNhap(String maPhieuNhap) {
        if (maPhieuNhap == null || maPhieuNhap.trim().isEmpty()) {
            this.maPhieuNhap = "";
        } else if (!maPhieuNhap.matches(REGEX_MA_PHIEU_NHAP)) {
            throw new IllegalArgumentException("Mã phiếu nhập phải có dạng PN + 5 chữ số (VD: PN00001)");
        } else {
            this.maPhieuNhap = maPhieuNhap;
        }
    }
    
    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
    
    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }
    
    public void setNgayNhap(Date ngayNhap) {
        Date currentDate = new Date();
        if (ngayNhap != null && ngayNhap.after(currentDate)) {
            throw new IllegalArgumentException("Ngày nhập không được sau ngày hiện tại");
        }
        this.ngayNhap = ngayNhap;
    }
    
    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }
    
    public NhanVien getNhanVien() {
        return nhanVien;
    }
    
    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }
    
    public Date getNgayNhap() {
        return ngayNhap;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(maPhieuNhap);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PhieuNhapThuoc other = (PhieuNhapThuoc) obj;
        return Objects.equals(maPhieuNhap, other.maPhieuNhap);
    }
}