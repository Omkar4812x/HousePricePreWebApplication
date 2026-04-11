package edu.omkar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/house_price_prediction_system", "root", "Omkar@2004");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        showForm(response.getWriter(), "", "");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirm  = request.getParameter("confirm");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            showForm(out, "danger", "All fields are required.");
            return;
        }
        if (!password.equals(confirm)) {
            showForm(out, "danger", "Passwords do not match.");
            return;
        }

        try (Connection conn = getConn()) {
            // Check if username already taken
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT userid FROM users WHERE username = ?");
            stmt.setString(1, username.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                showForm(out, "danger", "Username '<b>" + username + "</b>' already exists. Please choose another.");
                return;
            }
            
            // Insert new user and fetch the generated ID
            stmt = conn.prepareStatement(
                "INSERT INTO users(username, password, usertype) VALUES(?, ?, 'user')", 
                Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username.trim());
            stmt.setString(2, password.trim());
            
            if (stmt.executeUpdate() > 0) {
                ResultSet rsKeys = stmt.getGeneratedKeys();
                int newUserId = 0;
                if (rsKeys.next()) {
                    newUserId = rsKeys.getInt(1);
                }
                
                // Registration successful! Create session automatically.
                HttpSession session = request.getSession();
                session.setAttribute("userid", newUserId);
                session.setAttribute("username", username.trim());
                session.setAttribute("usertype", "user");
                
                // Immediately push to user dashboard
                response.sendRedirect("userhome");
                
            } else {
                showForm(out, "danger", "Registration failed. Try again.");
            }
        } catch (Exception ex) {
            System.out.println("Register error: " + ex);
            showForm(out, "danger", "Error: " + ex.getMessage());
        }
    }

    private void showForm(PrintWriter out, String alertType, String alertMsg) {
        out.println("<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Register - House Price Prediction</title>");
        out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("<link href='https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap' rel='stylesheet'>");
        out.println("<style>");
        out.println("body { font-family: 'Inter', sans-serif; background: linear-gradient(135deg, #0f2027, #203a43, #2c5364); min-height: 100vh; display: flex; align-items: center; justify-content: center; margin: 0; }");
        out.println(".glass-card { background: rgba(255, 255, 255, 0.05); backdrop-filter: blur(15px); -webkit-backdrop-filter: blur(15px); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 20px; padding: 3rem; width: 100%; max-width: 450px; box-shadow: 0 25px 45px rgba(0, 0, 0, 0.2); }");
        out.println(".text-gradient { background: linear-gradient(to right, #00c6ff, #0072ff); -webkit-background-clip: text; -webkit-text-fill-color: transparent; font-weight: 700; }");
        out.println(".form-control { background: rgba(255, 255, 255, 0.1); border: 1px solid rgba(255, 255, 255, 0.2); color: #fff; border-radius: 10px; padding: 12px 20px; transition: all 0.3s; }");
        out.println(".form-control::placeholder { color: rgba(255, 255, 255, 0.5); }");
        out.println(".form-control:focus { background: rgba(255, 255, 255, 0.15); border-color: #00c6ff; box-shadow: 0 0 15px rgba(0, 198, 255, 0.3); color: #fff; }");
        out.println(".btn-login { background: linear-gradient(to right, #00c6ff, #0072ff); border: none; border-radius: 10px; padding: 12px; font-weight: 600; letter-spacing: 1px; transition: all 0.3s; color: white; width: 100%; }");
        out.println(".btn-login:hover { transform: translateY(-2px); box-shadow: 0 10px 20px rgba(0, 198, 255, 0.4); }");
        out.println(".custom-link { color: #00c6ff; text-decoration: none; transition: color 0.3s; }");
        out.println(".custom-link:hover { color: #fff; text-decoration: underline; }");
        out.println("</style>");
        out.println("</head><body><div class='glass-card'>");
        out.println("<div class='text-center mb-4'><h2 class='text-gradient mb-2'>Create Account</h2><p class='text-white-50'>Join us to predict house prices.</p></div>");
        if (!alertType.isEmpty())
            out.println("<div class='alert alert-" + alertType + " text-center p-2 mb-4' style='background:rgba(220,53,69,0.2);border:1px solid #dc3545;color:#ff8a94;border-radius:10px;'>" + alertMsg + "</div>");
        out.println("<form action='register' method='post'>");
        out.println("<div class='mb-4'><label class='form-label text-white-50 small text-uppercase fw-bold'>Username</label>");
        out.println("<input type='text' name='username' class='form-control' placeholder='Choose a username' required></div>");
        out.println("<div class='mb-4'><label class='form-label text-white-50 small text-uppercase fw-bold'>Password</label>");
        out.println("<input type='password' name='password' class='form-control' placeholder='Choose a password' required></div>");
        out.println("<div class='mb-4'><label class='form-label text-white-50 small text-uppercase fw-bold'>Confirm Password</label>");
        out.println("<input type='password' name='confirm' class='form-control' placeholder='Re-enter password' required></div>");
        out.println("<div class='d-grid mt-5'><button type='submit' class='btn btn-login'>SIGN UP</button></div>");
        out.println("</form><div class='text-center mt-4'>");
        out.println("<p class='text-white-50 mb-0'>Already have an account? <a href='login.html' class='custom-link fw-bold'>Login here</a></p>");
        out.println("</div></div></body></html>");
    }
}
