package edu.omkar.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginModel {
	private int userId;
	private String username;
	private String password;
	private String usertype;
}
