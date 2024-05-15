import java.sql.SQLException;
import java.util.Scanner;

public class App {
    Scanner scanner = new Scanner(System.in);
    Customer user = new Customer();
    private int pilihanMenu;

    public void inputNama() {
        System.out.println("Nama Lengkap: ");
        String inputNama = scanner.next();
        if (!user.setNama(inputNama)) {
            this.inputNama();
        }
    }

    public void inputEmail() throws SQLException {
        System.out.println("Email: ");
        String email = scanner.next();
        if (!user.setEmail(email)) {
            this.inputEmail();
        }
    }

    public void inputAlamat() {
        System.out.println("Alamat: ");
        String alamat = scanner.next();
        if (!user.setAlamat(alamat)) {
            this.inputAlamat();
        }
    }

    public void inputNoHP() {
        System.out.println("Nomor HP: ");
        String noHP = scanner.next();
        if (!user.setNoHP(noHP)) {
            this.inputNoHP();
        }
    }

    public void inputPassword() {
        System.out.println("Password: ");
        String password = scanner.next();
        if (!user.setPassword(password)) {
            this.inputPassword();
        }
    }

    public void inputKonfirmasiPassword() throws SQLException {
        System.out.println("Konfirmasi Password: ");
        String konfirmasiPassword = scanner.next();
        if (!user.setKonfirmasiPassword(konfirmasiPassword)) {
            this.inputKonfirmasiPassword();
        } else {
            if (user.insertCustomer()) {
                this.pilihanMenu();
            }
        }
    }

    public void pilihanMenu() {
        System.out.println("Daftar Pesawat");

    }

    public void login() throws SQLException {
        System.out.println("Masukkan Email ");
        String inputEmail = scanner.next();
        System.out.println("Masukkan Password ");
        String inputPassword = scanner.next();

        String result = user.login(inputEmail, inputPassword);
        if (result.equals("Customer")) {

        } else if (result.equals("Admin")) {

        }
    }

    public void Registrasi() throws Exception {
        this.inputNama();
        this.inputEmail();
        this.inputAlamat();
        this.inputNoHP();
        this.inputPassword();
        this.inputKonfirmasiPassword();
    }

    // method ini digunakan untuk mengechek email customer yang
    // akan ganti password atau hapus akun
    public void inputEmailPassword() throws SQLException {
        System.out.println("Email: ");
        String email = scanner.next();
        if (!user.checkEmail(email)) {
            this.inputEmailPassword();
        }
    }

    public void forgotPassword() throws Exception {
        this.inputEmailPassword();
        this.inputPassword();
        user.forgotPassword();
    }

    public void deleteUser() throws Exception {
        this.inputEmailPassword();
        System.out.println("Apakah anda yakin menghapus akun anda ? Ya/Tidak");
        String konfirmasi = scanner.next();
        if (konfirmasi.equals("Ya")) {
            user.deleteUser();
        }
    }

    public static void main(String[] args) throws Exception {
        App app = new App();

        System.out.println("1. Registrasi");
        System.out.println("2. Login");
        System.out.println("3. Lupa password");
        System.out.println("4. Hapus akun");
        app.pilihanMenu = app.scanner.nextInt();
        if (app.pilihanMenu == 1) {
            app.Registrasi();
        } else if (app.pilihanMenu == 2) {
            app.login();
        } else if (app.pilihanMenu == 3) {
            app.forgotPassword();
        } else {
            app.deleteUser();
        }

    }
}
