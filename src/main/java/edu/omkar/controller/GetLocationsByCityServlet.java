package edu.omkar.controller;

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

@WebServlet("/getlocationsbycity")
public class GetLocationsByCityServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int ctid = Integer.parseInt(request.getParameter("ctid"));
            LocationService locationService = new LocationServiceImpl();
            List<LocationModel> list = locationService.getLocationsByCity(ctid);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                LocationModel lm = list.get(i);
                json.append("{\"id\":").append(lm.getId())
                    .append(",\"name\":\"").append(lm.getName().replace("\"", "\\\"")).append("\"}");
                if (i < list.size() - 1) json.append(",");
            }
            json.append("]");
            out.print(json.toString());
        } catch (Exception ex) {
            out.print("[]");
        }
    }
}
