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
import edu.omkar.model.ModelParam;
import edu.omkar.model.PropertyModel;
import edu.omkar.services.ModelService;
import edu.omkar.services.ModelServiceImpl;
import edu.omkar.services.PropertyService;
import edu.omkar.services.PropertyServiceImpl;
import edu.omkar.util.LinearRegressionTrainer;

@WebServlet("/trainmodel")
public class TrainModelServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        ModelService modelService = new ModelServiceImpl();
        ModelParam latest = modelService.getLatestModel();

        out.println("<div class='container mt-5'>");
        out.println("<h3 class='text-center mb-4'>&#129302; Linear Regression Model Training</h3>");

        // Current model stats card
        if (latest != null) {
            out.println("<div class='row justify-content-center mb-4'>");
            out.println("<div class='col-md-8'>");
            out.println("<div class='card bg-dark text-white border-secondary shadow'>");
            out.println("<div class='card-header text-center border-secondary'><h5 class='mb-0'>&#9989; Last Trained Model</h5></div>");
            out.println("<div class='card-body'>");
            out.println("<div class='row text-center'>");
            out.println("<div class='col-md-4'><div class='p-3 border border-secondary rounded mb-2'>");
            out.println("<h6 class='text-muted'>R&sup2; Score</h6>");
            out.println("<h3 class='text-success'>" + String.format("%.4f", latest.getRSquared()) + "</h3></div></div>");
            out.println("<div class='col-md-4'><div class='p-3 border border-secondary rounded mb-2'>");
            out.println("<h6 class='text-muted'>MSE</h6>");
            out.println("<h3 class='text-warning'>" + String.format("%.2f", latest.getMse()) + "</h3></div></div>");
            out.println("<div class='col-md-4'><div class='p-3 border border-secondary rounded mb-2'>");
            out.println("<h6 class='text-muted'>Trained At</h6>");
            out.println("<h6 class='text-info'>" + latest.getTrainedAt() + "</h6></div></div></div>");
            out.println("<hr class='border-secondary'>");
            out.println("<h6 class='mb-2'>Model Equation:</h6>");
            out.println("<code class='text-warning small'>");
            out.println("Price = " + String.format("%.4f", latest.getIntercept()));
            out.println(" + " + String.format("%.4f", latest.getSlopeSqFeet()) + " * SqFeet");
            out.println(" + " + String.format("%.4f", latest.getSlopeNbed()) + " * Bedrooms");
            out.println(" + " + String.format("%.4f", latest.getSlopeNbath()) + " * Bathrooms");
            out.println(" + " + String.format("%.4f", latest.getSlopeAge()) + " * Age");
            out.println("</code>");
            out.println("</div></div></div></div>");
        } else {
            out.println("<div class='alert alert-warning text-center'>No model trained yet. Click Train Model to begin.</div>");
        }

        // Train button
        out.println("<div class='row justify-content-center mb-4'>");
        out.println("<div class='col-md-6 text-center'>");
        out.println("<form action='trainmodel' method='post'>");
        out.println("<button type='submit' name='action' value='train' class='btn btn-success btn-lg px-5'>");
        out.println("&#128640; " + (latest == null ? "Train Model" : "Retrain Model") + "</button>");
        out.println("</form></div></div>");

        // Info box
        out.println("<div class='row justify-content-center'>");
        out.println("<div class='col-md-8'>");
        out.println("<div class='card bg-dark border-secondary text-white-50'>");
        out.println("<div class='card-body small'>");
        out.println("<h6 class='text-white'>&#128218; About the Model</h6>");
        out.println("<p>Uses <strong>Multiple Linear Regression</strong> with Normal Equation:</p>");
        out.println("<code class='text-info'>price = b + m1&times;area + m2&times;bedrooms + m3&times;bathrooms + m4&times;age</code><br><br>");
        out.println("<ul><li><strong>R&sup2; Score:</strong> Closer to 1.0 means better fit</li>");
        out.println("<li><strong>MSE:</strong> Lower is better (Mean Squared Error)</li>");
        out.println("<li>Minimum <strong>10 property records</strong> needed to train</li></ul>");
        out.println("</div></div></div></div>");
        out.println("</div>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher r = request.getRequestDispatcher("admindashboard.html");
        r.include(request, response);

        out.println("<div class='container mt-5'>");

        PropertyService propertyService = new PropertyServiceImpl();
        List<PropertyModel> data = propertyService.getAllProperties();

        if (data == null || data.size() < 5) {
            out.println("<div class='alert alert-danger text-center'>");
            out.println("<h5>&#10060; Not enough data! Need at least 5 property records to train.</h5>");
            out.println("<p>Currently have: " + (data == null ? 0 : data.size()) + " records.</p>");
            out.println("<a href='addproperty' class='btn btn-primary'>Add Property Data</a>");
            out.println("</div></div>");
            return;
        }

        try {
            // Train model using Normal Equation
            double[] coeffs = LinearRegressionTrainer.train(data);
            double rSquared = LinearRegressionTrainer.computeRSquared(data, coeffs);
            double mse      = LinearRegressionTrainer.computeMSE(data, coeffs);

            // Save model to DB
            ModelParam mp = new ModelParam();
            mp.setIntercept(coeffs[0]);
            mp.setSlopeSqFeet(coeffs[1]);
            mp.setSlopeNbed(coeffs[2]);
            mp.setSlopeNbath(coeffs[3]);
            mp.setSlopeAge(coeffs[4]);
            mp.setRSquared(rSquared);
            mp.setMse(mse);

            ModelService modelService = new ModelServiceImpl();
            boolean saved = modelService.saveModel(mp);

            if (saved) {
                out.println("<div class='alert alert-success text-center'><h5>&#9989; Model Trained Successfully!</h5></div>");
                out.println("<div class='row justify-content-center'>");
                out.println("<div class='col-md-8'><div class='card bg-dark text-white border-secondary shadow'>");
                out.println("<div class='card-header text-center'><h5>Training Results (" + data.size() + " records used)</h5></div>");
                out.println("<div class='card-body'>");
                out.println("<div class='row text-center'>");
                out.println("<div class='col-md-6'><div class='p-3 border border-secondary rounded mb-3'>");
                out.println("<h6 class='text-muted'>R&sup2; Score</h6>");
                out.println("<h2 class='text-success'>" + String.format("%.4f", rSquared) + "</h2>");
                out.println("<small class='text-muted'>" + String.format("%.2f%%", rSquared * 100) + " accuracy</small>");
                out.println("</div></div>");
                out.println("<div class='col-md-6'><div class='p-3 border border-secondary rounded mb-3'>");
                out.println("<h6 class='text-muted'>MSE</h6>");
                out.println("<h2 class='text-warning'>" + String.format("%.2f", mse) + "</h2>");
                out.println("<small class='text-muted'>Mean Squared Error</small>");
                out.println("</div></div></div>");
                out.println("<hr class='border-secondary'>");
                out.println("<h6>Learned Equation:</h6>");
                out.println("<code class='text-warning'>");
                out.println("Price = " + String.format("%.4f",coeffs[0]));
                out.println(" + " + String.format("%.4f",coeffs[1]) + "&times;Area");
                out.println(" + " + String.format("%.4f",coeffs[2]) + "&times;Bedrooms");
                out.println(" + " + String.format("%.4f",coeffs[3]) + "&times;Bathrooms");
                out.println(" + " + String.format("%.4f",coeffs[4]) + "&times;Age");
                out.println("</code>");
                out.println("</div></div></div></div>");
            } else {
                out.println("<div class='alert alert-danger text-center'>Model trained but failed to save. Check DB connection.</div>");
            }
        } catch (Exception ex) {
            out.println("<div class='alert alert-danger text-center'>Training Error: " + ex.getMessage() + "</div>");
        }
        out.println("</div>");
    }
}
