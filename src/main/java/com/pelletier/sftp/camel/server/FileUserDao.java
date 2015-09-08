package com.pelletier.sftp.camel.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.pelletier.sftp.camel.trigger.FtpServerUserTrigger;

/**
 * Reads a properties file to get users.<br>
 * <b>NOTE</b> This has not been tested, minimal time was put into this,
 * as I did not think it would be actually getting used.
 * 
 * @author Ryan Pelletier
 *
 */
public class FileUserDao implements UserDao {

	/**
	 * <className of user trigger, FtpServerUserTrigger>
	 */
	private Map<String, FtpServerUserTrigger> ftpServerUserTriggers = null;
	private String filename = null;

	public FileUserDao() {
	}

	/**
	 * Return true if the user should be able to use the SFTP server
	 */
	@Override
	public boolean authenticateUser(String username, String password) {
		return isAuthenticatedUser(getUserFromConfigFile(username), username, password);
	}
	
	/**
	 * Returns the FtpServerUserTrigger associated with this User
	 */
	@Override
	public FtpServerUserTrigger getFtpServerUserTrigger(String username) {
		return ftpServerUserTriggers.get(getUserFromConfigFile(username).getFtpServerUserTrigger());
	}
	
	private boolean isAuthenticatedUser(User user, String username, String password){
		return user == null ? false : (user.getPassword().equals(password) && user.getUsername().equals(username));
	}
	
	private User getUserFromConfigFile(String username){
		try {
			List<String> lines = IOUtils.readLines(new FileReader(new File(filename)));
			lines.add("");
			User user = new User();
			for (String line : lines) {
				if(line.equals("")){
					if(user.getUsername().equals(username)){
						return user;
					}
					user = new User();
					continue;
				}
				
				String property = line.split("\\.")[1];
				if(property.contains("username")){
					user.setUsername(property.split("=")[1]);
				}else if(property.contains("password")){
					user.setPassword(property.split("=")[1]);
				}else if(property.contains("trigger")){
					user.setFtpServerUserTrigger(property.split("=")[1]);
				}else{
					throw new Exception("The configuration file could not be parsed : " + property);
				}
			}
			return user;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void setFtpServerUserTriggers(Map<String, FtpServerUserTrigger> ftpServerUserTriggers) {
		this.ftpServerUserTriggers = ftpServerUserTriggers;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
