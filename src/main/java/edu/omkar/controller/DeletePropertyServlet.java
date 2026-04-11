package edu.omkar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import edu.omkar.services.PropertyService;
import edu.omkar.services.PropertyServiceImpl;

@WebServlet("/deleteproperty")
public class DeletePropertyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            PropertyService propertyService = new PropertyServiceImpl();
            boolean b = propertyService.deletePropertyById(pid);
            if (b) response.sendRedirect("viewproperty");
            else {
                response.setContentType("text/html");
                response.getWriter().println("<h3>Property Not Deleted. Try again.</h3>");
            }
        } catch (Exception ex) {
            response.sendRedirect("viewproperty");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
