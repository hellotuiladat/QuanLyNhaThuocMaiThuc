package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Thuoc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maThuoc;
    private String tenThuoc;
    private String donViTinh;
    private double giaBan;
    private int soLuongTon;
    private Date hanSuDung;
    private String moTa;
    private DanhMucThuoc danhMucThuoc;
    private String hinhAnh;
    private String thanhPhan;
    private Date ngaySanXuat;
    private String xuatXu;
    
    public static final String REGEX_MA_THUOC = "^TH\\d{5}$";
    
    public Thuoc() {
        
    }
    
	public Thuoc(String maThuoc, String tenThuoc, String donViTinh, double giaBan, int soLuongTon, Date hanSuDung,
			String mota, DanhMucThuoc danhMucThuoc, String hinhAnh, String thanhPhan, Date ngaySanXuat,
			String xuatXu) {
		super();
		setMaThuoc(maThuoc);
		setTenThuoc(tenThuoc);
		setDonViTinh(donViTinh);
		setGiaBan(giaBan);
		setSoLuongTon(soLuongTon);
		setNgaySanXuat(ngaySanXuat);
		setHanSuDung(hanSuDung);
		setMoTa(mota);
		setDanhMucThuoc(danhMucThuoc);
		setHinhAnh(hinhAnh);
		setThanhPhan(thanhPhan);
		setXuatXu(xuatXu);
	}
    
    public Thuoc(String maThuoc) {
		setMaThuoc(maThuoc);
	}

    public void setMaThuoc(String maThuoc) {
        if (maThuoc == null || maThuoc.trim().isEmpty()) {
            this.maThuoc = "";
        } else if (!maThuoc.matches(REGEX_MA_THUOC)) {
            throw new IllegalArgumentException("Mã thuốc phải có dạng TH + 5 chữ số (VD: TH00001)");
        } else {
            this.maThuoc = maThuoc;
        }
    }
    
    public void setTenThuoc(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thuốc không được rỗng");
        }
        this.tenThuoc = tenThuoc;
    }
    
    public void setDonViTinh(String donViTinh) {
        if (donViTinh == null || donViTinh.trim().isEmpty()) {
            throw new IllegalArgumentException("Đơn vị tính không được rỗng");
        }
        this.donViTinh = donViTinh;
    }
    
    public void setGiaBan(double giaBan) {
        if (giaBan < 0) {
            throw new IllegalArgumentException("Giá bán phải lớn hơn hoặc bằng 0");
        }
        this.giaBan = giaBan;
    }
    
    public void setSoLuongTon(int soLuongTon) {
        if (soLuongTon < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho phải lớn hơn hoặc bằng 0");
        }
        this.soLuongTon = soLuongTon;
    }
    
    public void setHanSuDung(Date hanSuDung) {
        if (hanSuDung == null) {
            throw new IllegalArgumentException("Hạn sử dụng không được để trống");
        }
        this.hanSuDung = hanSuDung;
    }
    
    public void setMoTa(String moTa) {
        if (moTa == null || moTa.trim().isEmpty() || moTa.trim().length() < 10) {
            throw new IllegalArgumentException("Mô tả phải có chú thích cụ thể rõ ràng");
        }
        this.moTa = moTa;
    }
    
    public void setDanhMucThuoc(DanhMucThuoc danhMucThuoc) {
        this.danhMucThuoc = danhMucThuoc;
    }
    
    public void setHinhAnh(String hinhAnh) {
        if (hinhAnh == null || hinhAnh.trim().isEmpty()) {
            throw new IllegalArgumentException("Hình ảnh thuốc không được để trống");
        }
        this.hinhAnh = hinhAnh;
    }
    
    public void setThanhPhan(String thanhPhan) {
        if (thanhPhan == null || thanhPhan.trim().isEmpty()) {
            throw new IllegalArgumentException("Thành phần thuốc không được để trống");
        }
        this.thanhPhan = thanhPhan;
    }
    
    public void setNgaySanXuat(Date ngaySanXuat) {
        Date currentDate = new Date();
        if (ngaySanXuat != null && ngaySanXuat.after(currentDate)) {
            throw new IllegalArgumentException("Ngày sản xuất thuốc phải trước ngày hiện tại");
        }
        this.ngaySanXuat = ngaySanXuat;
    }
    
    public void setXuatXu(String xuatXu) {
        if (xuatXu == null || xuatXu.trim().isEmpty()) {
            throw new IllegalArgumentException("Xuất xứ của thuốc không được để trống");
        }
        this.xuatXu = xuatXu;
    }
    
    public String getMaThuoc() {
        return maThuoc;
    }
    
    public String getTenThuoc() {
        return tenThuoc;
    }
    
    public String getDonViTinh() {
        return donViTinh;
    }
    
    public double getGiaBan() {
        return giaBan;
    }
    
    public int getSoLuongTon() {
        return soLuongTon;
    }
    
    public Date getHanSuDung() {
        return hanSuDung;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public DanhMucThuoc getDanhMucThuoc() {
        return danhMucThuoc;
    }
    
    public String getHinhAnh() {
        return hinhAnh;
    }
    
    public String getThanhPhan() {
        return thanhPhan;
    }
    
    public Date getNgaySanXuat() {
        return ngaySanXuat;
    }
    
    public String getXuatXu() {
        return xuatXu;
    }
    
    
    
    @Override
    public String toString() {
        return "Thuoc{" +
                "maThuoc='" + maThuoc + '\'' +
                ", tenThuoc='" + tenThuoc + '\'' +
                ", donViTinh='" + donViTinh + '\'' +
                ", giaBan=" + giaBan +
                ", soLuongTon=" + soLuongTon +
                ", hanSuDung=" + hanSuDung +
                ", moTa='" + moTa + '\'' +
                ", danhMucThuoc=" + (danhMucThuoc != null ? danhMucThuoc.getMaDanhMuc() : "null") +
                ", hinhAnh='" + hinhAnh + '\'' +
                ", thanhPhan='" + thanhPhan + '\'' +
                ", ngaySanXuat=" + ngaySanXuat +
                ", xuatXu='" + xuatXu + '\'' +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maThuoc);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Thuoc other = (Thuoc) obj;
        return Objects.equals(maThuoc, other.maThuoc);
    }
}