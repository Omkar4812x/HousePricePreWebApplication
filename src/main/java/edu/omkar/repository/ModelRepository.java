package edu.omkar.repository;

import edu.omkar.model.ModelParam;

public interface ModelRepository {
    boolean saveModel(ModelParam model);
    ModelParam getLatestModel();
}
