package edu.omkar.repository;

import java.util.ArrayList;
import java.util.List;
import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.LocationModel;

public class LocationRepositoryImpl extends DBConfig implements LocationRepository {

    @Override
    public boolean addLocation(LocationModel model) {
        try {
            stmt = conn.prepareStatement("INSERT INTO location(locname, ctid, status) VALUES(?,?,1)");
            stmt.setString(1, model.getName());
            stmt.setInt(2, model.getCityId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error addLocation: " + ex);
            return false;
        }
    }

    @Override
    public List<LocationModel> getAllLocations() {
        List<LocationModel> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(
                "SELECT l.locid, l.locname, l.ctid, c.cityname, s.statename " +
                "FROM location l " +
                "JOIN city c ON l.ctid = c.ctid " +
                "JOIN state s ON c.stateid = s.stateid " +
                "WHERE l.status = 1 ORDER BY l.locid");
            rs = stmt.executeQuery();
            while (rs.next()) {
                LocationModel lm = new LocationModel();
                lm.setId(rs.getInt(1));
                lm.setName(rs.getString(2));
                lm.setCityId(rs.getInt(3));
                lm.setCityName(rs.getString(4));
                lm.setStateName(rs.getString(5));
                list.add(lm);
            }
        } catch (Exception ex) {
            System.out.println("Error getAllLocations: " + ex);
        }
        return list;
    }

    @Override
    public List<LocationModel> getLocationsByCity(int cityId) {
        List<LocationModel> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(
                "SELECT locid, locname FROM location WHERE ctid = ? AND status = 1 ORDER BY locname");
            stmt.setInt(1, cityId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                LocationModel lm = new LocationModel();
                lm.setId(rs.getInt(1));
                lm.setName(rs.getString(2));
                list.add(lm);
            }
        } catch (Exception ex) {
            System.out.println("Error getLocationsByCity: " + ex);
        }
        return list;
    }

    @Override
    public boolean deleteLocationById(int id) {
        try {
            stmt = conn.prepareStatement("UPDATE location SET status = 0 WHERE locid = ?");
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error deleteLocationById: " + ex);
            return false;
        }
    }

    @Override
    public boolean updateLocation(LocationModel model) {
        try {
            stmt = conn.prepareStatement("UPDATE location SET locname = ?, ctid = ? WHERE locid = ?");
            stmt.setString(1, model.getName());
            stmt.setInt(2, model.getCityId());
            stmt.setInt(3, model.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error updateLocation: " + ex);
            return false;
        }
    }
}
