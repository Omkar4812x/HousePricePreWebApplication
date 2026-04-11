package edu.omkar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import edu.omkar.model.CityModel;
import edu.omkar.services.CityService;
import edu.omkar.services.CityServiceImpl;

@WebServlet("/getcitiesbystate")
public class GetCitiesByStateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int stateId = Integer.parseInt(request.getParameter("stateid"));
            CityService cityService = new CityServiceImpl();
            List<CityModel> list = cityService.getCitiesByState(stateId);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                CityModel cm = list.get(i);
                json.append("{\"id\":").append(cm.getId())
                    .append(",\"name\":\"").append(cm.getName().replace("\"", "\\\"")).append("\"}");
                if (i < list.size() - 1) json.append(",");
            }
            json.append("]");
            out.print(json.toString());
        } catch (Exception ex) {
            out.print("[]");
        }
    }
}
