package edu.omkar.repository;

import java.util.Optional;

import edu.omkar.model.AdminLoginModel;

public interface ValidateAdminRepo {
	public Optional<AdminLoginModel> validateAdmin(AdminLoginModel model);
}
