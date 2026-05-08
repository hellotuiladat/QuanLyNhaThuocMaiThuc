package entity;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietPhieuNhap implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Thuoc thuoc;
    private PhieuNhapThuoc phieuNhapThuoc;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    
    public ChiTietPhieuNhap() {
        this(null, null, 0, 0.0);
    }
    
    public ChiTietPhieuNhap(Thuoc thuoc, PhieuNhapThuoc phieuNhapThuoc, int soLuong, double donGia) {
        setThuoc(thuoc);
        setphieuNhapThuoc(phieuNhapThuoc);
        setSoLuong(soLuong);
        setDonGia(donGia);
        setThanhTien();
    }
    
    public ChiTietPhieuNhap(ChiTietPhieuNhap ctpn) {
        if (ctpn != null) {
            this.thuoc = ctpn.thuoc;
            this.phieuNhapThuoc = ctpn.phieuNhapThuoc;
            this.soLuong = ctpn.soLuong;
            this.donGia = ctpn.donGia;
            this.thanhTien = ctpn.thanhTien;
        }
    }
    
    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }
    
    public void setphieuNhapThuoc(PhieuNhapThuoc phieuNhapThuoc) {
        this.phieuNhapThuoc = phieuNhapThuoc;
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
    
    public Thuoc getThuoc() {
        return thuoc;
    }
    
    public PhieuNhapThuoc getphieuNhapThuoc() {
        return phieuNhapThuoc;
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