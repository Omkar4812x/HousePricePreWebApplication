package edu.omkar.repository;

import java.util.ArrayList;
import java.util.List;
import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.SearchHistoryModel;

public class SearchHistoryRepositoryImpl extends DBConfig implements SearchHistoryRepository {

    @Override
    public boolean saveHistory(SearchHistoryModel model) {
        try {
            stmt = conn.prepareStatement(
                "INSERT INTO searchhistory(userid, statename, cityname, locname, asqfeet, nbed, nbath, age, predicted_price) " +
                "VALUES(?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, model.getUserId());
            stmt.setString(2, model.getStateName());
            stmt.setString(3, model.getCityName());
            stmt.setString(4, model.getLocName());
            stmt.setInt(5, model.getSqFeet());
            stmt.setInt(6, model.getNbed());
            stmt.setInt(7, model.getNbath());
            stmt.setInt(8, model.getAge());
            stmt.setDouble(9, model.getPredictedPrice());
            return stmt.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Error saveHistory: " + ex);
            return false;
        }
    }

    @Override
    public List<SearchHistoryModel> getHistoryByUser(int userId) {
        List<SearchHistoryModel> list = new ArrayList<>();
        try {
            stmt = conn.prepareStatement(
                "SELECT histid, userid, statename, cityname, locname, asqfeet, nbed, nbath, age, predicted_price, search_date " +
                "FROM searchhistory WHERE userid = ? ORDER BY histid DESC");
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                SearchHistoryModel sh = new SearchHistoryModel();
                sh.setHistId(rs.getInt(1));
                sh.setUserId(rs.getInt(2));
                sh.setStateName(rs.getString(3));
                sh.setCityName(rs.getString(4));
                sh.setLocName(rs.getString(5));
                sh.setSqFeet(rs.getInt(6));
                sh.setNbed(rs.getInt(7));
                sh.setNbath(rs.getInt(8));
                sh.setAge(rs.getInt(9));
                sh.setPredictedPrice(rs.getDouble(10));
                sh.setSearchDate(rs.getString(11));
                list.add(sh);
            }
        } catch (Exception ex) {
            System.out.println("Error getHistoryByUser: " + ex);
        }
        return list;
    }
}
