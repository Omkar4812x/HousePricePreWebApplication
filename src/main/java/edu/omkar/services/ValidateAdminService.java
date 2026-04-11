package edu.omkar.services;

import java.util.Optional;

import edu.omkar.model.AdminLoginModel;

public interface ValidateAdminService {
	public Optional<AdminLoginModel> validateAdmin(AdminLoginModel model);
}
