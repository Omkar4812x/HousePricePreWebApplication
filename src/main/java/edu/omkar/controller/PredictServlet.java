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
import edu.omkar.model.ModelParam;
import edu.omkar.model.StateModel;
import edu.omkar.services.ModelService;
import edu.omkar.services.ModelServiceImpl;
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;
import edu.omkar.util.LinearRegressionTrainer;

@WebServlet("/predict")
public class PredictServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("userdashboard.html");
        r.include(request, response);

        StateService stateService = new StateServiceImpl();
        List<StateModel> states = stateService.getAllStates();

        ModelService modelService = new ModelServiceImpl();
        ModelParam model = modelService.getLatestModel();

        out.println("<div class='container mt-5'>");
        out.println("<div class='row justify-content-center'>");
        out.println("<div class='col-md-8'>");
        out.println("<div class='card shadow-lg border-0'>");
        out.println("<div class='card-body bg-dark text-white p-5'>");
        out.println("<h2 class='text-center mb-4'>&#127968; House Price Prediction</h2>");

        if (model == null) {
            out.println("<div class='alert alert-warning text-center'>Model not trained yet. Please contact admin.</div>");
            out.println("</div></div></div></div></div>");
            return;
        }

        out.println("<form name='predictForm' action='predict' method='post'>");

        // Row 1: State -> City -> Location (cascaded AJAX)
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label'>State</label>");
        out.println("<select id='stateSelect' name='stateid' class='form-select bg-secondary text-white' required onchange='loadCities(this.value)'>");
        out.println("<option value=''>-- Select State --</option>");
        for (StateModel sm : states)
            out.println("<option value='" + sm.getId() + "'>" + sm.getName() + "</option>");
        out.println("</select></div>");

        out.println("<div class='col-md-4'><label class='form-label'>City</label>");
        out.println("<select id='citySelect' name='ctid' class='form-select bg-secondary text-white' required onchange='loadLocations(this.value)'>");
        out.println("<option value=''>-- Select City --</option>");
        out.println("</select></div>");

        out.println("<div class='col-md-4'><label class='form-label'>Location</label>");
        out.println("<select id='locSelect' name='locid' class='form-select bg-secondary text-white' required>");
        out.println("<option value=''>-- Select Location --</option>");
        out.println("</select></div></div>");

        // Row 2: Area + Bedrooms + Bathrooms
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label'>Area (sq ft)</label>");
        out.println("<input type='number' name='asqfeet' class='form-control' placeholder='e.g. 1200' min='1' required></div>");
        out.println("<div class='col-md-4'><label class='form-label'>Bedrooms</label>");
        out.println("<input type='number' name='nbed' class='form-control' value='2' min='1' required></div>");
        out.println("<div class='col-md-4'><label class='form-label'>Bathrooms</label>");
        out.println("<input type='number' name='nbath' class='form-control' value='1' min='1' required></div></div>");

        // Row 3: Age
        out.println("<div class='row mb-4'>");
        out.println("<div class='col-md-6'><label class='form-label'>Age of House (years)</label>");
        out.println("<input type='number' name='age' class='form-control' placeholder='e.g. 5' min='0' required></div></div>");

        out.println("<div class='d-grid'><button type='submit' class='btn btn-success btn-lg'>&#128200; Predict Price</button></div>");
        out.println("</form></div></div></div></div></div>");

        // AJAX for cascaded dropdowns
        out.println("<script>");
        out.println("function loadCities(stateId){");
        out.println("  var cs=document.getElementById('citySelect');");
        out.println("  var ls=document.getElementById('locSelect');");
        out.println("  cs.innerHTML='<option value=\"\">-- Select City --</option>';");
        out.println("  ls.innerHTML='<option value=\"\">-- Select Location --</option>';");
        out.println("  if(!stateId) return;");
        out.println("  fetch('getcitiesbystate?stateid='+stateId).then(r=>r.json()).then(data=>{");
        out.println("    data.forEach(c=>{ cs.innerHTML+='<option value=\"'+c.id+'\">'+c.name+'</option>'; });");
        out.println("  });");
        out.println("}");
        out.println("function loadLocations(ctid){");
        out.println("  var ls=document.getElementById('locSelect');");
        out.println("  ls.innerHTML='<option value=\"\">-- Select Location --</option>';");
        out.println("  if(!ctid) return;");
        out.println("  fetch('getlocationsbycity?ctid='+ctid).then(r=>r.json()).then(data=>{");
        out.println("    data.forEach(l=>{ ls.innerHTML+='<option value=\"'+l.id+'\">'+l.name+'</option>'; });");
        out.println("  });");
        out.println("}");
        out.println("</script>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("userdashboard.html");
        r.include(request, response);

        out.println("<div class='container mt-5'>");
        out.println("<div class='row justify-content-center'>");
        out.println("<div class='col-md-6'>");

        try {
            double sqFeet = Double.parseDouble(request.getParameter("asqfeet"));
            int nbed      = Integer.parseInt(request.getParameter("nbed"));
            int nbath     = Integer.parseInt(request.getParameter("nbath"));
            int age       = Integer.parseInt(request.getParameter("age"));

            ModelService modelService = new ModelServiceImpl();
            ModelParam model = modelService.getLatestModel();

            if (model == null) {
                out.println("<div class='alert alert-warning'>Model not available. Contact admin.</div>");
            } else {
                double[] coeffs = {
                    model.getIntercept(),
                    model.getSlopeSqFeet(),
                    model.getSlopeNbed(),
                    model.getSlopeNbath(),
                    model.getSlopeAge()
                };
                double predictedPrice = LinearRegressionTrainer.predict(coeffs, sqFeet, nbed, nbath, age);
                if (predictedPrice < 0) predictedPrice = 0;

                out.println("<div class='card shadow-lg border-0 bg-dark text-white text-center'>");
                out.println("<div class='card-body p-5'>");
                out.println("<div class='mb-3'><span style='font-size:4rem;'>&#127968;</span></div>");
                out.println("<h4 class='text-muted mb-2'>Estimated House Price</h4>");
                out.println("<h1 class='text-success mb-1'>&#8377; " + String.format("%,.2f", predictedPrice) + "</h1>");
                out.println("<p class='text-muted mt-3'>");
                out.println(String.format("Area: <b>%s sqft</b> | Beds: <b>%d</b> | Baths: <b>%d</b> | Age: <b>%d yrs</b>",
                    (int)sqFeet, nbed, nbath, age));
                out.println("</p>");
                out.println("<hr class='border-secondary'>");
                out.println("<a href='predict' class='btn btn-outline-light mt-2'>&#8592; Predict Another</a>");
                out.println("</div></div>");
            }
        } catch (Exception ex) {
            out.println("<div class='alert alert-danger'>Error: Please fill all fields correctly.</div>");
            out.println("<a href='predict' class='btn btn-primary'>Try Again</a>");
        }
        out.println("</div></div></div>");
    }
}
