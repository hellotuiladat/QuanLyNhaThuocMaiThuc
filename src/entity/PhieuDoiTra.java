package entity;

import java.util.Date;

public class PhieuDoiTra {
    private String maPhieuDoiTra;
    private Date ngayDoiTra;
    private String loaiPhieu; // "DOI" hoặc "TRA"
    private String lyDo;
    private double tongTien;
    private String trangThai; // "Đã xử lý", "Chờ xử lý"
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private HoaDon hoaDonGoc; // Hóa đơn gốc để đổi trả
    
    public PhieuDoiTra() {
    }
    
    public PhieuDoiTra(String maPhieuDoiTra, Date ngayDoiTra, String loaiPhieu, String lyDo, 
                       double tongTien, String trangThai, NhanVien nhanVien, KhachHang khachHang, 
                       HoaDon hoaDonGoc) {
        this.maPhieuDoiTra = maPhieuDoiTra;
        this.ngayDoiTra = ngayDoiTra;
        this.loaiPhieu = loaiPhieu;
        this.lyDo = lyDo;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.hoaDonGoc = hoaDonGoc;
    }

    // Getters and Setters
    public String getMaPhieuDoiTra() {
        return maPhieuDoiTra;
    }

    public void setMaPhieuDoiTra(String maPhieuDoiTra) {
        this.maPhieuDoiTra = maPhieuDoiTra;
    }

    public Date getNgayDoiTra() {
        return ngayDoiTra;
    }

    public void setNgayDoiTra(Date ngayDoiTra) {
        this.ngayDoiTra = ngayDoiTra;
    }

    public String getLoaiPhieu() {
        return loaiPhieu;
    }

    public void setLoaiPhieu(String loaiPhieu) {
        this.loaiPhieu = loaiPhieu;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public HoaDon getHoaDonGoc() {
        return hoaDonGoc;
    }

    public void setHoaDonGoc(HoaDon hoaDonGoc) {
        this.hoaDonGoc = hoaDonGoc;
    }

    @Override
    public String toString() {
        return "PhieuDoiTra{" +
                "maPhieuDoiTra='" + maPhieuDoiTra + '\'' +
                ", ngayDoiTra=" + ngayDoiTra +
                ", loaiPhieu='" + loaiPhieu + '\'' +
                ", lyDo='" + lyDo + '\'' +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                ", nhanVien=" + nhanVien +
                ", khachHang=" + khachHang +
                ", hoaDonGoc=" + hoaDonGoc +
                '}';
    }
}