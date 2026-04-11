package edu.omkar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import edu.omkar.services.CityService;
import edu.omkar.services.CityServiceImpl;

@WebServlet("/deletecity")
public class DeleteCityServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int cityId = Integer.parseInt(request.getParameter("cityid"));
            CityService cityService = new CityServiceImpl();
            boolean b = cityService.deleteCityById(cityId);
            if (b) {
                response.sendRedirect("viewcity");
            } else {
                response.setContentType("text/html");
                response.getWriter().println("<h3>City Not Deleted. Try again.</h3>");
            }
        } catch (Exception ex) {
            response.sendRedirect("viewcity");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
