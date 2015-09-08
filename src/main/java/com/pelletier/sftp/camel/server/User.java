package com.pelletier.sftp.camel.server;

/**
 * This class was used to help with the FileUserDao implementation.
 * If that is not getting used, this can probably be thrown away.
 * 
 * @author Ryan Pelletier
 *
 */
public class User {
	private String username;
	private String password;
	private String ftpServerUserTrigger;
	
	public User() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFtpServerUserTrigger(String ftpServerUserTrigger) {
		this.ftpServerUserTrigger = ftpServerUserTrigger;
	}

	public String getFtpServerUserTrigger() {
		return ftpServerUserTrigger;
	}

}
