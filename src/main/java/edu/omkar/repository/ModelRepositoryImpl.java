package edu.omkar.repository;

import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.ModelParam;

public class ModelRepositoryImpl extends DBConfig implements ModelRepository {

    @Override
    public boolean saveModel(ModelParam model) {
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO model_params(intercept, slope_sqfeet, slope_nbed, slope_nbath, slope_age, r_squared, mse) " +
                "VALUES(?,?,?,?,?,?,?)");
            stmt.setDouble(1, model.getIntercept());
            stmt.setDouble(2, model.getSlopeSqFeet());
            stmt.setDouble(3, model.getSlopeNbed());
            stmt.setDouble(4, model.getSlopeNbath());
            stmt.setDouble(5, model.getSlopeAge());
            stmt.setDouble(6, model.getRSquared());
            stmt.setDouble(7, model.getMse());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error saveModel: " + ex);
            return false;
        }
    }

    @Override
    public ModelParam getLatestModel() {
        try {
            stmt = conn.prepareStatement(
                "SELECT paramid, intercept, slope_sqfeet, slope_nbed, slope_nbath, slope_age, " +
                "r_squared, mse, trained_at FROM model_params ORDER BY paramid DESC LIMIT 1");
            rs = stmt.executeQuery();
            if (rs.next()) {
                ModelParam mp = new ModelParam();
                mp.setId(rs.getInt(1));
                mp.setIntercept(rs.getDouble(2));
                mp.setSlopeSqFeet(rs.getDouble(3));
                mp.setSlopeNbed(rs.getDouble(4));
                mp.setSlopeNbath(rs.getDouble(5));
                mp.setSlopeAge(rs.getDouble(6));
                mp.setRSquared(rs.getDouble(7));
                mp.setMse(rs.getDouble(8));
                mp.setTrainedAt(rs.getString(9));
                return mp;
            }
        } catch (Exception ex) {
            System.out.println("Error getLatestModel: " + ex);
        }
        return null;
    }
}
