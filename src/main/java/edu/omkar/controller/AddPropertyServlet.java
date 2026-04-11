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
import edu.omkar.model.PropertyModel;
import edu.omkar.model.StateModel;
import edu.omkar.services.PropertyService;
import edu.omkar.services.PropertyServiceImpl;
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;

@WebServlet("/addproperty")
public class AddPropertyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        StateService stateService = new StateServiceImpl();
        List<StateModel> states = stateService.getAllStates();

        out.println("<div class='container mt-4'><div class='row justify-content-center'>");
        out.println("<div class='col-md-8'><div class='card shadow-lg border-0'>");
        out.println("<div class='card-body bg-dark text-white p-5'>");
        out.println("<h2 class='text-center mb-4'>Add House Property</h2>");
        out.println("<form name='frm' action='' method='post'>");

        // Row 1: State -> City -> Location (cascaded)
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label'>State</label>");
        out.println("<select id='stateSelect' name='stateid' class='form-select bg-secondary text-white' required onchange='loadCities(this.value)'>");
        out.println("<option value=''>-- Select State --</option>");
        for (StateModel sm : states) {
            out.println("<option value='" + sm.getId() + "'>" + sm.getName() + "</option>");
        }
        out.println("</select></div>");

        out.println("<div class='col-md-4'><label class='form-label'>City</label>");
        out.println("<select id='citySelect' name='ctid' class='form-select bg-secondary text-white' required onchange='loadLocations(this.value)'>");
        out.println("<option value=''>-- Select City --</option>");
        out.println("</select></div>");

        out.println("<div class='col-md-4'><label class='form-label'>Location</label>");
        out.println("<select id='locSelect' name='locid' class='form-select bg-secondary text-white' required>");
        out.println("<option value=''>-- Select Location --</option>");
        out.println("</select></div></div>");

        // Row 2: Property Name + Address
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-6'><label class='form-label'>Property Name</label>");
        out.println("<input type='text' name='pname' class='form-control' placeholder='Property name' required></div>");
        out.println("<div class='col-md-6'><label class='form-label'>Address</label>");
        out.println("<input type='text' name='paddress' class='form-control' placeholder='Full address' required></div></div>");

        // Row 3: Area, Bedrooms, Bathrooms
        out.println("<div class='row mb-3'>");
        out.println("<div class='col-md-4'><label class='form-label'>Area (sq ft)</label>");
        out.println("<input type='number' name='asqfeet' class='form-control' placeholder='e.g. 1200' min='1' required></div>");
        out.println("<div class='col-md-4'><label class='form-label'>Bedrooms</label>");
        out.println("<input type='number' name='nbed' class='form-control' value='1' min='1' required></div>");
        out.println("<div class='col-md-4'><label class='form-label'>Bathrooms</label>");
        out.println("<input type='number' name='nbath' class='form-control' value='1' min='1' required></div></div>");

        // Row 4: Age + Price
        out.println("<div class='row mb-4'>");
        out.println("<div class='col-md-6'><label class='form-label'>Age of House (years)</label>");
        out.println("<input type='number' name='age' class='form-control' placeholder='e.g. 5' min='1' required></div>");
        out.println("<div class='col-md-6'><label class='form-label'>Actual Price (&#8377;)</label>");
        out.println("<input type='number' name='actualprice' class='form-control' placeholder='e.g. 5000000' min='0' step='0.01' required></div></div>");

        out.println("<div class='d-grid'><input type='submit' value='Add Property' name='s' class='btn btn-primary btn-lg'></div>");
        out.println("</form></div></div></div></div>");

        // AJAX JS for cascaded dropdowns
        out.println("<script>");
        out.println("function loadCities(stateId){");
        out.println("  var cs=document.getElementById('citySelect');");
        out.println("  var ls=document.getElementById('locSelect');");
        out.println("  cs.innerHTML='<option value=\"\">-- Select City --</option>';");
        out.println("  ls.innerHTML='<option value=\"\">-- Select Location --</option>';");
        out.println("  if(!stateId) return;");
        out.println("  fetch('getcitiesbystate?stateid='+stateId)");
        out.println("    .then(r=>r.json()).then(data=>{");
        out.println("      data.forEach(c=>{ cs.innerHTML+='<option value=\"'+c.id+'\">'+c.name+'</option>'; });");
        out.println("    });");
        out.println("}");
        out.println("function loadLocations(ctid){");
        out.println("  var ls=document.getElementById('locSelect');");
        out.println("  ls.innerHTML='<option value=\"\">-- Select Location --</option>';");
        out.println("  if(!ctid) return;");
        out.println("  fetch('getlocationsbycity?ctid='+ctid)");
        out.println("    .then(r=>r.json()).then(data=>{");
        out.println("      data.forEach(l=>{ ls.innerHTML+='<option value=\"'+l.id+'\">'+l.name+'</option>'; });");
        out.println("    });");
        out.println("}");
        out.println("</script>");

        String btn = request.getParameter("s");
        if (btn != null) {
            try {
                PropertyModel pm = new PropertyModel();
                pm.setName(request.getParameter("pname"));
                pm.setAddress(request.getParameter("paddress"));
                pm.setAge(Integer.parseInt(request.getParameter("age")));
                pm.setSqFeet(Integer.parseInt(request.getParameter("asqfeet")));
                pm.setNbed(Integer.parseInt(request.getParameter("nbed")));
                pm.setNbath(Integer.parseInt(request.getParameter("nbath")));
                pm.setActualPrice(Double.parseDouble(request.getParameter("actualprice")));
                pm.setLocId(Integer.parseInt(request.getParameter("locid")));
                PropertyService propertyService = new PropertyServiceImpl();
                boolean b = propertyService.addProperty(pm);
                if (b) out.println("<h4 class='text-center text-success mt-3'>Property Added Successfully!</h4>");
                else   out.println("<h4 class='text-center text-danger mt-3'>Property Not Added. Try again.</h4>");
            } catch (Exception ex) {
                out.println("<h4 class='text-center text-danger mt-3'>Error: " + ex.getMessage() + "</h4>");
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
