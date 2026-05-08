package smoke;

import ConnectDB.DatabaseConnection;

/**
 * Kiểm tra nhanh: JDBC + tải class các màn/DAO (không mở GUI).
 * Chạy: java -cp "bin;lib/*" smoke.SmokeRun
 */
public final class SmokeRun {

    private SmokeRun() {}

    public static void main(String[] args) {
        System.out.println("=== SmokeRun / QuanLyTiemThuocTay_MaiThuc ===");
        boolean dbOk = false;
        try {
            dbOk = DatabaseConnection.getInstance().testConnection();
        } catch (Throwable t) {
            System.err.println("Database: EXCEPTION — " + t.getMessage());
        }
        System.out.println("Database (SQL Server): " + (dbOk ? "OK" : "FAIL (kiểm tra SQL Server, DB QLHieuThuocTayMaiThuc, user sa)"));

        String[] mustLoad = {
            "dao.TaiKhoanDAO",
            "dao.ThuocDAO",
            "dao.HoaDonDAO",
            "dao.PhieuDatThuocDAO",
            "dao.PhieuNhapThuocDAO",
            "gui.form.Login",
            "gui.form.ManHinhChinh",
            "gui.form.formLapHoaDon",
            "gui.form.FormQuanLyHoaDon",
            "gui.form.formPhieuDatThuoc",
            "gui.form.FormPhieuNhapThuoc",
            "gui.form.formQuanLyThuoc",
            "gui.form.FormQuanLyKhachHang",
        };
        int ok = 0;
        for (String name : mustLoad) {
            try {
                Class.forName(name);
                ok++;
                System.out.println("  [OK] " + name);
            } catch (Throwable t) {
                System.err.println("  [FAIL] " + name + " — " + t.getClass().getSimpleName() + ": " + t.getMessage());
            }
        }
        System.out.println("Classes loaded: " + ok + "/" + mustLoad.length);
        System.out.println(dbOk && ok == mustLoad.length ? "RESULT: PASS (runtime + classpath)" : "RESULT: XEM CHI TIẾT Ở TRÊN");
    }
}
