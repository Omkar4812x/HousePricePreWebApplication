package edu.omkar.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import edu.omkar.model.StateModel;
import edu.omkar.services.*;


@WebServlet("/addstate")
public class AddStateServlate extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		
		RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
		r.include(request, response);
		out.println("<form name = 'frm' action = '' method = 'post'>");
		out.println("<div class='container mt-5'>");
		out.println("<div class='row justify-content-center'>");
		out.println("<div class='col-md-6 col-lg-5'>");
		out.println("<div class='card shadow-lg border-0'>");
		out.println("<div class='card-body bg-dark text-white p-5'>");
		out.println("<h2 class='text-center mb-4'>Add New State</h2>");
		out.println("<div class='mb-4'>");
		out.println("<label class='form-label'>State Name</label>");
		out.println("<input type='text' name='name' class='form-control form-control-lg' placeholder='Enter state name' required>");
		out.println("</div>");
		out.println("<div class='d-grid'>");
		out.println("<input type='submit' value='Add State' name = 's' class='btn btn-primary btn-lg'>");
		out.println("</div>");
		out.println("</div>"); 
		out.println("</div>"); 
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("</form>");
		String btn = request.getParameter("s");
		
		if(btn!=null)
		{
			StateModel sm = new StateModel();
			String stateName = request.getParameter("name");
			sm.setName(stateName);
			StateService stateService = new StateServiceImpl();
			boolean b = stateService.isAddState(sm);
			
			if(b)
			{
				out.println("<h1 class ='text-dark'>State Added Successfully ...</h1>");
			}
			else
			{
				out.println("<h1 class ='text-dark'>State Not Added ...</h1>");
			}
		}
		
		else
		{
			
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
