use master
GO

-- Drop database nếu đã tồn tại
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'QLHieuThuocTayMaiThuc')
BEGIN
    ALTER DATABASE QLHieuThuocTayMaiThuc SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QLHieuThuocTayMaiThuc;
END
GO

CREATE DATABASE QLHieuThuocTayMaiThuc
GO

use QLHieuThuocTayMaiThuc
GO

-- =============================================
-- TẠO CÁC BẢNG
-- =============================================

CREATE TABLE NhaCungCap (
    maNCC NVARCHAR(20) PRIMARY KEY,
    tenNCC NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15),
    congNo DECIMAL(18,2) DEFAULT 0 CHECK (congNo >= 0)
);

CREATE TABLE NhanVien (
    maNV NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    chucVu NVARCHAR(50) NOT NULL CHECK (chucVu IN (N'Nhân viên bán thuốc', N'Nhân viên quản lý')),
    soDienThoai NVARCHAR(15),
    ngaySinh DATE,
    gioiTinh NVARCHAR(10) CHECK (gioiTinh IN (N'Nam', N'Nữ')),
    diaChi NVARCHAR(200) NOT NULL,
    email NVARCHAR(100),
    daXoa BIT DEFAULT 0 NOT NULL
);

CREATE TABLE KhachHang (
    maKH NVARCHAR(20) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15),
    email NVARCHAR(100)
);

CREATE TABLE Thue (
    maThue NVARCHAR(20) PRIMARY KEY,
    tenThue NVARCHAR(100) NOT NULL,
    phanTramThue DECIMAL(5,2) CHECK (phanTramThue >= 0)
);

CREATE TABLE DanhMucThuoc (
    maDanhMuc NVARCHAR(20) PRIMARY KEY,
    tenDanhMuc NVARCHAR(100) NOT NULL
);

CREATE TABLE KhuyenMai (
    maKM NVARCHAR(20) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL,
    phanTramGiamGia DECIMAL(5,2) CHECK (phanTramGiamGia > 0 AND phanTramGiamGia <= 100),
    CONSTRAINT CK_KhuyenMai_NgayKetThuc CHECK (ngayKetThuc >= ngayBatDau)
);

CREATE TABLE Thuoc (
    maThuoc NVARCHAR(20) PRIMARY KEY,
    tenThuoc NVARCHAR(100) NOT NULL,
    donViTinh NVARCHAR(20) NOT NULL,
    giaBan DECIMAL(18,2) CHECK (giaBan >= 0),
    soLuongTon INT CHECK (soLuongTon >= 0),
    hanSuDung DATE NOT NULL,
    moTa NVARCHAR(255) NOT NULL,
    maDanhMuc NVARCHAR(20),
    hinhAnh NVARCHAR(255) NOT NULL,
    thanhPhan NVARCHAR(255) NOT NULL,
    ngaySanXuat DATE,
    xuatXu NVARCHAR(100) NOT NULL,
    FOREIGN KEY (maDanhMuc) REFERENCES DanhMucThuoc(maDanhMuc)
);

CREATE TABLE PhieuNhapHang (
    maPhieuNhap NVARCHAR(20) PRIMARY KEY,
    maNV NVARCHAR(20) NOT NULL,
    maNCC NVARCHAR(20) NOT NULL,
    ngayNhap DATE NOT NULL,
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maNCC) REFERENCES NhaCungCap(maNCC)
);

CREATE TABLE ChiTietPhieuNhap (
    maPhieuNhap NVARCHAR(20),
    maThuoc NVARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong >= 0),
    donGia DECIMAL(18,2) CHECK (donGia >= 0),
    PRIMARY KEY (maPhieuNhap, maThuoc),
    FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhapHang(maPhieuNhap),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);

CREATE TABLE PhieuDatThuoc (
    maPhieuDat NVARCHAR(20) PRIMARY KEY,
    ngayDat DATE NOT NULL,
    maKH NVARCHAR(20) NOT NULL,
    diaChi NVARCHAR(200) NOT NULL,
    hinhThucThanhToan NVARCHAR(50) CHECK (hinhThucThanhToan IN (N'Thanh toán online', N'Tại chỗ')),
    trangThai NVARCHAR(20) CHECK (trangThai IN (N'Đã hoàn thành', N'Chưa hoàn thành')),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH)
);

CREATE TABLE ChiTietPhieuDatThuoc (
    maPhieuDat NVARCHAR(20),
    maThuoc NVARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong > 0),
    donGia DECIMAL(18,2) CHECK (donGia >= 0),
    PRIMARY KEY (maPhieuDat, maThuoc),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatThuoc(maPhieuDat),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);

CREATE TABLE HoaDon (
    maHD NVARCHAR(20) PRIMARY KEY,
    ngayLap DATE NOT NULL,
    maThue NVARCHAR(20),
    maNV NVARCHAR(20),
    maKH NVARCHAR(20),
    maKM NVARCHAR(20),
    maPhieuDat NVARCHAR(20),
    FOREIGN KEY (maThue) REFERENCES Thue(maThue),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM),
    FOREIGN KEY (maPhieuDat) REFERENCES PhieuDatThuoc(maPhieuDat)
);

CREATE TABLE ChiTietHoaDon (
    maHD NVARCHAR(20),
    maThuoc NVARCHAR(20),
    soLuong INT NOT NULL CHECK (soLuong >= 0),
    donGia DECIMAL(18,2) CHECK (donGia >= 0),
    PRIMARY KEY (maHD, maThuoc),
    FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    FOREIGN KEY (maThuoc) REFERENCES Thuoc(maThuoc)
);

CREATE TABLE TaiKhoan (
    tenDangNhap NVARCHAR(50) PRIMARY KEY,
    matKhau NVARCHAR(100) NOT NULL,
    trangThai NVARCHAR(20) CHECK (trangThai IN (N'Hoạt động', N'Ngừng hoạt động')),
    maNV NVARCHAR(20) UNIQUE,
    vaiTro NVARCHAR(50) CHECK (vaiTro IN (N'Nhân viên bán thuốc', N'Nhân viên quản lý')),
    FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

GO
ALTER TABLE HoaDon
ALTER COLUMN maKH NVARCHAR(20) NULL;

ALTER TABLE HoaDon
ALTER COLUMN maPhieuDat NVARCHAR(20) NULL;

GO
-- =============================================
-- INSERT DỮ LIỆU
-- =============================================

-- NhaCungCap (NCC00001, NCC00002, ...)
INSERT INTO NhaCungCap (maNCC, tenNCC, soDienThoai, congNo) VALUES
(N'NCC00001', N'Công ty Dược Hòa Bình', '0912345678', 1000000),
(N'NCC00002', N'Công ty Dược Bình Minh', '0987654321', 500000),
(N'NCC00003', N'Công ty Dược An Khang', '0909999999', 0),
(N'NCC00004', N'Công ty Dược Phẩm Việt', '0918888888', 250000),
(N'NCC00005', N'Công ty TNHH Dược Quốc Tế', '0927777777', 0);

-- NhanVien (NV00001, NV00002, ...) - CHỈ CÓ 2 CHỨC VỤ
INSERT INTO NhanVien (maNV, hoTen, chucVu, soDienThoai, ngaySinh, gioiTinh, diaChi, email, daXoa) VALUES
(N'NV00001', N'Nguyễn Văn An', N'Nhân viên quản lý', '0911111111', '1990-05-10', N'Nam', N'123 Lê Lợi, Q1, TP.HCM', 'vanan@gmail.com', 0),
(N'NV00002', N'Trần Thị Bình', N'Nhân viên bán thuốc', '0922222222', '1995-03-15', N'Nữ', N'456 Nguyễn Huệ, Q1, TP.HCM', 'thibinh@gmail.com', 0),
(N'NV00003', N'Lê Văn Cường', N'Nhân viên bán thuốc', '0933333333', '1988-07-20', N'Nam', N'789 Trần Hưng Đạo, Q5, TP.HCM', 'vancuong@gmail.com', 0),
(N'NV00004', N'Phạm Thị Dung', N'Nhân viên bán thuốc', '0944444444', '1998-11-25', N'Nữ', N'321 Võ Văn Tần, Q3, TP.HCM', 'thidung@gmail.com', 0),
(N'NV00005', N'Hoàng Minh Em', N'Nhân viên quản lý', '0955555555', '1992-09-08', N'Nam', N'654 Lý Thường Kiệt, Q10, TP.HCM', 'minhem@gmail.com', 0);

-- KhachHang (KH00001, KH00002, ...)
INSERT INTO KhachHang (maKH, hoTen, soDienThoai, email) VALUES
(N'KH00001', N'Phạm Thị Diệu', '0961111111', 'dieu@gmail.com'),
(N'KH00002', N'Ngô Văn Em', '0962222222', 'em@gmail.com'),
(N'KH00003', N'Võ Thị Phương', '0963333333', 'phuong@gmail.com'),
(N'KH00004', N'Đặng Quốc Gia', '0964444444', 'gia@gmail.com'),
(N'KH00005', N'Trương Thị Hà', '0965555555', 'ha@gmail.com'),
(N'KH00006', N'Lý Văn Hùng', '0966666666', 'hung@gmail.com'),
(N'KH00007', N'Bùi Thị Lan', '0967777777', 'lan@gmail.com'),
(N'KH00008', N'Nguyễn Minh Khoa', '0968888888', 'khoa@gmail.com'),
(N'KH00009', N'Lê Thanh Mai', '0969999999', 'mai@gmail.com'),
(N'KH00010', N'Phan Quốc Bảo', '0970000000', 'bao@gmail.com');

-- Thue (TE00001, TE00002, ...)
INSERT INTO Thue (maThue, tenThue, phanTramThue) VALUES
(N'TE00001', N'Thuế VAT 5%', 5.00)

-- DanhMucThuoc (DM00001, DM00002, ...)
INSERT INTO DanhMucThuoc (maDanhMuc, tenDanhMuc) VALUES
(N'DM00001', N'Giảm đau - Hạ sốt'),
(N'DM00002', N'Tiêu hóa'),
(N'DM00003', N'Vitamin - Khoáng chất'),
(N'DM00004', N'Thực phẩm chức năng'),
(N'DM00005', N'Tim mạch'),
(N'DM00006', N'Kháng sinh'),
(N'DM00007', N'Hô hấp'),
(N'DM00008', N'Chống dị ứng'),
(N'DM00009', N'Sát khuẩn - Khử trùng');

-- KhuyenMai (KM00001, KM00002, ...)
INSERT INTO KhuyenMai (maKM, tenKM, ngayBatDau, ngayKetThuc, phanTramGiamGia) VALUES
(N'KM00001', N'Khuyến mãi Tết Nguyên Đán', '2026-01-01', '2026-01-31', 10.00),
(N'KM00002', N'Khuyến mãi mùa hè', '2026-06-01', '2026-06-30', 5.00),
(N'KM00003', N'Khuyến mãi 30/4 - 1/5', '2026-04-28', '2026-05-03', 15.00),
(N'KM00004', N'Khuyến mãi Black Friday', '2026-11-25', '2026-11-30', 20.00);

-- Thuoc (TH00001, TH00002, ...)
INSERT INTO Thuoc (maThuoc, tenThuoc, donViTinh, giaBan, soLuongTon, hanSuDung, moTa, maDanhMuc, hinhAnh, thanhPhan, ngaySanXuat, xuatXu) VALUES
(N'TH00001', N'Hapacol 650 DHG', N'Hộp 10 vỉ x 10 viên', 25000, 1021, '2025-10-15', N'Thuốc giảm đau, hạ sốt hiệu quả', N'DM00001', N'img_product/hapacol_650_extra_dhg.png', N'Paracetamol 650mg', '2024-02-15', N'Việt Nam'),
(N'TH00002', N'Bột pha hỗn dịch uống Smecta vị cam', N'Hộp 10 gói', 4000, 1021, '2025-11-21', N'Điều trị tiêu chảy cấp và mạn tính', N'DM00002', N'img_product/bot-pha-hon-dich-uong-smecta.jpg', N'Diosmectite 3g', '2024-05-21', N'Pháp'),
(N'TH00003', N'Siro C.C Life 100mg/5ml Foripharm', N'Chai 120ml', 30000, 1032, '2025-12-01', N'Bổ sung vitamin C cho cơ thể', N'DM00003', N'img_product/C.c-Life-100MgChai.jpg', N'Vitamin C 100mg/5ml', '2024-03-01', N'Việt Nam'),
(N'TH00004', N'Panadol Extra đỏ', N'Hộp 12 vỉ x 10 viên', 250000, 1034, '2026-01-07', N'Giảm đau nhanh, hạ sốt hiệu quả', N'DM00001', N'img_product/Panadol-Extra.png', N'Paracetamol 500mg, Caffeine 65mg', '2024-08-07', N'Anh'),
(N'TH00005', N'Viên sủi Vitatrum C BRV', N'Tuýp 20 viên', 24000, 1076, '2026-02-28', N'Bổ sung vitamin C, tăng cường đề kháng', N'DM00003', N'img_product/vitatrum-c-brv.png', N'Vitamin C 1000mg', '2024-12-31', N'Việt Nam'),
(N'TH00006', N'Bổ Gan Trường Phúc', N'Hộp 30 viên', 95000, 1034, '2026-03-15', N'Hỗ trợ bảo vệ và phục hồi chức năng gan', N'DM00004', N'img_product/bo-gan-tuong-phu.jpg', N'Diệp hạ châu, Đảng Sâm, Bạch truật, Cam thảo, Phục Linh, Nhân trần, Trần bì', '2024-02-15', N'Việt Nam'),
(N'TH00007', N'Bài Thạch Trường Phúc', N'Hộp 30 viên', 95000, 1076, '2026-04-10', N'Hỗ trợ điều trị sỏi thận, sỏi mật', N'DM00004', N'img_product/bai-trang-truong-phuc.jpg', N'Xa tiền tử, Bạch mao căn, Sinh Địa, Ý Dĩ, Kim tiền thảo', '2024-02-10', N'Việt Nam'),
(N'TH00008', N'Đại Tràng Trường Phúc', N'Hộp 30 viên', 105000, 1021, '2026-05-03', N'Hỗ trợ điều trị viêm đại tràng, rối loạn tiêu hóa', N'DM00004', N'img_product/dai-trang-truong-phuc.jpg', N'Hoàng liên, Mộc hương, Bạch truật, Bạch thược, Ngũ bội tử, Hậu phác, Cam thảo, Xa tiền tử, Hoạt thạch', '2024-09-03', N'Việt Nam'),
(N'TH00009', N'Ninh Tâm Vương Hồng Bàng', N'Hộp 60 viên', 180000, 1054, '2026-06-15', N'Hỗ trợ tim mạch, giảm stress, cải thiện giấc ngủ', N'DM00005', N'img_product/ninh-tam-vuong-hong-bang.png', N'L-Carnitine, Taurine, Đan sâm, Khổ sâm bắc, Nattokinase, Hoàng đằng, Magie', '2024-08-15', N'Việt Nam'),
(N'TH00010', N'Paracetamol 500mg', N'Hộp 10 vỉ x 10 viên', 15000, 2000, '2026-07-31', N'Thuốc giảm đau, hạ sốt phổ biến', N'DM00001', N'img_product/paracetamol.png', N'Paracetamol 500mg', '2024-06-01', N'Việt Nam'),
(N'TH00011', N'Amoxicillin 500mg', N'Hộp 10 vỉ x 10 viên', 45000, 1500, '2026-08-20', N'Kháng sinh điều trị nhiễm khuẩn đường hô hấp', N'DM00006', N'img_product/amoxicillin.png', N'Amoxicillin 500mg', '2024-04-20', N'Ấn Độ'),
(N'TH00012', N'ORS', N'Hộp 20 gói', 50000, 800, '2026-09-15', N'Bù nước và điện giải khi tiêu chảy', N'DM00002', N'img_product/ors.png', N'Natri clorua, Kali clorua, Natri citrat, Glucose', '2025-06-15', N'Thái Lan'),
(N'TH00013', N'Vitamin B Complex', N'Chai 100 viên', 85000, 600, '2026-10-30', N'Bổ sung vitamin B tổng hợp', N'DM00003', N'img_product/vitamin-b.png', N'Vitamin B1, B2, B6, B12', '2025-03-30', N'Mỹ'),
(N'TH00014', N'Tây Thi Hoàn', N'Hộp 10 viên', 120000, 450, '2026-11-25', N'Hỗ trợ tiêu hóa, giảm đầy hơi, khó tiêu', N'DM00004', N'img_product/tay-thi-hoan.png', N'Trần bì, Bạch truật, Hậu phác, Mộc hương', '2024-11-25', N'Trung Quốc'),
(N'TH00015', N'Eugica', N'Hộp 20 gói x 5ml', 85000, 750, '2026-12-10', N'Siro ho, long đờm cho trẻ em', N'DM00007', N'img_product/eugica.png', N'Cao lá bạc hà, Cao lá khuynh diệp, Tinh dầu húng', '2024-07-10', N'Việt Nam'),
(N'TH00016', N'Cetirizine 10mg', N'Hộp 10 vỉ x 10 viên', 35000, 1200, '2027-01-18', N'Thuốc chống dị ứng', N'DM00008', N'img_product/cetirizine.png', N'Cetirizine dihydrochloride 10mg', '2024-09-18', N'Việt Nam'),
(N'TH00017', N'Glucosamine 1500mg', N'Hộp 30 gói', 420000, 300, '2027-02-20', N'Hỗ trợ xương khớp', N'DM00004', N'img_product/glucosamine.png', N'Glucosamine sulfate 1500mg', '2025-01-20', N'Hàn Quốc'),
(N'TH00018', N'Omega 3 Fish Oil', N'Chai 100 viên', 350000, 400, '2027-03-15', N'Bổ sung Omega 3 tốt cho tim mạch', N'DM00003', N'img_product/omega3.png', N'EPA 180mg, DHA 120mg', '2025-04-15', N'Na Uy'),
(N'TH00019', N'Betadine 10%', N'Chai 125ml', 65000, 850, '2027-04-30', N'Dung dịch sát khuẩn vết thương', N'DM00009', N'img_product/betadine.png', N'Povidone Iodine 10%', '2024-06-30', N'Thái Lan'),
(N'TH00020', N'Cảm Xuyên Hương', N'Hộp 50 viên', 55000, 950, '2027-10-01', N'Thuốc cảm cúm theo đông y', N'DM00004', N'img_product/cam-xuyen-huong.png', N'Hương nhu, Tử tô, Hoa cúc, Bạc hà', '2024-08-22', N'Việt Nam');

-- Tạo Phiếu Nhập Hàng 
INSERT INTO PhieuNhapHang (maPhieuNhap, maNV, maNCC, ngayNhap) VALUES
-- Tháng 12/2025
(N'PN00001', N'NV00003', N'NCC00001', '2025-12-01'),
(N'PN00002', N'NV00003', N'NCC00002', '2025-12-05'),
(N'PN00003', N'NV00002', N'NCC00003', '2025-12-09'),

-- Tháng 12/2025
(N'PN00004', N'NV00003', N'NCC00001', '2025-12-13'),
(N'PN00005', N'NV00002', N'NCC00002', '2025-12-17'),
(N'PN00006', N'NV00003', N'NCC00003', '2025-12-21'),

-- Tháng 12/2025
(N'PN00007', N'NV00003', N'NCC00001', '2025-12-24'),
(N'PN00008', N'NV00002', N'NCC00002', '2025-12-27'),
(N'PN00009', N'NV00003', N'NCC00003', '2025-12-30'),

-- Tháng 1/2026
(N'PN00010', N'NV00002', N'NCC00001', '2026-01-04'),
(N'PN00011', N'NV00003', N'NCC00002', '2026-01-10'),
(N'PN00012', N'NV00002', N'NCC00003', '2026-01-16'),

-- Tháng 1/2026
(N'PN00013', N'NV00003', N'NCC00001', '2026-01-22'),
(N'PN00014', N'NV00002', N'NCC00002', '2026-01-27'),
(N'PN00015', N'NV00003', N'NCC00003', '2026-01-31'),

-- Tháng 2/2026
(N'PN00016', N'NV00002', N'NCC00001', '2026-02-05'),
(N'PN00017', N'NV00003', N'NCC00002', '2026-02-11'),
(N'PN00018', N'NV00002', N'NCC00003', '2026-02-18'),

-- Tháng 2/2026
(N'PN00019', N'NV00003', N'NCC00001', '2026-02-23'),
(N'PN00020', N'NV00002', N'NCC00002', '2026-02-27'),
(N'PN00021', N'NV00003', N'NCC00003', '2026-02-28'),

-- Tháng 3/2026
(N'PN00022', N'NV00002', N'NCC00001', '2026-03-05'),
(N'PN00023', N'NV00003', N'NCC00002', '2026-03-12'),
(N'PN00024', N'NV00002', N'NCC00003', '2026-03-20'),

-- Tháng 3/2026
(N'PN00025', N'NV00003', N'NCC00001', '2026-03-24'),
(N'PN00026', N'NV00002', N'NCC00002', '2026-03-28'),
(N'PN00027', N'NV00003', N'NCC00003', '2026-03-31'),

-- Tháng 4/2026
(N'PN00028', N'NV00002', N'NCC00001', '2026-04-10'),
(N'PN00029', N'NV00003', N'NCC00002', '2026-04-20');

GO
-- Chi tiết phiếu nhập để đạt đúng số lượng tồn kho
-- TH00001: Hapacol 650 DHG - Tồn kho mục tiêu: 1021
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00001', N'TH00001', 300, 20000),
(N'PN00007', N'TH00001', 400, 21000),
(N'PN00013', N'TH00001', 321, 22000);

-- TH00002: Smecta - Tồn kho mục tiêu: 1021
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00002', N'TH00002', 500, 3000),
(N'PN00008', N'TH00002', 321, 3200),
(N'PN00014', N'TH00002', 200, 3500);

-- TH00003: Siro C.C Life - Tồn kho mục tiêu: 1032
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00003', N'TH00003', 400, 25000),
(N'PN00009', N'TH00003', 332, 26000),
(N'PN00015', N'TH00003', 300, 27000);

-- TH00004: Panadol Extra - Tồn kho mục tiêu: 1034
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00004', N'TH00004', 500, 240000),
(N'PN00010', N'TH00004', 334, 245000),
(N'PN00016', N'TH00004', 200, 248000);

-- TH00005: Vitatrum C - Tồn kho mục tiêu: 1076
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00005', N'TH00005', 400, 20000),
(N'PN00011', N'TH00005', 376, 21000),
(N'PN00017', N'TH00005', 300, 22000);

-- TH00006: Bổ Gan Trường Phúc - Tồn kho mục tiêu: 1034
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00006', N'TH00006', 500, 85000),
(N'PN00012', N'TH00006', 334, 88000),
(N'PN00018', N'TH00006', 200, 90000);

-- TH00007: Bài Thạch Trường Phúc - Tồn kho mục tiêu: 1076
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00001', N'TH00007', 400, 85000),
(N'PN00013', N'TH00007', 376, 88000),
(N'PN00019', N'TH00007', 300, 90000);

-- TH00008: Đại Tràng Trường Phúc - Tồn kho mục tiêu: 1021
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00002', N'TH00008', 350, 95000),
(N'PN00014', N'TH00008', 371, 98000),
(N'PN00020', N'TH00008', 300, 100000);

-- TH00009: Ninh Tâm Vương - Tồn kho mục tiêu: 1054
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00003', N'TH00009', 400, 170000),
(N'PN00015', N'TH00009', 354, 175000),
(N'PN00021', N'TH00009', 300, 178000);

-- TH00010: Paracetamol 500mg - Tồn kho mục tiêu: 2000
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00004', N'TH00010', 600, 12000),
(N'PN00010', N'TH00010', 700, 12500),
(N'PN00016', N'TH00010', 700, 13000);

-- TH00011: Amoxicillin 500mg - Tồn kho mục tiêu: 1500
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00005', N'TH00011', 500, 35000),
(N'PN00011', N'TH00011', 500, 37000),
(N'PN00022', N'TH00011', 500, 38000);

-- TH00012: ORS - Tồn kho mục tiêu: 800
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00006', N'TH00012', 300, 45000),
(N'PN00017', N'TH00012', 300, 46000),
(N'PN00023', N'TH00012', 200, 48000);

-- TH00013: Vitamin B Complex - Tồn kho mục tiêu: 600
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00007', N'TH00013', 250, 80000),
(N'PN00018', N'TH00013', 200, 82000),
(N'PN00024', N'TH00013', 150, 83000);

-- TH00014: Tây Thi Hoàn - Tồn kho mục tiêu: 450
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00008', N'TH00014', 200, 110000),
(N'PN00019', N'TH00014', 150, 115000),
(N'PN00025', N'TH00014', 100, 118000);

-- TH00015: Eugica - Tồn kho mục tiêu: 750
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00009', N'TH00015', 300, 78000),
(N'PN00020', N'TH00015', 250, 80000),
(N'PN00026', N'TH00015', 200, 82000);

-- TH00016: Cetirizine 10mg - Tồn kho mục tiêu: 1200
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00010', N'TH00016', 400, 28000),
(N'PN00016', N'TH00016', 400, 30000),
(N'PN00027', N'TH00016', 400, 32000);

-- TH00017: Glucosamine 1500mg - Tồn kho mục tiêu: 300
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00011', N'TH00017', 150, 400000),
(N'PN00021', N'TH00017', 150, 410000);

-- TH00018: Omega 3 Fish Oil - Tồn kho mục tiêu: 400
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00012', N'TH00018', 200, 330000),
(N'PN00022', N'TH00018', 200, 340000);

-- TH00019: Betadine 10% - Tồn kho mục tiêu: 850
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00013', N'TH00019', 350, 60000),
(N'PN00023', N'TH00019', 300, 62000),
(N'PN00028', N'TH00019', 200, 63000);

-- TH00020: Cảm Xuyên Hương - Tồn kho mục tiêu: 950
INSERT INTO ChiTietPhieuNhap (maPhieuNhap, maThuoc, soLuong, donGia) VALUES
(N'PN00014', N'TH00020', 400, 50000),
(N'PN00024', N'TH00020', 350, 52000),
(N'PN00029', N'TH00020', 200, 53000);


-- PhieuDatThuoc (PDT00001, PDT00002, ...)
INSERT INTO PhieuDatThuoc (maPhieuDat, ngayDat, maKH, diaChi, hinhThucThanhToan, trangThai) VALUES
(N'PDT00001', '2025-12-15', N'KH00001', N'123 Lê Lợi, Q1, TP.HCM', N'Tại chỗ', N'Đã hoàn thành'),
(N'PDT00002', '2026-01-12', N'KH00002', N'456 Nguyễn Huệ, Q1, TP.HCM', N'Thanh toán online', N'Đã hoàn thành'),
(N'PDT00003', '2026-03-15', N'KH00003', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'Tại chỗ', N'Đã hoàn thành'),
(N'PDT00004', '2026-04-18', N'KH00004', N'321 Võ Văn Tần, Q3, TP.HCM', N'Thanh toán online', N'Đã hoàn thành'),
(N'PDT00005', '2026-05-06', N'KH00008', N'99 Nguyễn Trãi, Q5, TP.HCM', N'Thanh toán online', N'Chưa hoàn thành'),
(N'PDT00006', '2026-05-20', N'KH00009', N'15 Lý Tự Trọng, Q1, TP.HCM', N'Tại chỗ', N'Chưa hoàn thành');

-- ChiTietPhieuDatThuoc
INSERT INTO ChiTietPhieuDatThuoc (maPhieuDat, maThuoc, soLuong, donGia) VALUES
(N'PDT00001', N'TH00001', 5, 25000),
(N'PDT00001', N'TH00003', 2, 30000),
(N'PDT00002', N'TH00010', 10, 15000),
(N'PDT00002', N'TH00002', 5, 4000),
(N'PDT00003', N'TH00005', 3, 24000),
(N'PDT00003', N'TH00016', 4, 35000),
(N'PDT00004', N'TH00011', 2, 45000),
(N'PDT00004', N'TH00019', 1, 65000),
(N'PDT00005', N'TH00006', 2, 95000),
(N'PDT00005', N'TH00015', 3, 85000),
(N'PDT00006', N'TH00003', 4, 30000),
(N'PDT00006', N'TH00012', 2, 50000);

-- HoaDon (HD00001, HD00002, ...)
INSERT INTO HoaDon (maHD, ngayLap, maThue, maNV, maKH, maKM, maPhieuDat) VALUES
(N'HD00001', '2025-12-15', N'TE00001', N'NV00002', N'KH00001', N'KM00001', N'PDT00001'),
(N'HD00002', '2026-01-12', N'TE00001', N'NV00002', N'KH00002', NULL, N'PDT00002'),
(N'HD00003', '2026-04-18', N'TE00001', N'NV00004', N'KH00004', N'KM00002', N'PDT00004'),
(N'HD00004', '2026-03-16', N'TE00001', N'NV00003', N'KH00003', NULL, N'PDT00003'),
(N'HD00005', '2025-12-22', N'TE00001', N'NV00002', N'KH00005', NULL, NULL),
(N'HD00006', '2026-01-05', N'TE00001', N'NV00004', N'KH00006', N'KM00001', NULL),
(N'HD00007', '2026-01-28', N'TE00001', N'NV00003', N'KH00007', NULL, NULL),
(N'HD00008', '2026-02-08', N'TE00001', N'NV00002', N'KH00001', NULL, NULL),
(N'HD00009', '2026-02-19', N'TE00001', N'NV00004', N'KH00008', NULL, NULL),
(N'HD00010', '2026-03-03', N'TE00001', N'NV00003', N'KH00009', NULL, NULL),
(N'HD00011', '2026-03-12', N'TE00001', N'NV00002', N'KH00010', N'KM00003', NULL),
(N'HD00012', '2026-03-25', N'TE00001', N'NV00004', N'KH00002', NULL, NULL),
(N'HD00013', '2026-04-05', N'TE00001', N'NV00003', N'KH00008', NULL, NULL),
(N'HD00014', '2026-04-12', N'TE00001', N'NV00002', N'KH00005', NULL, NULL),
(N'HD00015', '2026-04-24', N'TE00001', N'NV00004', N'KH00001', NULL, NULL);

-- ChiTietHoaDon
INSERT INTO ChiTietHoaDon (maHD, maThuoc, soLuong, donGia) VALUES
(N'HD00001', N'TH00001', 5, 25000),
(N'HD00001', N'TH00003', 2, 30000),
(N'HD00002', N'TH00010', 10, 15000),
(N'HD00002', N'TH00002', 5, 4000),
(N'HD00003', N'TH00011', 2, 45000),
(N'HD00003', N'TH00019', 1, 65000),
(N'HD00004', N'TH00005', 3, 24000),
(N'HD00004', N'TH00016', 4, 35000),
(N'HD00005', N'TH00004', 1, 250000),
(N'HD00005', N'TH00012', 2, 50000),
(N'HD00006', N'TH00010', 8, 15000),
(N'HD00007', N'TH00015', 2, 85000),
(N'HD00007', N'TH00002', 4, 4000),
(N'HD00008', N'TH00001', 3, 25000),
(N'HD00008', N'TH00011', 1, 45000),
(N'HD00009', N'TH00006', 2, 95000),
(N'HD00010', N'TH00003', 2, 30000),
(N'HD00010', N'TH00014', 1, 120000),
(N'HD00011', N'TH00017', 1, 420000),
(N'HD00012', N'TH00019', 2, 65000),
(N'HD00012', N'TH00016', 2, 35000),
(N'HD00013', N'TH00008', 1, 105000),
(N'HD00013', N'TH00009', 1, 180000),
(N'HD00014', N'TH00012', 3, 50000),
(N'HD00015', N'TH00018', 1, 350000),
(N'HD00015', N'TH00020', 2, 55000);

-- TaiKhoan - CHỈ CÓ 2 VAI TRÒ
INSERT INTO TaiKhoan (tenDangNhap, matKhau, trangThai, maNV, vaiTro) VALUES
(N'admin', N'Admin@123', N'Hoạt động', N'NV00001', N'Nhân viên quản lý'),
(N'nvbanthuoc1', N'Pass@123', N'Hoạt động', N'NV00002', N'Nhân viên bán thuốc'),
(N'nvbanthuoc2', N'Pass@123', N'Hoạt động', N'NV00003', N'Nhân viên bán thuốc'),
(N'nvbanthuoc3', N'Pass@123', N'Hoạt động', N'NV00004', N'Nhân viên bán thuốc'),
(N'quanly1', N'Manager@2025', N'Hoạt động', N'NV00005', N'Nhân viên quản lý');

GO

-- =============================================
-- HÀM TỰ ĐỘNG TẠO MÃ
-- =============================================

-- Hàm tạo mã Thuốc
CREATE FUNCTION fn_GenerateMaThuoc()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maThuoc, 3, 5) AS INT)), 0)
    FROM Thuoc
    WHERE maThuoc LIKE 'TH%';
    
    SET @newMa = 'TH' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Nhà Cung Cấp
CREATE FUNCTION fn_GenerateMaNCC()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maNCC, 4, 5) AS INT)), 0)
    FROM NhaCungCap
    WHERE maNCC LIKE 'NCC%';
    
    SET @newMa = 'NCC' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Khách Hàng
CREATE FUNCTION fn_GenerateMaKH()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maKH, 3, 5) AS INT)), 0)
    FROM KhachHang
    WHERE maKH LIKE 'KH%';
    
    SET @newMa = 'KH' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Nhân Viên
CREATE FUNCTION fn_GenerateMaNV()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maNV, 3, 5) AS INT)), 0)
    FROM NhanVien
    WHERE maNV LIKE 'NV%';
    
    SET @newMa = 'NV' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Danh Mục
CREATE FUNCTION fn_GenerateMaDanhMuc()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maDanhMuc, 3, 5) AS INT)), 0)
    FROM DanhMucThuoc
    WHERE maDanhMuc LIKE 'DM%';
    
    SET @newMa = 'DM' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Hóa Đơn
CREATE FUNCTION fn_GenerateMaHD()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maHD, 3, 5) AS INT)), 0)
    FROM HoaDon
    WHERE maHD LIKE 'HD%';
    
    SET @newMa = 'HD' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Khuyến Mãi
CREATE FUNCTION fn_GenerateMaKM()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maKM, 3, 5) AS INT)), 0)
    FROM KhuyenMai
    WHERE maKM LIKE 'KM%';
    
    SET @newMa = 'KM' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Phiếu Đặt Thuốc
CREATE FUNCTION fn_GenerateMaPhieuDat()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maPhieuDat, 4, 5) AS INT)), 0)
    FROM PhieuDatThuoc
    WHERE maPhieuDat LIKE 'PDT%';
    
    SET @newMa = 'PDT' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Phiếu Nhập
CREATE FUNCTION fn_GenerateMaPhieuNhap()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maPhieuNhap, 3, 5) AS INT)), 0)
    FROM PhieuNhapHang
    WHERE maPhieuNhap LIKE 'PN%';
    
    SET @newMa = 'PN' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- Hàm tạo mã Thuế
CREATE FUNCTION fn_GenerateMaThue()
RETURNS NVARCHAR(20)
AS
BEGIN
    DECLARE @maxNumber INT;
    DECLARE @newMa NVARCHAR(20);
    
    SELECT @maxNumber = ISNULL(MAX(CAST(SUBSTRING(maThue, 3, 5) AS INT)), 0)
    FROM Thue
    WHERE maThue LIKE 'TE%';
    
    SET @newMa = 'TE' + RIGHT('00000' + CAST(@maxNumber + 1 AS VARCHAR), 5);
    RETURN @newMa;
END;
GO

-- =============================================
-- TEST HÀM TẠO MÃ
-- =============================================
PRINT N'=== TEST CÁC HÀM TẠO MÃ ===';
SELECT dbo.fn_GenerateMaThuoc() AS MaThuocMoi;
SELECT dbo.fn_GenerateMaNCC() AS MaNCCMoi;
SELECT dbo.fn_GenerateMaKH() AS MaKHMoi;
SELECT dbo.fn_GenerateMaNV() AS MaNVMoi;
SELECT dbo.fn_GenerateMaDanhMuc() AS MaDanhMucMoi;
SELECT dbo.fn_GenerateMaHD() AS MaHDMoi;
SELECT dbo.fn_GenerateMaKM() AS MaKMMoi;
SELECT dbo.fn_GenerateMaPhieuDat() AS MaPhieuDatMoi;
SELECT dbo.fn_GenerateMaPhieuNhap() AS MaPhieuNhapMoi;
SELECT dbo.fn_GenerateMaThue() AS MaThueMoi;

PRINT N'';
PRINT N' Tạo database thành công!';
PRINT N' Database: QLHieuThuocTay';
PRINT N' Ngày tạo: 2025-10-13';
PRINT N' Tạo bởi: anhhoang19122005';
PRINT N'';
PRINT N'=== THÔNG TIN RÀNG BUỘC ===';
PRINT N' Chức vụ nhân viên: Nhân viên bán thuốc | Nhân viên quản lý';
PRINT N' Vai trò tài khoản: Nhân viên bán thuốc | Nhân viên quản lý';
PRINT N' Trạng thái tài khoản: Hoạt động | Ngừng hoạt động';
PRINT N' Giới tính: Nam | Nữ';
PRINT N' Hình thức thanh toán: Thanh toán online | Tại chỗ';
PRINT N' Trạng thái phiếu đặt: Đã hoàn thành | Chưa hoàn thành';
PRINT N' Phần trăm thuế: >= 0';
PRINT N' Phần trăm giảm giá: 0 < % <= 100';
PRINT N' Công nợ: >= 0';
PRINT N' Giá bán, đơn giá, số lượng: >= 0';

GO