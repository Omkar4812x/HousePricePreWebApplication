package edu.omkar.dbconfig;

import java.sql.*;

public class DBConfig {
	protected Connection conn;
	protected PreparedStatement stmt;
	protected ResultSet rs;

	public DBConfig() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/house_price_prediction_system", "root",
					"Omkar@2004");
		} catch (ClassNotFoundException ex) {
			System.out.println("JDBC Driver class not found: " + ex);
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("Database connection failed: " + ex);
			ex.printStackTrace();
		} catch (Exception ex) {
			System.out.println("Error is " + ex);
			ex.printStackTrace();
		}
	}

}
