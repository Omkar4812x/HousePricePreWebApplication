package edu.omkar.services;

import java.util.Optional;

import edu.omkar.model.AdminLoginModel;
import edu.omkar.repository.ValidateAdminRepo;
import edu.omkar.repository.ValidateAdminRepoImpl;

public class ValidateAdminServiceImpl implements ValidateAdminService{
	
	ValidateAdminRepo validateAdminRepo;
	
	@Override
	public Optional<AdminLoginModel> validateAdmin(AdminLoginModel model) {
	
		validateAdminRepo = new ValidateAdminRepoImpl();
		return validateAdminRepo.validateAdmin(model);
		
	}
	
	

}
