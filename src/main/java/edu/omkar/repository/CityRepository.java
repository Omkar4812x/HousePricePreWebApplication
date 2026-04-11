package edu.omkar.repository;

import java.util.List;
import edu.omkar.model.CityModel;

public interface CityRepository {
    boolean addCity(CityModel model);
    List<CityModel> getAllCities();
    List<CityModel> getCitiesByState(int stateId);
    boolean deleteCityById(int id);
    boolean updateCity(CityModel model);
}
