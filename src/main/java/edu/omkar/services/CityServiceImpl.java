package edu.omkar.services;

import java.util.List;
import edu.omkar.model.CityModel;
import edu.omkar.repository.CityRepositoryImpl;

public class CityServiceImpl implements CityService {
    CityRepositoryImpl cityRepo = new CityRepositoryImpl();

    @Override
    public boolean addCity(CityModel model) { return cityRepo.addCity(model); }

    @Override
    public List<CityModel> getAllCities() { return cityRepo.getAllCities(); }

    @Override
    public List<CityModel> getCitiesByState(int stateId) { return cityRepo.getCitiesByState(stateId); }

    @Override
    public boolean deleteCityById(int id) { return cityRepo.deleteCityById(id); }

    @Override
    public boolean updateCity(CityModel model) { return cityRepo.updateCity(model); }
}
