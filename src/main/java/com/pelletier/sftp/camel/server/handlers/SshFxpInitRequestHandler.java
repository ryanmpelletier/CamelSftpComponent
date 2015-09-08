package com.pelletier.sftp.camel.server.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.sshd.sftp.Reply;
import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;

import com.pelletier.sftp.camel.server.FtpResource;
import com.pelletier.sftp.camel.server.TriggerPullResult;
import com.pelletier.sftp.camel.server.UserDao;
import com.pelletier.sftp.camel.trigger.FtpServerUserTrigger;

/**
 * Handles initialization request to SFTP server, resolves FtpServerUserTrigger to use and
 * creates an empty TriggerPullResult on the session.<br>
 * <b>NOTE :</b> The userDao can be left null, but if it is, the defaultTriggers map
 * must be configured in order for an FtpServerUserTrigger to be resolved for the 
 * logged in user. 
 * 
 * @author Ryan Pelletier
 *
 */

public class SshFxpInitRequestHandler extends AbstractRequestHandler {

	/**
	 * Map of usernames and ftpServerUserTriggers from xml configuration files
	 */
	private UserDao userDao = null;
	
	/**
	 * Must be configured if userDao is null, this contains the key,
	 * which is what is searched for 
	 */
	private Map<String,FtpServerUserTrigger> defaultTriggers = null;
	
	/**
	 * Sets the triggerPullResult, and an ftpServerUserTrigger on the session.
	 */
	@Override
	public Reply handle(SftpSession session, Request sftpRequest){
		
		List<FtpResource> ftpResources = new ArrayList<FtpResource>();
		TriggerPullResult triggerPullResult = new TriggerPullResult(ftpResources);
		triggerPullResult.setUsername(session.getSession().getUsername());
		
		session.getSession().setAttribute(TRIGGER_KEY, triggerPullResult);
		FtpServerUserTrigger ftpServerUserTrigger = (userDao == null ? null : userDao.getFtpServerUserTrigger(session.getSession().getUsername()));
		session.getSession().setAttribute(TRIGGER, (ftpServerUserTrigger == null ? getDefaultFtpServerUserTrigger(session.getSession().getUsername()) : ftpServerUserTrigger));
		return null;
	}
	
	/**
	 * If the username string contains the key from the default triggers map, return the value 
	 * from the map, which is the FtpServerUserTrigger
	 * @param username
	 * @return
	 */
	private FtpServerUserTrigger getDefaultFtpServerUserTrigger(String username){
		for(String key : this.defaultTriggers.keySet()){
			if(username.toLowerCase().contains(key)){
				return this.defaultTriggers.get(key);
			}
		}
		return null;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setDefaultTriggers(Map<String, FtpServerUserTrigger> defaultTriggers) {
		this.defaultTriggers = defaultTriggers;
	}


}
