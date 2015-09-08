package com.pelletier.sftp.camel.server;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * Implementation for authenticating user credentials based on username and password.<br>
 * <b>NOTE :</b>UserDao can be left null, which will allow all users to log in. UserDao
 * can be implemented for certain user access scenarios. 
 * 
 * @author Ryan Pelletier
 *
 */
public class PasswordAuthenticatorImp implements PasswordAuthenticator {
	
	private UserDao userDao = null;
	private boolean allowAll = false;
	
	@Override
	public boolean authenticate(String username, String password, ServerSession serverSession) {
		if(allowAll || userDao == null){
			return true;
		}
		return userDao.authenticateUser(username, password);
	}

	public void setAllowAll(boolean allowAll) {
		this.allowAll = allowAll;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
