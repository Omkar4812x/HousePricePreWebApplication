package edu.omkar.services;

import edu.omkar.model.ModelParam;
import edu.omkar.repository.ModelRepositoryImpl;

public class ModelServiceImpl implements ModelService {
    ModelRepositoryImpl modelRepo = new ModelRepositoryImpl();

    @Override
    public boolean saveModel(ModelParam model) { return modelRepo.saveModel(model); }

    @Override
    public ModelParam getLatestModel() { return modelRepo.getLatestModel(); }
}
