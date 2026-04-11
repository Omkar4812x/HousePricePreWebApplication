package edu.omkar.services;

import java.util.List;
import edu.omkar.model.PropertyModel;
import edu.omkar.repository.PropertyRepositoryImpl;

public class PropertyServiceImpl implements PropertyService {
    PropertyRepositoryImpl propertyRepo = new PropertyRepositoryImpl();

    @Override
    public boolean addProperty(PropertyModel model) { return propertyRepo.addProperty(model); }

    @Override
    public List<PropertyModel> getAllProperties() { return propertyRepo.getAllProperties(); }

    @Override
    public PropertyModel getPropertyById(int id) { return propertyRepo.getPropertyById(id); }

    @Override
    public boolean deletePropertyById(int id) { return propertyRepo.deletePropertyById(id); }

    @Override
    public boolean updateProperty(PropertyModel model) { return propertyRepo.updateProperty(model); }
}
