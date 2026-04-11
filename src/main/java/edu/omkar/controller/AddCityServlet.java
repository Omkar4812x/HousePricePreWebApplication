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
import edu.omkar.model.StateModel;
import edu.omkar.services.CityService;
import edu.omkar.services.CityServiceImpl;
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;

@WebServlet("/addcity")
public class AddCityServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        StateService stateService = new StateServiceImpl();
        List<StateModel> states = stateService.getAllStates();

        out.println("<form name='frm' action='' method='post'>");
        out.println("<div class='container mt-5'><div class='row justify-content-center'>");
        out.println("<div class='col-md-6 col-lg-5'><div class='card shadow-lg border-0'>");
        out.println("<div class='card-body bg-dark text-white p-5'>");
        out.println("<h2 class='text-center mb-4'>Add New City</h2>");

        out.println("<div class='mb-3'><label class='form-label'>Select State</label>");
        out.println("<select name='stateid' class='form-select form-select-lg bg-secondary text-white' required>");
        out.println("<option value=''>-- Select State --</option>");
        for (StateModel sm : states) {
            out.println("<option value='" + sm.getId() + "'>" + sm.getName() + "</option>");
        }
        out.println("</select></div>");

        out.println("<div class='mb-4'><label class='form-label'>City Name</label>");
        out.println("<input type='text' name='name' class='form-control form-control-lg' placeholder='Enter city name' required></div>");

        out.println("<div class='d-grid'><input type='submit' value='Add City' name='s' class='btn btn-primary btn-lg'></div>");
        out.println("</div></div></div></div></div></form>");

        String btn = request.getParameter("s");
        if (btn != null) {
            CityModel cm = new CityModel();
            cm.setName(request.getParameter("name"));
            cm.setStateId(Integer.parseInt(request.getParameter("stateid")));
            CityService cityService = new CityServiceImpl();
            boolean b = cityService.addCity(cm);
            if (b) out.println("<h4 class='text-center text-success mt-3'>City Added Successfully!</h4>");
            else   out.println("<h4 class='text-center text-danger mt-3'>City Not Added. Try again.</h4>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
