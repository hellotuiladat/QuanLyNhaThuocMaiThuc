package entity;

import java.util.Objects;

public class ChiTietNhapThuoc {
    private String maPhieuNhap;
    private String maThuoc;
    private int soLuong;
    private double donGia;
    
    public ChiTietNhapThuoc() {
    }
    
    public ChiTietNhapThuoc(String maPhieuNhap, String maThuoc, int soLuong, double donGia) {
        this.maPhieuNhap = maPhieuNhap;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        setDonGia(donGia);
    }
    
    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }
    
    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }
    
    public String getMaThuoc() {
        return maThuoc;
    }
    
    public void setMaThuoc(String maThuoc) {
        this.maThuoc = maThuoc;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm");
        }
        this.donGia = donGia;
    }
    
    public double getThanhTien() {
        return soLuong * donGia;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maPhieuNhap, maThuoc);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChiTietNhapThuoc other = (ChiTietNhapThuoc) obj;
        return Objects.equals(maPhieuNhap, other.maPhieuNhap) && 
               Objects.equals(maThuoc, other.maThuoc);
    }
    
    @Override
    public String toString() {
        return "ChiTietNhapThuoc [maPhieuNhap=" + maPhieuNhap + ", maThuoc=" + maThuoc 
               + ", soLuong=" + soLuong + ", donGia=" + donGia + "]";
    }
}