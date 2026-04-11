package edu.omkar.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import edu.omkar.model.CityModel;
import edu.omkar.model.LocationModel;
import edu.omkar.services.CityService;
import edu.omkar.services.CityServiceImpl;
import edu.omkar.services.LocationService;
import edu.omkar.services.LocationServiceImpl;

@WebServlet("/addlocation")
public class AddLocationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        CityService cityService = new CityServiceImpl();
        List<CityModel> cities = cityService.getAllCities();

        out.println("<form name='frm' action='' method='post'>");
        out.println("<div class='container mt-5'><div class='row justify-content-center'>");
        out.println("<div class='col-md-6 col-lg-5'><div class='card shadow-lg border-0'>");
        out.println("<div class='card-body bg-dark text-white p-5'>");
        out.println("<h2 class='text-center mb-4'>Add New Location</h2>");

        out.println("<div class='mb-3'><label class='form-label'>Select City</label>");
        out.println("<select name='ctid' class='form-select form-select-lg bg-secondary text-white' required>");
        out.println("<option value=''>-- Select City --</option>");
        for (CityModel cm : cities) {
            out.println("<option value='" + cm.getId() + "'>" + cm.getName() + " (" + cm.getStateName() + ")</option>");
        }
        out.println("</select></div>");

        out.println("<div class='mb-4'><label class='form-label'>Location Name</label>");
        out.println("<input type='text' name='name' class='form-control form-control-lg' placeholder='Enter location name' required></div>");

        out.println("<div class='d-grid'><input type='submit' value='Add Location' name='s' class='btn btn-primary btn-lg'></div>");
        out.println("</div></div></div></div></div></form>");

        String btn = request.getParameter("s");
        if (btn != null) {
            LocationModel lm = new LocationModel();
            lm.setName(request.getParameter("name"));
            lm.setCityId(Integer.parseInt(request.getParameter("ctid")));
            LocationService locationService = new LocationServiceImpl();
            boolean b = locationService.addLocation(lm);
            if (b) out.println("<h4 class='text-center text-success mt-3'>Location Added Successfully!</h4>");
            else   out.println("<h4 class='text-center text-danger mt-3'>Location Not Added. Try again.</h4>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
