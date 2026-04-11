package edu.omkar.services;

import edu.omkar.model.ModelParam;

public interface ModelService {
    boolean saveModel(ModelParam model);
    ModelParam getLatestModel();
}
