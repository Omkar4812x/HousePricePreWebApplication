package edu.omkar.repository;
import java.util.*;
import edu.omkar.model.StateModel;

public interface StateRepository {
	public boolean isAddState(StateModel model);
	public List<StateModel> getAllStates();
	public boolean isDeleteStateById(int id);
	public boolean idUpdateState(StateModel model);
}
