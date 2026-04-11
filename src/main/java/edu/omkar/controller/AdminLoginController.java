package edu.omkar.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import edu.omkar.model.AdminLoginModel;
import edu.omkar.services.ValidateAdminService;
import edu.omkar.services.ValidateAdminServiceImpl;

@WebServlet("/validateadmin")
public class AdminLoginController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        AdminLoginModel model = new AdminLoginModel();
        model.setUsername(username);
        model.setPassword(password);

        ValidateAdminService svc = new ValidateAdminServiceImpl();
        Optional<AdminLoginModel> o = svc.validateAdmin(model);

        if (o.isPresent()) {
            AdminLoginModel logged = o.get();

            // Store user info in session
            HttpSession session = request.getSession();
            session.setAttribute("userid",   logged.getUserId());
            session.setAttribute("username", logged.getUsername());
            session.setAttribute("usertype", logged.getUsertype());

            if ("admin".equalsIgnoreCase(logged.getUsertype())) {
                // ADMIN → Admin Dashboard
                RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
                r.forward(request, response);
                return;
            } else {
                // USER → User Home
                response.sendRedirect("userhome");
                return;
            }
        } else {
            // Invalid credentials -> redirect to login with error
            response.sendRedirect("login.html?error=1");
            return;
        }
    }
}
