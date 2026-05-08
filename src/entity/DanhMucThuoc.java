package entity;

import java.io.Serializable;
import java.util.Objects;

public class DanhMucThuoc implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maDanhMuc;
    private String tenDanhMuc;
    
    public static final String REGEX_MA_DANH_MUC = "^DM\\d{5}$";
    
    public DanhMucThuoc() {
        this("", "");
    }
    
    public DanhMucThuoc(String maDanhMuc, String tenDanhMuc) {
        setMaDanhMuc(maDanhMuc);
        setTenDanhMuc(tenDanhMuc);
    }
    
    public DanhMucThuoc(DanhMucThuoc dm) {
        if (dm != null) {
            this.maDanhMuc = dm.maDanhMuc;
            this.tenDanhMuc = dm.tenDanhMuc;
        }
    }
    
    public DanhMucThuoc(String maDanhMuc) {
		setMaDanhMuc(maDanhMuc);
	}

	public void setMaDanhMuc(String maDanhMuc) {
        if (maDanhMuc == null || maDanhMuc.trim().isEmpty()) {
            this.maDanhMuc = "";
        } else if (!maDanhMuc.matches(REGEX_MA_DANH_MUC)) {
            throw new IllegalArgumentException("Mã danh mục phải có dạng DM + 5 chữ số (VD: DM00001)");
        } else {
            this.maDanhMuc = maDanhMuc;
        }
    }
    
    public void setTenDanhMuc(String tenDanhMuc) {
        if (tenDanhMuc == null || tenDanhMuc.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được rỗng");
        }
        this.tenDanhMuc = tenDanhMuc;
    }
    
    public String getMaDanhMuc() {
        return maDanhMuc;
    }
    
    public String getTenDanhMuc() {
        return tenDanhMuc;
    }
    
    @Override
    public String toString() {
        return "DanhMucThuoc{" +
                "maDanhMuc='" + maDanhMuc + '\'' +
                ", tenDanhMuc='" + tenDanhMuc + '\'' +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maDanhMuc);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DanhMucThuoc other = (DanhMucThuoc) obj;
        return Objects.equals(maDanhMuc, other.maDanhMuc);
    }
}