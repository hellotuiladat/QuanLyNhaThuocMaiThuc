package entity;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NhanVien {
	private String maNV;
	private String tenNV;
	private String chucVu;
	private String soDienThoai;
	private Date ngaySinh;
	private String gioiTinh;
	private String diaChi;
	private String email;
	private boolean daXoa;
    public static final String REGEX_MA_NV = "^NV\\d{5}$";
    public static final String REGEX_SO_DIEN_THOAI = "^(\\+84|0)\\d{9}$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
	public boolean isXoa() {
		return daXoa;
	}

	public void setXoa(boolean daXoa) {
		this.daXoa = false;
	}

	public NhanVien() {
		// TODO Auto-generated constructor stub
	}
	
	public NhanVien(String maNV, String tenNV, String chucVu, String soDienThoai, Date ngaySinh, String gioiTinh,
			String diaChi, String email) {
		super();
		setMaNV(maNV);
		setTenNV(tenNV);
		setChucVu(chucVu);
		setSoDienThoai(soDienThoai);
		setNgaySinh(ngaySinh);
		setGioiTinh(gioiTinh);
		setDiaChi(diaChi);
		setEmail(email);
		setXoa(false);
	}
	public NhanVien(String maNV, String tenNV, String chucVu, String soDienThoai, Date ngaySinh, String gioiTinh,
			String diaChi, String email, boolean daXoa) {
		super();
		setMaNV(maNV);
		setTenNV(tenNV);
		setChucVu(chucVu);
		setSoDienThoai(soDienThoai);
		setNgaySinh(ngaySinh);
		setGioiTinh(gioiTinh);
		setDiaChi(diaChi);
		setEmail(email);
		setXoa(daXoa);
	}
	public NhanVien(String maNV2) {
		this.maNV = maNV2;
	}

	public String getMaNV() {
		return maNV;
	}
    public void setMaNV(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            this.maNV = "";
        } else if (!maNV.matches(REGEX_MA_NV)) {
            throw new IllegalArgumentException("Mã nhân viên phải có dạng NV + 5 chữ số (VD: NV00001)");
        } else {
            this.maNV = maNV;
        }
    }
	public String getTenNV() {
		return tenNV;
	}
	public void setTenNV(String tenNV) {
        if (tenNV == null || tenNV.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng");
        }
        this.tenNV = tenNV;
    }
	public String getChucVu() {
		return chucVu;
	}
    public void setChucVu(String chucVu) {
        if (chucVu == null || 
            (!chucVu.equals("Nhân viên quản lý") && !chucVu.equals("Nhân viên bán thuốc"))) {
            throw new IllegalArgumentException("Chức vụ chỉ được phép là Nhân viên quản lý hoặc là Nhân viên bán thuốc");
        }
        this.chucVu = chucVu;
    }
	public String getSoDienThoai() {
		return soDienThoai;
	}
    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches(REGEX_SO_DIEN_THOAI)) {
            throw new IllegalArgumentException("Số điện thoại phải bắt đầu (+84 | 0) + 9 số");
        }
        this.soDienThoai = soDienThoai;
    }
	public Date getNgaySinh() {
		return ngaySinh;
	}
    public void setNgaySinh(Date ngaySinh) {
        if (ngaySinh == null) {
            throw new IllegalArgumentException("Ngày sinh không được để trống");
        }
        
        Date currentDate = new Date();
        if (ngaySinh.after(currentDate)) {
            throw new IllegalArgumentException("Ngày sinh phải trước ngày hiện tại");
        }
        
        Calendar calNgaySinh = Calendar.getInstance();
        calNgaySinh.setTime(ngaySinh);
        
        Calendar calHienTai = Calendar.getInstance();
        calHienTai.setTime(currentDate);
        
        int tuoi = calHienTai.get(Calendar.YEAR) - calNgaySinh.get(Calendar.YEAR);
        
        if (calHienTai.get(Calendar.MONTH) < calNgaySinh.get(Calendar.MONTH) ||
            (calHienTai.get(Calendar.MONTH) == calNgaySinh.get(Calendar.MONTH) &&
             calHienTai.get(Calendar.DAY_OF_MONTH) < calNgaySinh.get(Calendar.DAY_OF_MONTH))) {
            tuoi--;
        }
        
        if (tuoi < 18) {
            throw new IllegalArgumentException("Nhân viên phải >= 18 tuổi");
        }
        
        this.ngaySinh = ngaySinh;
    }
	@Override
	public int hashCode() {
		return Objects.hash(maNV);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}

	public String getGioiTinh() {
		return gioiTinh;
	}
    public void setGioiTinh(String gioiTinh) {
        if (gioiTinh == null || (!gioiTinh.equals("Nam") && !gioiTinh.equals("Nữ"))) {
            throw new IllegalArgumentException("Giới tính chỉ được phép là Nam hoặc là Nữ");
        }
        this.gioiTinh = gioiTinh;
    }
	public String getDiaChi() {
		return diaChi;
	}
    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ nhân viên không được phép để trống");
        }
        this.diaChi = diaChi;
    }
	public String getEmail() {
		return email;
	}
    public void setEmail(String email) {
        if (email == null || !email.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException("Email phải có định dạng abcde@gmail.com");
        }
        this.email = email;
    }

}
