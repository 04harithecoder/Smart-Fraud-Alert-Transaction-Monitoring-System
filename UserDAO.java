import java.sql.*;

public class UserDAO {

    // 🔹 REGISTER USER
    public String registerUser(String name, String email, String accNo, String password) {

        // ✅ Validate Account Number
        if (accNo == null || accNo.length() != 11 || !accNo.matches("\\d+")) {
            return "Account number must be exactly 11 digits!";
        }

        // ✅ Validate Name
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be empty!";
        }

        // ✅ Validate Password
        if (password == null || password.length() < 4) {
            return "Password must be at least 4 characters!";
        }

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                return "Database connection failed!";
            }

            // 🔍 Check if account already exists
            String checkQuery = "SELECT * FROM users WHERE account_no=?";
            PreparedStatement checkPs = con.prepareStatement(checkQuery);
            checkPs.setString(1, accNo);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                return "Account already exists!";
            }

            // 📝 Insert New User
            String insertQuery = "INSERT INTO users(name, account_no, password,email) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(insertQuery);

            ps.setString(1, name);
            ps.setString(2, accNo);
            ps.setString(3, password);
            ps.setString(4, email);

            int result = ps.executeUpdate();

            if (result > 0) {
                return "SUCCESS";
            } else {
                return "Registration failed!";
            }

        } catch (SQLException e) {
            return "Database Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // 🔹 LOGIN VALIDATION
    public String validateLogin(String accNo, String password) {

        // ✅ Basic Validation
        if (accNo == null || accNo.isEmpty() || password == null || password.isEmpty()) {
            return "Please enter all fields!";
        }

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                return "Database connection failed!";
            }

            String query = "SELECT * FROM users WHERE account_no=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, accNo);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return "SUCCESS";
            } else {
                return "Invalid Account Number or Password!";
            }

        } catch (SQLException e) {
            return "Database Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}