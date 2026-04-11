package edu.omkar.repository;

import java.util.List;
import edu.omkar.model.LocationModel;

public interface LocationRepository {
    boolean addLocation(LocationModel model);
    List<LocationModel> getAllLocations();
    List<LocationModel> getLocationsByCity(int cityId);
    boolean deleteLocationById(int id);
    boolean updateLocation(LocationModel model);
}
