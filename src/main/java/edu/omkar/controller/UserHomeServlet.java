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
import java.util.List;
import edu.omkar.model.ModelParam;
import edu.omkar.model.PropertyModel;
import edu.omkar.model.SearchHistoryModel;
import edu.omkar.model.StateModel;
import edu.omkar.services.ModelService;
import edu.omkar.services.ModelServiceImpl;
import edu.omkar.services.PropertyService;
import edu.omkar.services.PropertyServiceImpl;
import edu.omkar.services.SearchHistoryServiceImpl;
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;
import edu.omkar.util.LinearRegressionTrainer;

@WebServlet("/userhome")
public class UserHomeServlet extends HttpServlet {

    /** Check session — redirect to login if not a user */
    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userid") == null) {
            response.sendRedirect("login.html");
            return false;
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

        StateService stateService = new StateServiceImpl();
        List<StateModel> states = stateService.getAllStates();

        out.println("<div class='container mt-4'>");
        out.println("<h4 class='mb-4 text-dark'>Welcome, <strong>" + username + "</strong> &#128075;</h4>");

        // ── Search & Predict Form ────────────────────────────────────────────
        out.println("<div class='card shadow border-0 mb-4'>");
        out.println("<div class='card-header bg-dark text-white'><h5 class='mb-0'>&#128269; Search Properties &amp; Predict Price</h5></div>");
        out.println("<div class='card-body bg-light'>");
        out.println("<form action='userhome' method='post'>");

        // Row 1: State → City → Location
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label fw-bold'>State</label>");
        out.println("<select id='stateSelect' name='stateid' class='form-select' onchange='loadCities(this.value)'>");
        out.println("<option value=''>-- Select State --</option>");
        for (StateModel sm : states)
            out.println("<option value='" + sm.getId() + "' data-name='" + sm.getName() + "'>" + sm.getName() + "</option>");
        out.println("</select></div>");
        out.println("<div class='col-md-4'><label class='form-label fw-bold'>City</label>");
        out.println("<select id='citySelect' name='ctid' class='form-select' onchange='loadLocations(this.value)'>");
        out.println("<option value=''>-- Select City --</option></select></div>");
        out.println("<div class='col-md-4'><label class='form-label fw-bold'>Location</label>");
        out.println("<select id='locSelect' name='locid' class='form-select'>");
        out.println("<option value=''>-- Select Location --</option></select></div></div>");

        // Row 2: Prediction details (optional)
        out.println("<p class='text-muted small mb-2'>&#128200; <em>Optional: Fill below for price prediction</em></p>");
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-3'><label class='form-label'>Area (sq ft)</label>");
        out.println("<input type='number' name='asqfeet' class='form-control' placeholder='e.g. 1200' min='1'></div>");
        out.println("<div class='col-md-3'><label class='form-label'>Bedrooms</label>");
        out.println("<input type='number' name='nbed' class='form-control' placeholder='e.g. 3' min='1'></div>");
        out.println("<div class='col-md-3'><label class='form-label'>Bathrooms</label>");
        out.println("<input type='number' name='nbath' class='form-control' placeholder='e.g. 2' min='1'></div>");
        out.println("<div class='col-md-3'><label class='form-label'>Age (years)</label>");
        out.println("<input type='number' name='age' class='form-control' placeholder='e.g. 5' min='0'></div></div>");

        out.println("<div class='d-grid'><button type='submit' class='btn btn-dark btn-lg'>&#128269; Search &amp; Predict</button></div>");
        out.println("</form></div></div>");
        out.println("</div>");

        // AJAX for cascaded dropdowns
        out.println("<script>");
        out.println("function loadCities(sid){");
        out.println("  var cs=document.getElementById('citySelect');");
        out.println("  var ls=document.getElementById('locSelect');");
        out.println("  cs.innerHTML='<option value=\"\">-- Select City --</option>';");
        out.println("  ls.innerHTML='<option value=\"\">-- Select Location --</option>';");
        out.println("  if(!sid) return;");
        out.println("  fetch('getcitiesbystate?stateid='+sid).then(r=>r.json()).then(d=>{");
        out.println("    d.forEach(c=>{ cs.innerHTML+='<option value=\"'+c.id+'\" data-name=\"'+c.name+'\">'+c.name+'</option>'; });");
        out.println("  });");
        out.println("}");
        out.println("function loadLocations(cid){");
        out.println("  var ls=document.getElementById('locSelect');");
        out.println("  ls.innerHTML='<option value=\"\">-- Select Location --</option>';");
        out.println("  if(!cid) return;");
        out.println("  fetch('getlocationsbycity?ctid='+cid).then(r=>r.json()).then(d=>{");
        out.println("    d.forEach(l=>{ ls.innerHTML+='<option value=\"'+l.id+'\" data-name=\"'+l.name+'\">'+l.name+'</option>'; });");
        out.println("  });");
        out.println("}");
        out.println("</script>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkSession(request, response)) return;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute("userid");

        RequestDispatcher r = request.getRequestDispatcher("userdashboard.html");
        r.include(request, response);

        String locidStr = request.getParameter("locid");
        int locId = 0;
        try { locId = Integer.parseInt(locidStr); } catch (Exception ignored) {}

        // ── Show matching properties ─────────────────────────────────────────
        PropertyService propService = new PropertyServiceImpl();
        List<PropertyModel> allProps = propService.getAllProperties();
        List<PropertyModel> filtered = new java.util.ArrayList<>();
        for (PropertyModel pm : allProps)
            if (locId == 0 || pm.getLocId() == locId) filtered.add(pm);

        out.println("<div class='container mt-4'>");

        // Properties table
        if (!filtered.isEmpty()) {
            out.println("<div class='card shadow border-0 mb-4'>");
            out.println("<div class='card-header bg-dark text-white'><h5 class='mb-0'>&#127968; Properties Found (" + filtered.size() + ")</h5></div>");
            out.println("<div class='card-body p-0' style='overflow-x:auto;'>");
            out.println("<table class='table table-hover text-center align-middle mb-0'>");
            out.println("<thead class='table-dark'><tr>");
            out.println("<th>Name</th><th>Address</th><th>State</th><th>City</th><th>Location</th>");
            out.println("<th>Area(sqft)</th><th>Bed</th><th>Bath</th><th>Age</th><th>Actual Price(&#8377;)</th></tr></thead><tbody>");
            for (PropertyModel pm : filtered) {
                out.println("<tr><td>" + pm.getName() + "</td><td>" + pm.getAddress() + "</td>");
                out.println("<td>" + pm.getStateName() + "</td><td>" + pm.getCityName() + "</td><td>" + pm.getLocName() + "</td>");
                out.println("<td>" + pm.getSqFeet() + "</td><td>" + pm.getNbed() + "</td><td>" + pm.getNbath() + "</td>");
                out.println("<td>" + pm.getAge() + "</td><td>&#8377;" + String.format("%,.2f", pm.getActualPrice()) + "</td></tr>");
            }
            out.println("</tbody></table></div></div>");
        } else {
            out.println("<div class='alert alert-info'>No properties found for the selected location.</div>");
        }

        // ── Prediction ───────────────────────────────────────────────────────
        String sqFeetStr = request.getParameter("asqfeet");
        if (sqFeetStr != null && !sqFeetStr.trim().isEmpty()) {
            try {
                double sqFeet = Double.parseDouble(sqFeetStr);
                int nbed  = Integer.parseInt(request.getParameter("nbed"));
                int nbath = Integer.parseInt(request.getParameter("nbath"));
                int age   = Integer.parseInt(request.getParameter("age"));

                // Need location name for history
                String stateName = request.getParameter("stateid") != null ? getNameFromSelect(request,"stateid") : "N/A";
                String cityName  = "N/A";
                String locName   = "N/A";
                // Try to get names from property list
                if (!filtered.isEmpty()) {
                    stateName = filtered.get(0).getStateName();
                    cityName  = filtered.get(0).getCityName();
                    locName   = filtered.get(0).getLocName();
                }

                ModelService modelService = new ModelServiceImpl();
                ModelParam model = modelService.getLatestModel();

                out.println("<div class='card shadow border-0 mb-4'>");
                out.println("<div class='card-header bg-success text-white'><h5 class='mb-0'>&#128200; Prediction Result</h5></div>");
                out.println("<div class='card-body text-center py-4'>");

                if (model == null) {
                    out.println("<div class='alert alert-warning'>Model not trained yet. Please contact admin.</div>");
                } else {
                    double[] coeffs = {model.getIntercept(), model.getSlopeSqFeet(),
                                       model.getSlopeNbed(), model.getSlopeNbath(), model.getSlopeAge()};
                    double price = LinearRegressionTrainer.predict(coeffs, sqFeet, nbed, nbath, age);
                    if (price < 0) price = 0;

                    out.println("<h5 class='text-muted'>Estimated House Price</h5>");
                    out.println("<h1 class='text-success'>&#8377; " + String.format("%,.2f", price) + "</h1>");
                    out.println("<p class='text-muted'>Area: <b>" + (int)sqFeet + " sqft</b> | Beds: <b>" + nbed +
                                "</b> | Baths: <b>" + nbath + "</b> | Age: <b>" + age + " yrs</b></p>");

                    // Save to search history
                    SearchHistoryModel hist = new SearchHistoryModel();
                    hist.setUserId(userId);
                    hist.setStateName(stateName);
                    hist.setCityName(cityName);
                    hist.setLocName(locName);
                    hist.setSqFeet((int)sqFeet);
                    hist.setNbed(nbed);
                    hist.setNbath(nbath);
                    hist.setAge(age);
                    hist.setPredictedPrice(price);
                    new SearchHistoryServiceImpl().saveHistory(hist);
                    out.println("<small class='text-muted'>Search saved to your history.</small>");
                }
                out.println("</div></div>");
            } catch (Exception ex) {
                out.println("<div class='alert alert-danger'>Prediction error: " + ex.getMessage() + "</div>");
            }
        }

        out.println("<a href='userhome' class='btn btn-outline-dark mb-4'>&#8592; New Search</a>");
        out.println("</div>");
    }

    private String getNameFromSelect(HttpServletRequest req, String param) {
        String v = req.getParameter(param);
        return v != null ? v : "N/A";
    }
}
