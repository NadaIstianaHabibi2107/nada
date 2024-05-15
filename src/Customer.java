import java.sql.Statement;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private String email;
    private String nama;
    private String peran = "Customer";
    private String noHP;
    private String password;
    private String alamat;
    Statement statement;

    public void createTabelCustomer() throws SQLException {
        statement = ConnectionDB.getConnection().createStatement();
        String queryCreate = """
                CREATE TABLE IF NOT EXISTS customer (
                    id_customer int PRIMARY KEY AUTO_INCREMENT,
                    nama varchar(255) NOT NULL,
                    noHP varchar(13) NOT NULL UNIQUE,
                    alamat varchar(255) NOT NULL)
                    """;
        statement.execute(queryCreate);
    }

    public void createTableUser() throws SQLException {
        statement = ConnectionDB.getConnection().createStatement();
        statement.execute(
                "CREATE TABLE IF NOT EXISTS user (id_user int PRIMARY KEY AUTO_INCREMENT, id_customer int NOT NULL, email varchar(255) NOT NULL UNIQUE, password varchar(255) NOT NULL,peran varchar(30) NOT NULL)");
    }

    public boolean setEmail(String inputEmail) throws SQLException {
        this.createTableUser();
        String regexPatternEmail = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!Pattern.compile(regexPatternEmail).matcher(inputEmail).matches()) {
            System.out.println("Email tidak valid. Masukkan kembali email anda");
            return false;
        } else {
            String querySelect = "SELECT email from user WHERE email = ?";
            PreparedStatement stmt = ConnectionDB.getConnection().prepareStatement(querySelect);
            stmt.setString(1, inputEmail);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                System.out.println("Email sudah terdaftar!");
                return false;
            } else {
                email = inputEmail;
                return true;
            }

        }
    }

    public boolean setNoHP(String inputNoHP) {
        String regexPetternHP = "^(\\+62|62|0)8[1-9][0-9]{6,9}$";
        if (!Pattern.matches(regexPetternHP, inputNoHP)) {
            System.out.println("Nomor HP tidak sesuai");
            return false;
        } else {
            noHP = inputNoHP;
            return true;
        }
    }

    public boolean setNama(String inputNama) {
        if (inputNama.length() == 0) {
            System.out.println("Nama tidak boleh kosong");
            return false;
        } else {
            nama = inputNama;
            return true;
        }
    }

    public boolean setAlamat(String inputAlamat) {
        if (inputAlamat.length() == 0) {
            System.out.println("Alamat tidak boleh kosong");
            return false;
        } else {
            alamat = inputAlamat;
            return true;
        }
    }

    public boolean setPassword(String inputPassword) {
        String regexPatternPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!?&])(?=\\S+$).{8,20}$";
        System.out.println(Pattern.matches(regexPatternPassword, inputPassword));
        if (!Pattern.compile(regexPatternPassword).matcher(inputPassword).matches()) {
            System.out.println(
                    "Password harus minimal 8 karakter dan maksimal 20 karakter yang terdiri dari kombinasi huruf kapital, huruf kecil, karakter dan angka");
            return false;
        } else {
            password = inputPassword;
            return true;
        }
    }

    public boolean setKonfirmasiPassword(String konfirmasiPassword) {
        if (!password.equals(konfirmasiPassword)) {
            System.out.println("Konfirmasi Password tidak sesuai");
            return false;
        } else {
            return true;
        }
    }

    public boolean insertCustomer() throws SQLException {
        this.createTabelCustomer();
        this.createTableUser();
        String queryInsertCustomer = "INSERT INTO customer (nama, noHP, alamat) VALUE (?, ?, ?)";
        PreparedStatement stmt = ConnectionDB.getConnection().prepareStatement(queryInsertCustomer,
                Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, nama);
        stmt.setString(2, noHP);
        stmt.setString(3, alamat);
        int result = stmt.executeUpdate();
        if (result == 0) {
            System.out.println("Registrasi Gagal");
            return false;
        } else {
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id_customer = generatedKeys.getInt(1);
                    String queryInsertUser = "INSERT INTO user (id_customer, email, password, peran) VALUE (?, ?, ?, ?)";
                    PreparedStatement stmt2 = ConnectionDB.getConnection().prepareStatement(queryInsertUser);
                    stmt2.setInt(1, id_customer);
                    stmt2.setString(2, email);
                    stmt2.setString(3, password);
                    stmt2.setString(4, peran);
                    int resultUser = stmt2.executeUpdate();
                    if (resultUser == 0) {
                        System.out.println("Maaf! pendaftaran gagal");
                        return false;
                    } else {
                        System.out.println("Pendaftaran berhasil");
                        return true;
                    }
                } else {
                    throw new SQLException("Gagal Membuat akun user");
                }
            }
        }
    }

    public String login(String inputEmail, String inputPassword) throws SQLException {
        String querySelect = "SELECT peran, id_customer FROM user WHERE email = ? and password = ?";
        PreparedStatement stmt = ConnectionDB.getConnection().prepareStatement(querySelect);
        stmt.setString(1, inputEmail);
        stmt.setString(2, inputPassword);
        ResultSet result = stmt.executeQuery();
        if (result.next()) {
            String peran = result.getString("peran");
            String id_customer = result.getString("id_customer");
            System.out.println("Peran " + peran);
            if (peran.equals("Customer")) {
                String querySelectCustomer = "SELECT * from customer WHERE id_customer = ?";
                PreparedStatement stmt2 = ConnectionDB.getConnection().prepareStatement(querySelectCustomer);
                stmt2.setString(1, id_customer);
                ResultSet result2 = stmt2.executeQuery();
                if (result2.next()) {
                    System.out.println("Selamat Datang " + result2.getString("nama"));
                    return peran;
                }
            }
            return "Customer tidak ditemukan";
        } else {
            System.out.println("Email dan Password tidak ditemukan");
            email = inputEmail;
            return "Return user dan password tidak cocok";
        }
    }

    public boolean checkEmail(String inputEmail) throws SQLException {
        String query = "SELECT email FROM user WHERE email = ?";
        PreparedStatement statement = ConnectionDB.getConnection().prepareStatement(query);
        statement.setString(1, inputEmail);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            email = result.getString("email");
            return true;
        } else {
            System.out.println("Email tidak terdaftar");
            return false;
        }
    }

    public boolean forgotPassword() throws SQLException {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        PreparedStatement statement = ConnectionDB.getConnection().prepareStatement(query);
        statement.setString(1, password);
        statement.setString(2, email);
        System.out.println(email + password);
        int result = statement.executeUpdate();
        if (result > 0) {
            System.out.println("Password berhasil diubah");
            return true;
        } else {
            System.out.println("Password gagal diubah. Coba lagi");
            return false;
        }
    }

    public boolean deleteUser() throws SQLException {
        String query = "DELETE FROM user WHERE email = ?";
        PreparedStatement statement = ConnectionDB.getConnection().prepareStatement(query);
        statement.setString(1, email);
        int result = statement.executeUpdate();
        if (result > 0) {
            System.out.println("User berhasil dihapus");
            return true;
        } else {
            System.out.println("User gagal dihapus");
            return false;
        }
    }

}
