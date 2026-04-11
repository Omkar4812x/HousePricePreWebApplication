package edu.omkar.repository;

import java.util.List;
import edu.omkar.model.PropertyModel;

public interface PropertyRepository {
    boolean addProperty(PropertyModel model);
    List<PropertyModel> getAllProperties();
    PropertyModel getPropertyById(int id);
    boolean deletePropertyById(int id);
    boolean updateProperty(PropertyModel model);
}
