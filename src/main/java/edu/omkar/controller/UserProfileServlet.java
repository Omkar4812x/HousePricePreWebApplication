package edu.omkar.controller;

import jakarta.servlet.RequestDispatcher;
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

@WebServlet("/userprofile")
public class UserProfileServlet extends HttpServlet {

    private Connection getConn() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/house_price_prediction_system", "root", "Omkar@2004");
    }

    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userid") == null) {
            response.sendRedirect("login.html"); return false;
        }
        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkSession(request, response)) return;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");

        RequestDispatcher r = request.getRequestDispatcher("userdashboard.html");
        r.include(request, response);

        out.println("<div class='container mt-5'><div class='row justify-content-center'>");
        out.println("<div class='col-md-6'><div class='card shadow border-0'>");
        out.println("<div class='card-header bg-dark text-white text-center'><h4 class='mb-0'>&#128100; My Profile</h4></div>");
        out.println("<div class='card-body p-4'>");
        out.println("<div class='mb-3'><label class='fw-bold'>Current Username:</label>");
        out.println("<p class='form-control bg-light'>" + username + "</p></div><hr>");
        out.println("<h6 class='text-muted mb-3'>Update Password</h6>");
        out.println("<form action='userprofile' method='post'>");
        out.println("<div class='mb-3'><label class='form-label'>New Password</label>");
        out.println("<input type='password' name='newpassword' class='form-control' placeholder='Enter new password' required></div>");
        out.println("<div class='mb-4'><label class='form-label'>Confirm Password</label>");
        out.println("<input type='password' name='confirm' class='form-control' placeholder='Confirm new password' required></div>");
        out.println("<div class='d-grid'><button type='submit' class='btn btn-dark btn-lg'>Update Password</button></div>");
        out.println("</form></div></div></div></div></div>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkSession(request, response)) return;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        int userId   = (int) session.getAttribute("userid");
        String newPwd  = request.getParameter("newpassword");
        String confirm = request.getParameter("confirm");

        RequestDispatcher r = request.getRequestDispatcher("userdashboard.html");
        r.include(request, response);
        out.println("<div class='container mt-5'><div class='row justify-content-center'><div class='col-md-6'>");

        if (!newPwd.equals(confirm)) {
            out.println("<div class='alert alert-danger'>Passwords do not match.</div>");
        } else {
            try (Connection conn = getConn()) {
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE users SET password = ? WHERE userid = ?");
                stmt.setString(1, newPwd);
                stmt.setInt(2, userId);
                if (stmt.executeUpdate() > 0)
                    out.println("<div class='alert alert-success'>&#10003; Password updated successfully!</div>");
                else
                    out.println("<div class='alert alert-danger'>Update failed. Try again.</div>");
            } catch (Exception ex) {
                out.println("<div class='alert alert-danger'>Error: " + ex.getMessage() + "</div>");
            }
        }
        out.println("<a href='userprofile' class='btn btn-dark mt-2'>&#8592; Back to Profile</a>");
        out.println("</div></div></div>");
    }
}
