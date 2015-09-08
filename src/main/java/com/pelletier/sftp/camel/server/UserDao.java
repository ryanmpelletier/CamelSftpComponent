package com.pelletier.sftp.camel.server;

import com.pelletier.sftp.camel.trigger.FtpServerUserTrigger;
/**
 * Authenticate user based on username and password, or get FtpServerUserTrigger based
 * on username.
 * 
 * @author Ryan Pelletier
 *
 */
public interface UserDao {
	
	public boolean authenticateUser(String username, String password);
	
	public FtpServerUserTrigger getFtpServerUserTrigger(String username);

}
