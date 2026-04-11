package edu.omkar.repository;

import java.util.ArrayList;
import java.util.List;
import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.CityModel;

public class CityRepositoryImpl extends DBConfig implements CityRepository {

    @Override
    public boolean addCity(CityModel model) {
        try {
            stmt = conn.prepareStatement("INSERT INTO city(cityname, stateid, status) VALUES(?,?,1)");
            stmt.setString(1, model.getName());
            stmt.setInt(2, model.getStateId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error addCity: " + ex);
            return false;
        }
    }

    @Override
    public List<CityModel> getAllCities() {
        List<CityModel> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(
                "SELECT c.ctid, c.cityname, c.stateid, s.statename " +
                "FROM city c JOIN state s ON c.stateid = s.stateid " +
                "WHERE c.status = 1 ORDER BY c.ctid");
            rs = stmt.executeQuery();
            while (rs.next()) {
                CityModel cm = new CityModel();
                cm.setId(rs.getInt(1));
                cm.setName(rs.getString(2));
                cm.setStateId(rs.getInt(3));
                cm.setStateName(rs.getString(4));
                list.add(cm);
            }
        } catch (Exception ex) {
            System.out.println("Error getAllCities: " + ex);
        }
        return list;
    }

    @Override
    public List<CityModel> getCitiesByState(int stateId) {
        List<CityModel> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(
                "SELECT ctid, cityname FROM city WHERE stateid = ? AND status = 1 ORDER BY cityname");
            stmt.setInt(1, stateId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CityModel cm = new CityModel();
                cm.setId(rs.getInt(1));
                cm.setName(rs.getString(2));
                list.add(cm);
            }
        } catch (Exception ex) {
            System.out.println("Error getCitiesByState: " + ex);
        }
        return list;
    }

    @Override
    public boolean deleteCityById(int id) {
        try {
            stmt = conn.prepareStatement("UPDATE city SET status = 0 WHERE ctid = ?");
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error deleteCityById: " + ex);
            return false;
        }
    }

    @Override
    public boolean updateCity(CityModel model) {
        try {
            stmt = conn.prepareStatement("UPDATE city SET cityname = ?, stateid = ? WHERE ctid = ?");
            stmt.setString(1, model.getName());
            stmt.setInt(2, model.getStateId());
            stmt.setInt(3, model.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error updateCity: " + ex);
            return false;
        }
    }
}
