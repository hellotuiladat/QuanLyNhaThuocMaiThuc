package entity;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietHoaDon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private HoaDon hoaDon;
    private Thuoc thuoc;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    
    public ChiTietHoaDon() {
        
    }
    
    public ChiTietHoaDon(HoaDon hoaDon, Thuoc thuoc, int soLuong, double donGia) {
        setHoaDon(hoaDon);
        setThuoc(thuoc);
        setSoLuong(soLuong);
        setDonGia(donGia);
        setThanhTien();
    }
    

	public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }
    
    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }
    
    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn hoặc bằng 0");
        }
        this.soLuong = soLuong;
        setThanhTien();
    }
    
    public void setDonGia(double donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải lớn hơn hoặc bằng 0");
        }
        this.donGia = donGia;
        setThanhTien();
    }
    
    public void setThanhTien() {
        this.thanhTien = this.donGia * this.soLuong;
    }
    
    public HoaDon getHoaDon() {
        return hoaDon;
    }
    
    public Thuoc getThuoc() {
        return thuoc;
    }
    
    public int getSoLuong() {
        return soLuong;
    }
    
    public double getDonGia() {
        return donGia;
    }
    
    public double getThanhTien() {
        return thanhTien;
    }
}