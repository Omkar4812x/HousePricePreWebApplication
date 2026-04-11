package edu.omkar.services;

import java.util.List;

import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.StateModel;
import edu.omkar.repository.StateRepositoryImpl;

public class StateServiceImpl implements StateService{
	StateRepositoryImpl stateRepo = new StateRepositoryImpl();
	@Override
	public boolean isAddState(StateModel model) {
		
		return stateRepo.isAddState(model);
	}
	@Override
	public List<StateModel> getAllStates() {
		
		return stateRepo.getAllStates();
	}
	@Override
	public boolean isDeleteStateById(int id) {
		// TODO Auto-generated method stub
		return stateRepo.isDeleteStateById(id);
	}
	@Override
	public boolean idUpdateState(StateModel model) {
	
		return stateRepo.idUpdateState(model);
	}
}
