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
import edu.omkar.model.PropertyModel;
import edu.omkar.model.StateModel;
import edu.omkar.services.CityService;
import edu.omkar.services.CityServiceImpl;
import edu.omkar.services.LocationService;
import edu.omkar.services.LocationServiceImpl;
import edu.omkar.services.PropertyService;
import edu.omkar.services.PropertyServiceImpl;
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;

@WebServlet("/updproperty")
public class UpdatePropertyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = request.getRequestDispatcher("admindashboard.html");
        rd.include(request, response);

        int pid = Integer.parseInt(request.getParameter("pid"));
        PropertyService propertyService = new PropertyServiceImpl();
        PropertyModel pm = propertyService.getPropertyById(pid);

        if (pm == null) { out.println("<h3 class='text-danger'>Property not found.</h3>"); return; }

        StateService stateService = new StateServiceImpl();
        List<StateModel> states = stateService.getAllStates();
        CityService cityService = new CityServiceImpl();
        List<CityModel> cities = cityService.getCitiesByState(pm.getStateId());
        LocationService locationService = new LocationServiceImpl();
        List<LocationModel> locations = locationService.getLocationsByCity(pm.getCityId());

        out.println("<div class='container mt-4'><div class='row justify-content-center'>");
        out.println("<div class='col-md-8'><div class='card shadow-lg border-0'>");
        out.println("<div class='card-body bg-dark text-white p-5'>");
        out.println("<h2 class='text-center mb-4'>Update House Property</h2>");
        out.println("<form name='frm' action='' method='post'>");
        out.println("<input type='hidden' name='pid' value='" + pm.getId() + "'>");

        // State -> City -> Location cascaded
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label'>State</label>");
        out.println("<select id='stateSelect' name='stateid' class='form-select bg-secondary text-white' required onchange='loadCities(this.value)'>");
        for (StateModel sm : states) {
            String sel = (sm.getId() == pm.getStateId()) ? "selected" : "";
            out.println("<option value='" + sm.getId() + "' " + sel + ">" + sm.getName() + "</option>");
        }
        out.println("</select></div>");

        out.println("<div class='col-md-4'><label class='form-label'>City</label>");
        out.println("<select id='citySelect' name='ctid' class='form-select bg-secondary text-white' required onchange='loadLocations(this.value)'>");
        out.println("<option value=''>-- Select City --</option>");
        for (CityModel cm : cities) {
            String sel = (cm.getId() == pm.getCityId()) ? "selected" : "";
            out.println("<option value='" + cm.getId() + "' " + sel + ">" + cm.getName() + "</option>");
        }
        out.println("</select></div>");

        out.println("<div class='col-md-4'><label class='form-label'>Location</label>");
        out.println("<select id='locSelect' name='locid' class='form-select bg-secondary text-white' required>");
        out.println("<option value=''>-- Select Location --</option>");
        for (LocationModel lm : locations) {
            String sel = (lm.getId() == pm.getLocId()) ? "selected" : "";
            out.println("<option value='" + lm.getId() + "' " + sel + ">" + lm.getName() + "</option>");
        }
        out.println("</select></div></div>");

        // Name + Address
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-6'><label class='form-label'>Property Name</label>");
        out.println("<input type='text' name='pname' value='" + pm.getName() + "' class='form-control' required></div>");
        out.println("<div class='col-md-6'><label class='form-label'>Address</label>");
        out.println("<input type='text' name='paddress' value='" + pm.getAddress() + "' class='form-control' required></div></div>");

        // Area, Bed, Bath
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label'>Area (sq ft)</label>");
        out.println("<input type='number' name='asqfeet' value='" + pm.getSqFeet() + "' class='form-control' min='1' required></div>");
        out.println("<div class='col-md-4'><label class='form-label'>Bedrooms</label>");
        out.println("<input type='number' name='nbed' value='" + pm.getNbed() + "' class='form-control' min='1' required></div>");
        out.println("<div class='col-md-4'><label class='form-label'>Bathrooms</label>");
        out.println("<input type='number' name='nbath' value='" + pm.getNbath() + "' class='form-control' min='1' required></div></div>");

        // Age + Price
        out.println("<div class='row mb-4'>");
        out.println("<div class='col-md-6'><label class='form-label'>Age of House (years)</label>");
        out.println("<input type='number' name='age' value='" + pm.getAge() + "' class='form-control' min='1' required></div>");
        out.println("<div class='col-md-6'><label class='form-label'>Actual Price (&#8377;)</label>");
        out.println("<input type='number' name='actualprice' value='" + pm.getActualPrice() + "' class='form-control' min='0' step='0.01' required></div></div>");

        out.println("<div class='d-grid'><input type='submit' value='Update Property' name='s' class='btn btn-warning btn-lg'></div>");
        out.println("</form></div></div></div></div>");

        // AJAX JS
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

        String btn = request.getParameter("s");
        if (btn != null) {
            try {
                PropertyModel upd = new PropertyModel();
                upd.setId(Integer.parseInt(request.getParameter("pid")));
                upd.setName(request.getParameter("pname"));
                upd.setAddress(request.getParameter("paddress"));
                upd.setAge(Integer.parseInt(request.getParameter("age")));
                upd.setSqFeet(Integer.parseInt(request.getParameter("asqfeet")));
                upd.setNbed(Integer.parseInt(request.getParameter("nbed")));
                upd.setNbath(Integer.parseInt(request.getParameter("nbath")));
                upd.setActualPrice(Double.parseDouble(request.getParameter("actualprice")));
                upd.setLocId(Integer.parseInt(request.getParameter("locid")));
                boolean b = propertyService.updateProperty(upd);
                if (b) response.sendRedirect("viewproperty");
                else   out.println("<h4 class='text-danger mt-3'>Property Not Updated. Try again.</h4>");
            } catch (Exception ex) {
                out.println("<h4 class='text-danger mt-3'>Error: " + ex.getMessage() + "</h4>");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
