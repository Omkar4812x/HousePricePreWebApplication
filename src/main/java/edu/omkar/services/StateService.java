package edu.omkar.services;

import java.util.List;

import edu.omkar.model.StateModel;

public interface StateService {
	public boolean isAddState(StateModel model);
	public List<StateModel> getAllStates();
	public boolean isDeleteStateById(int id);
	public boolean idUpdateState(StateModel model);
	
}
