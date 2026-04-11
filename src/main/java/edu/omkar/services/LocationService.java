package edu.omkar.services;

import java.util.List;
import edu.omkar.model.LocationModel;

public interface LocationService {
    boolean addLocation(LocationModel model);
    List<LocationModel> getAllLocations();
    List<LocationModel> getLocationsByCity(int cityId);
    boolean deleteLocationById(int id);
    boolean updateLocation(LocationModel model);
}
