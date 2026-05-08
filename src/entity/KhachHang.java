package entity;

import java.io.Serializable;
import java.util.Objects;

public class KhachHang implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maKH;
    private String hoTen;
    private String soDienThoai;
    private String email;
    
    public static final String REGEX_MA_KH = "^KH\\d{5}$";
    public static final String REGEX_SO_DIEN_THOAI = "^(\\+84|0)\\d{9}$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
    
    public KhachHang() {
        this.maKH = null;
        this.hoTen = "Khách lẻ";
        this.soDienThoai = "xxxxxxxxxx";
        this.email = "xxx@gmail.com";
    }
    
    public KhachHang(String maKH, String hoTen, String soDienThoai, String email) {
        setMaKH(maKH);
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
    }
    
    public KhachHang(String maKH2) {
		setMaKH(maKH2);
	}

	public void setMaKH(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) {
            this.maKH = "";
        } else if (!maKH.matches(REGEX_MA_KH)) {
            throw new IllegalArgumentException("Mã khách hàng phải có dạng KH + 5 chữ số (VD: KH00001)");
        } else {
            this.maKH = maKH;
        }
    }
    
    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng");
        }
        this.hoTen = hoTen;
    }
    
    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches(REGEX_SO_DIEN_THOAI)) {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu (+84 | 0) + 9 số");
        }
        this.soDienThoai = soDienThoai;
    }
    
    public void setEmail(String email) {
        if (email == null || !email.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException("Email phải có định dạng abcde@gmail.com");
        }
        this.email = email;
    }
    
    public String getMaKH() {
        return maKH;
    }
    
    public String getHoTen() {
        return hoTen;
    }
    
    public String getSoDienThoai() {
        return soDienThoai;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", hoTen='" + hoTen + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maKH);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KhachHang other = (KhachHang) obj;
        return Objects.equals(maKH, other.maKH);
    }
}