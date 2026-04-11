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
import edu.omkar.services.PropertyService;
import edu.omkar.services.PropertyServiceImpl;

@WebServlet("/viewproperty")
public class ViewAllPropertyServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        PropertyService propertyService = new PropertyServiceImpl();
        List<PropertyModel> list = propertyService.getAllProperties();

        out.println("<div class='container-fluid mt-4 px-4'>");

        // Search bar with id and onkeyup
        out.println("<div class='mb-3'>");
        out.println("<input type='text' id='searchInput' onkeyup='filterTable()' "
                  + "class='form-control bg-white text-dark shadow-sm' placeholder='&#128269; Search properties...'>");
        out.println("</div>");

        out.println("<div class='card shadow-lg border-0 bg-dark text-white'>");
        out.println("<div class='card-header bg-dark text-white text-center border-bottom'>");
        out.println("<h4 class='mb-0'>Property / House Data List</h4></div>");
        out.println("<div class='card-body p-0' style='overflow-x:auto;'>");
        out.println("<table class='table table-dark table-hover text-center align-middle mb-0' style='min-width:1100px;'>");
        out.println("<thead class='border-bottom'><tr>");
        out.println("<th>SR</th><th>Name</th><th>Address</th><th>State</th><th>City</th><th>Location</th>");
        out.println("<th>Area(sqft)</th><th>Bed</th><th>Bath</th><th>Age</th><th>Price(&#8377;)</th>");
        out.println("<th>DELETE</th><th>UPDATE</th></tr></thead>");

        // tbody with id for JS filter
        out.println("<tbody id='tableBody'>");
        int count = 0;
        for (PropertyModel pm : list) {
            count++;
            out.println("<tr style='border-bottom:1px solid #555;'>");
            out.println("<td>" + count + "</td>");
            out.println("<td>" + pm.getName() + "</td>");
            out.println("<td>" + pm.getAddress() + "</td>");
            out.println("<td>" + pm.getStateName() + "</td>");
            out.println("<td>" + pm.getCityName() + "</td>");
            out.println("<td>" + pm.getLocName() + "</td>");
            out.println("<td>" + pm.getSqFeet() + "</td>");
            out.println("<td>" + pm.getNbed() + "</td>");
            out.println("<td>" + pm.getNbath() + "</td>");
            out.println("<td>" + pm.getAge() + "</td>");
            out.println("<td>" + String.format("%.2f", pm.getActualPrice()) + "</td>");
            out.println("<td><a href='deleteproperty?pid=" + pm.getId()
                + "' class='text-danger text-decoration-none fw-bold'"
                + " onclick='return confirm(\"Are you sure?\")'>DELETE</a></td>");
            out.println("<td><a href='updproperty?pid=" + pm.getId()
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
