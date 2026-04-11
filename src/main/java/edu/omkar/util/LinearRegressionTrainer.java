package edu.omkar.util;

import java.util.List;
import edu.omkar.model.PropertyModel;

/**
 * Multiple Linear Regression Trainer (Pure Java, No external libraries)
 * Model: price = b + m1*sqfeet + m2*nbed + m3*nbath + m4*age
 * Uses Normal Equation: β = (X^T X)^{-1} X^T y  solved via Gaussian Elimination
 */
public class LinearRegressionTrainer {

    /**
     * Train the model on given property data.
     * @return double[] coefficients: [intercept, slope_sqfeet, slope_nbed, slope_nbath, slope_age]
     */
    public static double[] train(List<PropertyModel> data) {
        int n = data.size();
        int p = 5; // intercept + 4 features

        double[][] X = new double[n][p];
        double[] y = new double[n];

        for (int i = 0; i < n; i++) {
            PropertyModel pm = data.get(i);
            X[i][0] = 1.0;                       // intercept term
            X[i][1] = pm.getSqFeet();             // area
            X[i][2] = pm.getNbed();               // bedrooms
            X[i][3] = pm.getNbath();              // bathrooms
            X[i][4] = pm.getAge();                // age
            y[i]    = pm.getActualPrice();
        }

        // Compute X^T X  (p x p)
        double[][] XtX = new double[p][p];
        for (int i = 0; i < p; i++)
            for (int j = 0; j < p; j++)
                for (int k = 0; k < n; k++)
                    XtX[i][j] += X[k][i] * X[k][j];

        // Compute X^T y  (p x 1)
        double[] Xty = new double[p];
        for (int i = 0; i < p; i++)
            for (int k = 0; k < n; k++)
                Xty[i] += X[k][i] * y[k];

        // Solve XtX * β = Xty  using Gaussian Elimination
        return gaussianElimination(XtX, Xty, p);
    }

    /** Gauss-Jordan elimination with partial pivoting to solve A*x = b */
    private static double[] gaussianElimination(double[][] A, double[] b, int n) {
        double[][] aug = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) aug[i][j] = A[i][j];
            aug[i][n] = b[i];
        }

        // Forward elimination
        for (int col = 0; col < n; col++) {
            int maxRow = col;
            for (int row = col + 1; row < n; row++)
                if (Math.abs(aug[row][col]) > Math.abs(aug[maxRow][col]))
                    maxRow = row;
            double[] tmp = aug[col]; aug[col] = aug[maxRow]; aug[maxRow] = tmp;

            if (Math.abs(aug[col][col]) < 1e-12) continue;

            for (int row = col + 1; row < n; row++) {
                double factor = aug[row][col] / aug[col][col];
                for (int j = col; j <= n; j++)
                    aug[row][j] -= factor * aug[col][j];
            }
        }

        // Back substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = aug[i][n];
            for (int j = i + 1; j < n; j++)
                x[i] -= aug[i][j] * x[j];
            if (Math.abs(aug[i][i]) > 1e-12)
                x[i] /= aug[i][i];
        }
        return x;
    }

    /**
     * Predict price for a single house.
     * @param coeffs array from train(): [intercept, m_sqfeet, m_nbed, m_nbath, m_age]
     */
    public static double predict(double[] coeffs, double sqFeet, int nbed, int nbath, int age) {
        return coeffs[0]
             + coeffs[1] * sqFeet
             + coeffs[2] * nbed
             + coeffs[3] * nbath
             + coeffs[4] * age;
    }

    /**
     * R² (coefficient of determination) — how well model fits data (1.0 = perfect)
     */
    public static double computeRSquared(List<PropertyModel> data, double[] coeffs) {
        double meanY = 0;
        for (PropertyModel pm : data) meanY += pm.getActualPrice();
        meanY /= data.size();

        double ssTot = 0, ssRes = 0;
        for (PropertyModel pm : data) {
            double pred = predict(coeffs, pm.getSqFeet(), pm.getNbed(), pm.getNbath(), pm.getAge());
            ssRes += Math.pow(pm.getActualPrice() - pred, 2);
            ssTot += Math.pow(pm.getActualPrice() - meanY, 2);
        }
        return (ssTot == 0) ? 0 : 1.0 - (ssRes / ssTot);
    }

    /**
     * MSE (Mean Squared Error) — average squared difference between actual and predicted
     */
    public static double computeMSE(List<PropertyModel> data, double[] coeffs) {
        double sum = 0;
        for (PropertyModel pm : data) {
            double pred = predict(coeffs, pm.getSqFeet(), pm.getNbed(), pm.getNbath(), pm.getAge());
            sum += Math.pow(pm.getActualPrice() - pred, 2);
        }
        return sum / data.size();
    }
}
