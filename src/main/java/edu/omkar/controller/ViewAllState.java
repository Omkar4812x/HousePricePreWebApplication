package edu.omkar.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import edu.omkar.model.*;
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;

@WebServlet("/viewstate")
public class ViewAllState extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
		r.include(request, response);
		StateService stateService = new StateServiceImpl();
		List<StateModel> list = stateService.getAllStates();

		out.println("<div class='container mt-5'>");

		out.println("<div class='mb-3'>");
		out.println(
				"<input type='text' id='searchInput' onkeyup='filterTable()' class='form-control bg-white text-dark shadow-sm' placeholder='&#128269; Search states...'>");
		out.println("</div>");

		out.println("<div class='card shadow-lg border-0 bg-dark text-white'>");

		out.println("<div class='card-header bg-dark text-white text-center border-bottom'>");
		out.println("<h4 class='mb-0'>State List</h4>");
		out.println("</div>");

		out.println("<div class='card-body p-0'>");
		out.println("<table class='table table-dark table-hover text-center align-middle mb-0'>");

		out.println("<thead class='border-bottom'>");
		out.println("<tr>");
		out.println("<th class='py-3'>SRNO</th>");
		out.println("<th class='py-3'>STATE NAME</th>");
		out.println("<th class='py-3'>DELETE</th>");
		out.println("<th class='py-3'>UPDATE</th>");
		out.println("</tr>");
		out.println("</thead>");

		out.println("<tbody id='tableBody'>");

		int count = 0;

		for (StateModel sm : list) {
			++count;

			out.println("<tr style='border-bottom:1px solid #555;'>");
			out.println("<td>" + count + "</td>");
			out.println("<td>" + sm.getName() + "</td>");
			out.println("<td><a href='deletestate?stateid=" + sm.getId()
					+ "' class='text-primary text-decoration-none fw-bold' onclick='return confirm(\"Are you sure?\")'>DELETE</a></td>");
			out.println("<td><a href='updstate?stateid=" + sm.getId() + "&statename=" + sm.getName()
					+ "' class='text-primary text-decoration-none fw-bold'>UPDATE</a></td>");
			out.println("</tr>");
		}

		out.println("</tbody>");
		out.println("</table>");
		out.println("</div>");

		out.println("</div>");
		out.println("</div>");

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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
