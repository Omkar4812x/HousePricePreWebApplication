package edu.omkar.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;

@WebServlet("/deletestate")
public class DeleteStateServlate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
		r.include(request, response);
		int stateId = Integer.parseInt(request.getParameter("stateid"));
		StateService stateService = new StateServiceImpl();
		boolean b = stateService.isDeleteStateById(stateId);
		if (b) {
			response.sendRedirect("viewstate");
		} else {
			out.println("<h1>State Not Deleted Successfully...</h1>");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
