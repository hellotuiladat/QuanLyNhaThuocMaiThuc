package entity;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietPhieuDatThuoc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Thuoc thuoc;
    private PhieuDatThuoc phieuDatThuoc;
    private double donGia;
    private int soLuong;
    private double thanhTien;
    
    public ChiTietPhieuDatThuoc() {
        this(null, null, 0.0, 0);
    }
    
    public ChiTietPhieuDatThuoc(Thuoc thuoc, PhieuDatThuoc phieuDatThuoc, double donGia, int soLuong) {
        setThuoc(thuoc);
        setPhieuDatThuoc(phieuDatThuoc);
        setDonGia(donGia);
        setSoLuong(soLuong);
        setThanhTien();
    }
    
    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }
    
    public void setPhieuDatThuoc(PhieuDatThuoc phieuDatThuoc) {
        this.phieuDatThuoc = phieuDatThuoc;
    }
    
    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải >= 0");
        }
        this.donGia = donGia;
        setThanhTien();
    }
    
    public void setSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng thuốc phải > 0");
        }
        this.soLuong = soLuong;
        setThanhTien();
    }
    
    public void setThanhTien() {
        this.thanhTien = this.donGia * this.soLuong;
    }
    
    public Thuoc getThuoc() {
        return thuoc;
    }
    
    public PhieuDatThuoc getPhieuDatThuoc() {
        return phieuDatThuoc;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public double getThanhTien() {
        return thanhTien;
    }
    

}