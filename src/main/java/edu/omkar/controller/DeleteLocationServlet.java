package edu.omkar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import edu.omkar.services.LocationService;
import edu.omkar.services.LocationServiceImpl;

@WebServlet("/deletelocation")
public class DeleteLocationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int locId = Integer.parseInt(request.getParameter("locid"));
            LocationService locationService = new LocationServiceImpl();
            boolean b = locationService.deleteLocationById(locId);
            if (b) response.sendRedirect("viewlocation");
            else {
                response.setContentType("text/html");
                response.getWriter().println("<h3>Location Not Deleted. Try again.</h3>");
            }
        } catch (Exception ex) {
            response.sendRedirect("viewlocation");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
