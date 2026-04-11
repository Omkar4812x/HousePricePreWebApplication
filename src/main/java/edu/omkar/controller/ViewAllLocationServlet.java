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
import edu.omkar.model.LocationModel;
import edu.omkar.services.LocationService;
import edu.omkar.services.LocationServiceImpl;

@WebServlet("/viewlocation")
public class ViewAllLocationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        LocationService locationService = new LocationServiceImpl();
        List<LocationModel> list = locationService.getAllLocations();

        out.println("<div class='container mt-5'>");

        // Search bar with id and onkeyup
        out.println("<div class='mb-3'>");
        out.println("<input type='text' id='searchInput' onkeyup='filterTable()' "
                  + "class='form-control bg-white text-dark shadow-sm' placeholder='&#128269; Search locations...'>");
        out.println("</div>");

        out.println("<div class='card shadow-lg border-0 bg-dark text-white'>");
        out.println("<div class='card-header bg-dark text-white text-center border-bottom'>");
        out.println("<h4 class='mb-0'>Location List</h4></div>");
        out.println("<div class='card-body p-0'>");
        out.println("<table class='table table-dark table-hover text-center align-middle mb-0'>");
        out.println("<thead class='border-bottom'><tr>");
        out.println("<th class='py-3'>SR NO</th><th class='py-3'>STATE</th><th class='py-3'>CITY</th>");
        out.println("<th class='py-3'>LOCATION</th><th class='py-3'>DELETE</th><th class='py-3'>UPDATE</th>");
        out.println("</tr></thead>");

        // tbody with id for JS filter
        out.println("<tbody id='tableBody'>");
        int count = 0;
        for (LocationModel lm : list) {
            count++;
            out.println("<tr style='border-bottom:1px solid #555;'>");
            out.println("<td>" + count + "</td>");
            out.println("<td>" + lm.getStateName() + "</td>");
            out.println("<td>" + lm.getCityName() + "</td>");
            out.println("<td>" + lm.getName() + "</td>");
            out.println("<td><a href='deletelocation?locid=" + lm.getId()
                + "' class='text-danger text-decoration-none fw-bold'"
                + " onclick='return confirm(\"Are you sure?\")'>DELETE</a></td>");
            out.println("<td><a href='updlocation?locid=" + lm.getId()
                + "&locname=" + lm.getName() + "&ctid=" + lm.getCityId()
                + "' class='text-primary text-decoration-none fw-bold'>UPDATE</a></td>");
            out.println("</tr>");
        }
        out.println("</tbody></table></div></div></div>");

        // Real-time search JS
        out.println("<script>");
        out.println("function filterTable(){");
        out.println("  var input = document.getElementById('searchInput').value.toLowerCase();");
        out.println("  var rows = document.getElementById('tableBody').getElementsByTagName('tr');");
        out.println("  for(var i=0;i<rows.length;i++){");
        out.println("    var text = rows[i].textContent.toLowerCase();");
        out.println("    rows[i].style.display = text.includes(input) ? '' : 'none';");
        out.println("  }");
        out.println("}");
        out.println("</script>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
