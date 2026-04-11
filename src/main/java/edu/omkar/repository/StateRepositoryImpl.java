package edu.omkar.repository;

import java.util.ArrayList;
import java.util.List;

import edu.omkar.dbconfig.DBConfig;
import edu.omkar.model.StateModel;

public class StateRepositoryImpl extends DBConfig implements StateRepository {
		List<StateModel> stateList;
	public boolean isAddState(StateModel model) {
		try {
			
			stmt = conn.prepareStatement("insert into state values('0',?,?)");
			stmt.setString(1, model.getName());
			stmt.setInt(2, 1);
			int value = stmt.executeUpdate();
			
			if(value>0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch(Exception ex)
		{
			System.out.println("Error is :"+ex);
			return false;
			
		}
		
	}

	@Override
	public List<StateModel> getAllStates() {
		try {
			stateList = new ArrayList<StateModel>();
			
			stmt = conn.prepareStatement("Select * from state where status = 1  order by stateid");
			rs=stmt.executeQuery();
			while(rs.next()) {
				StateModel sm = new StateModel();
				sm.setId(rs.getInt(1));
				sm.setName(rs.getString(2));
				stateList.add(sm);
			}
			return stateList;
			
		}
		catch(Exception ex)
		{
			System.out.println("Error is :"+ex);
			return null;
		}
		
	}

	@Override
	public boolean isDeleteStateById(int id) {
		try {
			stmt = conn.prepareStatement("update state set status = 0 where stateid = ?");
			stmt.setInt(1, id);
			int value = stmt.executeUpdate();
			if(value>0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch(Exception ex)
		{
			System.out.println("Error is "+ex);
			return false;
		}
	}

	@Override
	public boolean idUpdateState(StateModel model) {
		try {
		stmt = conn.prepareStatement("update state set statename=? where stateid = ? ");
		stmt.setString(1, model.getName());
		stmt.setInt(2,model.getId());
		int value = stmt.executeUpdate();
		if(value>0)
		{
			return true;
		}
		else
		{
			return false;
		}
		}
		catch(Exception ex)
		{
			System.out.println("Error is :"+ex);
			return false;
		}
	
	}
}
