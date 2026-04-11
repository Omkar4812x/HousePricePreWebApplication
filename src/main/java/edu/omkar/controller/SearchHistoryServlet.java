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
import edu.omkar.model.SearchHistoryModel;
import edu.omkar.services.SearchHistoryService;
import edu.omkar.services.SearchHistoryServiceImpl;

@WebServlet("/searchhistory")
public class SearchHistoryServlet extends HttpServlet {

    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userid") == null) {
            response.sendRedirect("login.html"); return false;
        }
        return true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkSession(request, response)) return;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        int userId = (int) session.getAttribute("userid");

        RequestDispatcher r = request.getRequestDispatcher("userdashboard.html");
        r.include(request, response);

        SearchHistoryService svc = new SearchHistoryServiceImpl();
        List<SearchHistoryModel> list = svc.getHistoryByUser(userId);

        out.println("<div class='container mt-5'>");
        out.println("<div class='mb-3'><input type='text' id='searchInput' onkeyup='filterTable()' "
                  + "class='form-control shadow-sm' placeholder='&#128269; Search history...'></div>");
        out.println("<div class='card shadow border-0'>");
        out.println("<div class='card-header bg-dark text-white text-center'><h4 class='mb-0'>&#128203; My Search History</h4></div>");
        out.println("<div class='card-body p-0' style='overflow-x:auto;'>");
        out.println("<table class='table table-hover text-center align-middle mb-0'>");
        out.println("<thead class='table-dark'><tr>");
        out.println("<th>#</th><th>State</th><th>City</th><th>Location</th>");
        out.println("<th>Area</th><th>Bed</th><th>Bath</th><th>Age</th>");
        out.println("<th>Predicted Price (&#8377;)</th><th>Date</th></tr></thead>");
        out.println("<tbody id='tableBody'>");

        if (list.isEmpty()) {
            out.println("<tr><td colspan='10' class='text-muted py-4'>No search history yet. Go search &amp; predict!</td></tr>");
        } else {
            int c = 0;
            for (SearchHistoryModel sh : list) {
                c++;
                out.println("<tr>");
                out.println("<td>" + c + "</td>");
                out.println("<td>" + sh.getStateName() + "</td>");
                out.println("<td>" + sh.getCityName() + "</td>");
                out.println("<td>" + sh.getLocName() + "</td>");
                out.println("<td>" + sh.getSqFeet() + " sqft</td>");
                out.println("<td>" + sh.getNbed() + "</td>");
                out.println("<td>" + sh.getNbath() + "</td>");
                out.println("<td>" + sh.getAge() + " yrs</td>");
                out.println("<td class='text-success fw-bold'>&#8377; " + String.format("%,.2f", sh.getPredictedPrice()) + "</td>");
                out.println("<td class='text-muted small'>" + sh.getSearchDate() + "</td>");
                out.println("</tr>");
            }
        }
        out.println("</tbody></table></div></div></div>");

        // Real-time search JS
        out.println("<script>");
        out.println("function filterTable(){");
        out.println("  var input=document.getElementById('searchInput').value.toLowerCase();");
        out.println("  var rows=document.getElementById('tableBody').getElementsByTagName('tr');");
        out.println("  for(var i=0;i<rows.length;i++){");
        out.println("    rows[i].style.display=rows[i].textContent.toLowerCase().includes(input)?'':'none';");
        out.println("  }");
        out.println("}");
        out.println("</script>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
