package edu.omkar.repository;

import java.util.Optional;
import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.AdminLoginModel;

/**
 * Login rules:
 *  - adminlogin table  → usertype = 'admin'  (omkar/omkar)
 *  - users table       → usertype from DB    (omkar@gmail.com / rohan → regular users)
 */
public class ValidateAdminRepoImpl extends DBConfig implements ValidateAdminRepo {

    @Override
    public Optional<AdminLoginModel> validateAdmin(AdminLoginModel model) {

        System.out.println("=== LOGIN ===  user=[" + model.getUsername() + "]");

        if (conn == null) {
            System.out.println("DB conn NULL");
            return Optional.empty();
        }

        // ── 1. Check adminlogin table FIRST (omkar/omkar = admin) ───────────
        try {
            stmt = conn.prepareStatement(
                "SELECT username, password FROM adminlogin WHERE username = ? AND password = ?");
            stmt.setString(1, model.getUsername());
            stmt.setString(2, model.getPassword());
            rs = stmt.executeQuery();
            if (rs.next()) {
                AdminLoginModel u = new AdminLoginModel();
                u.setUserId(0);
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setUsertype("admin");
                System.out.println("MATCH: adminlogin → admin");
                return Optional.of(u);
            }
        } catch (Exception ex) {
            System.out.println("adminlogin error: " + ex.getMessage());
        }

        // ── 2. Check users table (regular users) ────────────────────────────
        try {
            stmt = conn.prepareStatement(
                "SELECT userid, username, password, usertype FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, model.getUsername());
            stmt.setString(2, model.getPassword());
            rs = stmt.executeQuery();
            if (rs.next()) {
                AdminLoginModel u = new AdminLoginModel();
                u.setUserId(rs.getInt("userid"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setUsertype("user");           // all users table entries = regular user
                System.out.println("MATCH: users → user (userid=" + u.getUserId() + ")");
                return Optional.of(u);
            }
        } catch (Exception ex) {
            System.out.println("users error: " + ex.getMessage());
        }

        System.out.println("NO MATCH in any table");
        return Optional.empty();
    }
}
