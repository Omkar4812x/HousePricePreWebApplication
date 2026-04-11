package edu.omkar.services;

import java.util.List;
import edu.omkar.model.LocationModel;
import edu.omkar.repository.LocationRepositoryImpl;

public class LocationServiceImpl implements LocationService {
    LocationRepositoryImpl locationRepo = new LocationRepositoryImpl();

    @Override
    public boolean addLocation(LocationModel model) { return locationRepo.addLocation(model); }

    @Override
    public List<LocationModel> getAllLocations() { return locationRepo.getAllLocations(); }

    @Override
    public List<LocationModel> getLocationsByCity(int cityId) { return locationRepo.getLocationsByCity(cityId); }

    @Override
    public boolean deleteLocationById(int id) { return locationRepo.deleteLocationById(id); }

    @Override
    public boolean updateLocation(LocationModel model) { return locationRepo.updateLocation(model); }
}
