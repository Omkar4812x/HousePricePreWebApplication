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
import edu.omkar.services.StateService;
import edu.omkar.services.StateServiceImpl;

@WebServlet("/updstate")
public class UpdateStateServlate extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
		r.include(request, response);
		
		int stateId = Integer.parseInt(request.getParameter("stateid"));
		String stateName = request.getParameter("statename");
		
		out.println("<form name = 'frm' action='' method='post'>");
		
		out.println("<div class='container p-5 bg-dark text-white mt-1'>");
		out.println("<div class='form-group '>");
		out.println("<input type='hidden' name='id' value='"+stateId+"' class='form-control' placeholder = 'Enter State Id' />");
		out.println("</div>");
		
		out.println("<div class='form-group  mt-3'>");
		out.println("<input type='text' name='name' value='"+stateName+"' class='form-control' placeholder = 'Enter State Name'/>");
		out.println("</ div>");
		
		
		out.println("<div class='form-group mt-3'>");
		out.println("<input type='submit' name='s' value='Update State' class='form-control' />");
		out.println("</div>");
		out.println("</div>");

		
		out.println("</div>");
		out.println("</form>");
		
		String btn = request.getParameter("s");
		if(btn!=null)
		{
			stateName = request.getParameter("name");
			 stateId = Integer.parseInt(request.getParameter("id"));
			 StateModel sm = new  StateModel();
			 sm.setName(stateName);
			 sm.setId(stateId);
			 
			 StateService stateService  = new StateServiceImpl();
			 boolean b = stateService.idUpdateState(sm);
			 if(b)
			 {
				 response.sendRedirect("viewstate");
			 }
			 else
			 {
				 out.println("<h1>State Not Updated</h1>");
			 }
					 
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
