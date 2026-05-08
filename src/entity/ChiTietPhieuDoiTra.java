package entity;

import java.io.Serializable;

public class ChiTietPhieuDoiTra implements Serializable {
    private String maPhieuDoiTra;
    private Thuoc thuoc; // thuốc cũ (trả lại)
    private Thuoc thuocMoi; // thuốc thay thế (nếu có)
    private Integer soLuong; // số lượng thuốc cũ
    private Integer soLuongThayThe; // số lượng thuốc mới
    private double donGia; // đơn giá thuốc cũ
    private Double donGiaThayThe; // đơn giá thuốc mới
    private String trangThaiThuoc; // "Bình thường", "Hỏng", ...
    private String loaiThaoTac; // "DOI" hoặc "TRA"
    private double chenhLechTien; // tiền chênh lệch (có thể âm)
    private boolean daXuLy; // đã xử lý dòng chi tiết hay chưa

    public ChiTietPhieuDoiTra() {
        this.soLuong = 0;
        this.soLuongThayThe = 0;
        this.donGia = 0.0;
        this.donGiaThayThe = 0.0;
        this.chenhLechTien = 0.0;
        this.daXuLy = false;
    }

    public ChiTietPhieuDoiTra(String maPhieuDoiTra, Thuoc thuoc,
            Integer soLuong, double donGia,
            String trangThaiThuoc, String loaiThaoTac) {
        this();
        this.maPhieuDoiTra = maPhieuDoiTra;
        this.thuoc = thuoc;
        this.soLuong = soLuong != null ? soLuong : 0;
        this.donGia = donGia;
        this.trangThaiThuoc = trangThaiThuoc;
        this.loaiThaoTac = loaiThaoTac;
        updateChenhLech();
    }

    // Getters and setters
    public String getMaPhieuDoiTra() {
        return maPhieuDoiTra;
    }

    public void setMaPhieuDoiTra(String maPhieuDoiTra) {
        this.maPhieuDoiTra = maPhieuDoiTra;
    }

    public Thuoc getThuoc() {
        return thuoc;
    }

    public void setThuoc(Thuoc thuoc) {
        this.thuoc = thuoc;
    }

    public Thuoc getThuocMoi() {
        return thuocMoi;
    }

    public void setThuocMoi(Thuoc thuocMoi) {
        this.thuocMoi = thuocMoi;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong != null ? soLuong : 0;
        updateChenhLech();
    }

    public Integer getSoLuongThayThe() {
        return soLuongThayThe;
    }

    public void setSoLuongThayThe(Integer soLuongThayThe) {
        this.soLuongThayThe = soLuongThayThe != null ? soLuongThayThe : 0;
        updateChenhLech();
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
        updateChenhLech();
    }

    public Double getDonGiaThayThe() {
        return donGiaThayThe;
    }

    public void setDonGiaThayThe(Double donGiaThayThe) {
        this.donGiaThayThe = donGiaThayThe;
        updateChenhLech();
    }

    public String getTrangThaiThuoc() {
        return trangThaiThuoc;
    }

    public void setTrangThaiThuoc(String trangThaiThuoc) {
        this.trangThaiThuoc = trangThaiThuoc;
    }

    public String getLoaiThaoTac() {
        return loaiThaoTac;
    }

    public void setLoaiThaoTac(String loaiThaoTac) {
        this.loaiThaoTac = loaiThaoTac;
    }

    public double getChenhLechTien() {
        return chenhLechTien;
    }

    public void setChenhLechTien(double chenhLechTien) {
        this.chenhLechTien = chenhLechTien;
    }

    public boolean isDaXuLy() {
        return daXuLy;
    }

    public void setDaXuLy(boolean daXuLy) {
        this.daXuLy = daXuLy;
    }

    // Backwards-compatible aliases used by existing dialogs/DAOs
    public void setThuocCu(Thuoc t) {
        setThuoc(t);
    }

    public Thuoc getThuocCu() {
        return getThuoc();
    }

    public void setSoLuongCu(int sl) {
        setSoLuong(Integer.valueOf(sl));
    }

    public int getSoLuongCu() {
        return getSoLuong() != null ? getSoLuong() : 0;
    }

    public void setDonGiaCu(double dg) {
        setDonGia(dg);
    }

    public double getDonGiaCu() {
        return getDonGia();
    }

    public void setDonGiaMoi(Double dgMoi) {
        setDonGiaThayThe(dgMoi);
    }

    public Double getDonGiaMoi() {
        return getDonGiaThayThe();
    }

    // Helpers
    private void updateChenhLech() {
        double thanhTienCu = (soLuong != null ? soLuong : 0) * donGia;
        double thanhTienMoi = (soLuongThayThe != null ? soLuongThayThe : 0) * (donGiaThayThe != null ? donGiaThayThe : 0.0);
        this.chenhLechTien = thanhTienMoi - thanhTienCu;
    }

    public double getThanhTienCu() {
        return (soLuong != null ? soLuong : 0) * donGia;
    }

    public double getThanhTienMoi() {
        return (soLuongThayThe != null ? soLuongThayThe : 0) * (donGiaThayThe != null ? donGiaThayThe : 0.0);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDoiTra{" +
                "maPhieuDoiTra='" + maPhieuDoiTra + '\'' +
                ", thuoc=" + thuoc +
                ", thuocMoi=" + thuocMoi +
                ", soLuong=" + soLuong +
                ", soLuongThayThe=" + soLuongThayThe +
                ", donGia=" + donGia +
                ", donGiaThayThe=" + donGiaThayThe +
                ", trangThaiThuoc='" + trangThaiThuoc + '\'' +
                ", loaiThaoTac='" + loaiThaoTac + '\'' +
                ", chenhLechTien=" + chenhLechTien +
                ", daXuLy=" + daXuLy +
                '}';
    }
}