package com.pelletier.sftp.camel.trigger;

import org.apache.sshd.sftp.Request;
import org.apache.sshd.sftp.SftpSession;

import com.pelletier.sftp.camel.server.TriggerPullResult;

public interface FtpServerUserTrigger {
	
	/**
	 * Determines whether or not the user trigger should fire, or pull uploaded resources
	 * from the FTP server
	 * 
	 * 
	 * @return boolean to determine if fireTrigger method should be called
	 */
	boolean shouldFire(SftpSession session, Request request);
	
	/**
	 * Aggregates one or more uploaded resources (depending on the trigger) for processing
	 * by the TriggerPullResultHandler
	 * 
	 * @return TriggerPullResult object containing uploaded resources from the FTP server
	 */
	TriggerPullResult fireTrigger(SftpSession session);
	
}
