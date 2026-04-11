package edu.omkar.repository;

import java.util.ArrayList;
import java.util.List;
import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.PropertyModel;

public class PropertyRepositoryImpl extends DBConfig implements PropertyRepository {

    @Override
    public boolean addProperty(PropertyModel model) {
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO property(pname, paddress, age, asqfeet, nbath, nbed, actualprice, locid, status) " +
                "VALUES(?,?,?,?,?,?,?,?,1)");
            stmt.setString(1, model.getName());
            stmt.setString(2, model.getAddress());
            stmt.setInt(3, model.getAge());
            stmt.setInt(4, model.getSqFeet());
            stmt.setInt(5, model.getNbath());
            stmt.setInt(6, model.getNbed());
            stmt.setDouble(7, model.getActualPrice());
            stmt.setInt(8, model.getLocId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error addProperty: " + ex);
            return false;
        }
    }

    @Override
    public List<PropertyModel> getAllProperties() {
        List<PropertyModel> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(
                "SELECT p.pid, p.pname, p.paddress, p.age, p.asqfeet, p.nbath, p.nbed, p.actualprice," +
                " p.locid, l.locname, l.ctid, c.cityname, c.stateid, s.statename " +
                "FROM property p " +
                "JOIN location l ON p.locid = l.locid " +
                "JOIN city c ON l.ctid = c.ctid " +
                "JOIN state s ON c.stateid = s.stateid " +
                "WHERE p.status = 1 ORDER BY p.pid");
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception ex) {
            System.out.println("Error getAllProperties: " + ex);
        }
        return list;
    }

    @Override
    public PropertyModel getPropertyById(int id) {
        try {
            stmt = conn.prepareStatement(
                "SELECT p.pid, p.pname, p.paddress, p.age, p.asqfeet, p.nbath, p.nbed, p.actualprice," +
                " p.locid, l.locname, l.ctid, c.cityname, c.stateid, s.statename " +
                "FROM property p " +
                "JOIN location l ON p.locid = l.locid " +
                "JOIN city c ON l.ctid = c.ctid " +
                "JOIN state s ON c.stateid = s.stateid " +
                "WHERE p.pid = ? AND p.status = 1");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception ex) {
            System.out.println("Error getPropertyById: " + ex);
        }
        return null;
    }

    private PropertyModel mapRow(java.sql.ResultSet rs) throws Exception {
        PropertyModel pm = new PropertyModel();
        pm.setId(rs.getInt(1));
        pm.setName(rs.getString(2));
        pm.setAddress(rs.getString(3));
        pm.setAge(rs.getInt(4));
        pm.setSqFeet(rs.getInt(5));
        pm.setNbath(rs.getInt(6));
        pm.setNbed(rs.getInt(7));
        pm.setActualPrice(rs.getDouble(8));
        pm.setLocId(rs.getInt(9));
        pm.setLocName(rs.getString(10));
        pm.setCityId(rs.getInt(11));
        pm.setCityName(rs.getString(12));
        pm.setStateId(rs.getInt(13));
        pm.setStateName(rs.getString(14));
        return pm;
    }

    @Override
    public boolean deletePropertyById(int id) {
        try {
            stmt = conn.prepareStatement("UPDATE property SET status = 0 WHERE pid = ?");
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error deletePropertyById: " + ex);
            return false;
        }
    }

    @Override
    public boolean updateProperty(PropertyModel model) {
        try {
            stmt = conn.prepareStatement(
                "UPDATE property SET pname=?, paddress=?, age=?, asqfeet=?, nbath=?, nbed=?, actualprice=?, locid=? " +
                "WHERE pid=?");
            stmt.setString(1, model.getName());
            stmt.setString(2, model.getAddress());
            stmt.setInt(3, model.getAge());
            stmt.setInt(4, model.getSqFeet());
            stmt.setInt(5, model.getNbath());
            stmt.setInt(6, model.getNbed());
            stmt.setDouble(7, model.getActualPrice());
            stmt.setInt(8, model.getLocId());
            stmt.setInt(9, model.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error updateProperty: " + ex);
            return false;
        }
    }
}
