package entity;

import java.io.Serializable;
import java.util.Objects;

public class Thue implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String maThue;
    private String tenThue;
    private double phanTramThue;
    
    public static final String REGEX_MA_THUE = "^TE\\d{5}$";
    
    public Thue() {
        this("", "", 0.0);
    }
    
    public Thue(String maThue, String tenThue, double phanTramThue) {
        setMaThue(maThue);
        setTenThue(tenThue);
        setPhanTramThue(phanTramThue);
    }
    
    public Thue(Thue thue) {
        if (thue != null) {
            this.maThue = thue.maThue;
            this.tenThue = thue.tenThue;
            this.phanTramThue = thue.phanTramThue;
        }
    }
    
    public Thue(String maThue2) {
		setMaThue(maThue2);
	}

	public void setMaThue(String maThue) {
        if (maThue == null || maThue.trim().isEmpty()) {
            this.maThue = "";
        } else if (!maThue.matches(REGEX_MA_THUE)) {
            throw new IllegalArgumentException("Mã thuế phải có dạng TE + 5 chữ số (VD: TE00001)");
        } else {
            this.maThue = maThue;
        }
    }
    
    public void setTenThue(String tenThue) {
        if (tenThue == null || tenThue.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thuế không được để trống");
        }
        this.tenThue = tenThue;
    }
    
    public void setPhanTramThue(double phanTramThue) {
        if (phanTramThue < 0) {
            throw new IllegalArgumentException("Phần trăm thuế phải >= 0");
        }
        this.phanTramThue = phanTramThue;
    }
    
    public String getMaThue() {
        return maThue;
    }
    
    public String getTenThue() {
        return tenThue;
    }
    
    public double getPhanTramThue() {
        return phanTramThue;
    }
    
    @Override
    public String toString() {
        return "Thue{" +
                "maThue='" + maThue + '\'' +
                ", tenThue='" + tenThue + '\'' +
                ", phanTramThue=" + phanTramThue + "%" +
                '}';
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maThue);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Thue other = (Thue) obj;
        return Objects.equals(maThue, other.maThue);
    }
}